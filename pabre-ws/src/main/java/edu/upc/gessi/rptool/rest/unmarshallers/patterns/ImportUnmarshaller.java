package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.data.MetricDataController;
import edu.upc.gessi.rptool.data.SchemaDataController;
import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;
import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.metrics.DomainMetric;
import edu.upc.gessi.rptool.domain.metrics.DomainMetricValue;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.Type;
import edu.upc.gessi.rptool.domain.patternelements.Keyword;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.RedundancyException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.exceptions.MissingCreatorPropertyException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.DependenciesImportUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.classifications.SchemaImportUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.keywords.KeywordUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.GenericMetricImportCreatorUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.sources.SourceUnmarshaller;

public class ImportUnmarshaller {

    private static final Logger logger = Logger.getLogger(ImportUnmarshaller.class.getName());

    // UNMARSHALLERS
    private List<SourceUnmarshaller> sources;
    private List<KeywordUnmarshaller> keywords;
    private List<GenericMetricImportCreatorUnmarshaller> metrics;
    private List<RequirementPatternImportUnmarshaller> patterns;
    private List<SchemaImportUnmarshaller> schemas;
    private List<DependenciesImportUnmarshaller> dependencies;

    private List<Source> sourcesOBJ;
    private List<Keyword> keywordsOBJ;
    private List<Metric> metricsOBJ;
    private List<RequirementPattern> patternsOBJ;
    private List<ClassificationSchema> schemasOBJ;
    private List<PatternObjectCompleteDependency> dependenciesOBJ;

    @JsonCreator
    public ImportUnmarshaller(@JsonProperty(value = "sources", required = false) List<SourceUnmarshaller> sources,
	    @JsonProperty(value = "keywords", required = false) List<KeywordUnmarshaller> keywords,
	    @JsonProperty(value = "metrics", required = false) List<GenericMetricImportCreatorUnmarshaller> metrics,
	    @JsonProperty(value = "patterns", required = false) List<RequirementPatternImportUnmarshaller> patterns,
	    @JsonProperty(value = "schemas", required = false) List<SchemaImportUnmarshaller> schemas,
	    @JsonProperty(value = "dependencies", required = false) List<DependenciesImportUnmarshaller> dependencies) {
	this.sources = sources;
	this.keywords = keywords;
	this.metrics = metrics;
	// Sort the List to have simpleMetric before the SetMetrics
	Comparator<GenericMetricImportCreatorUnmarshaller> cmpMetrics = (o1, o2) -> {
	int res = 0;
	String o1Type = o1.getType();
	String o2Type = o2.getType();
	String setMetricName = Type.SET.toString();
	if (!o1Type.equals(setMetricName) && o2Type.equals(setMetricName)) // o1 is simple and o2 is not
		res = -1;
	else if (o1Type.equals(setMetricName) && !o2Type.equals(setMetricName)) // o1 is set and o2 is not
		res = 1;
	return res;
	};
	sortMetrics(metrics, cmpMetrics);
	this.patterns = patterns;
	this.schemas = schemas;
	this.dependencies = dependencies;
	sourcesOBJ = new ArrayList<>();
	keywordsOBJ = new ArrayList<>();
	metricsOBJ = new ArrayList<>();
	patternsOBJ = new ArrayList<>();
	schemasOBJ = new ArrayList<>();
	dependenciesOBJ = new ArrayList<>();
    }

    public void buildAndSaveSources() throws SemanticallyIncorrectException {
	if (sources != null) { // check if we have source to import
	    for (int i = 0; i < sources.size(); i++) {
		Source s = sources.get(i).build();
		sourcesOBJ.add(s);

	    }
	}
    }

    public void buildKeywords() {
	if (keywords != null) { // check if we have keywords to import
	    for (int i = 0; i < keywords.size(); i++) {
		Keyword k = keywords.get(i).build();
		keywordsOBJ.add(k);
	    }
	}
    }

    public void buildAndSaveMetrics() throws MissingCreatorPropertyException, IntegrityException, ValueException,
	    SemanticallyIncorrectException, JsonMappingException {
	if (metrics != null) { // check if we have metrics to import
	    for (int i = 0; i < metrics.size(); i++) {
		Metric m = metrics.get(i).build();
		metricsOBJ.add(m);
		if (m.getType() == Type.DOMAIN) { // Special case when we have to same the DomainMetricValue
		    DomainMetric dm = (DomainMetric) m;
		    if (dm.getDefaultValue() != null) {
			MetricDataController.save(dm.getDefaultValue());// if the domain has any default save it.
		    }
		    for (DomainMetricValue dmv : dm.getPossibleValues()) {
			MetricDataController.save(dmv);// Save each possible value
		    }
		}
		MetricDataController.save(m);
		// flush to synchronize with the DB so if we have a set next he can select this
		// metric has simpleMetric
		GenericDataController.flush();
	    }
	}
    }

    public void buildPatterns() throws IntegrityException, SemanticallyIncorrectException {
	if (patterns != null) { // check if we have patterns to import
	    for (int i = 0; i < patterns.size(); i++) {
		RequirementPattern rp = patterns.get(i).build();
		patternsOBJ.add(rp);
	    }
	}
    }

    public void buildAndSaveSchemas()
	    throws HibernateException, IntegrityException, RedundancyException, SemanticallyIncorrectException {
	if (schemas != null) { // check if we have patterns to import
	    for (int i = 0; i < schemas.size(); i++) {
		ClassificationSchema cs = schemas.get(i).build();
		SchemaDataController.saveSchema(cs);
		schemasOBJ.add(cs);
	    }
	    GenericDataController.flush();
	}
    }

    public void buildAndSaveDependencies() throws SemanticallyIncorrectException {
	if (dependencies != null) { // check if we have patterns to import
	    for (int i = 0; i < dependencies.size(); i++) {
		Set<PatternObjectCompleteDependency> pocd = dependencies.get(i).buildAndSave();
		dependenciesOBJ.addAll(pocd);
	    }
	}
    }

    public List<Source> getSourcesOBJ() {
	return sourcesOBJ;
    }

    public void setSourcesOBJ(List<Source> sourcesOBJ) {
	this.sourcesOBJ = sourcesOBJ;
    }

    public List<Keyword> getKeywordsOBJ() {
	return keywordsOBJ;
    }

    public void setKeywordsOBJ(List<Keyword> keywordsOBJ) {
	this.keywordsOBJ = keywordsOBJ;
    }

    public List<Metric> getMetricsOBJ() {
	return metricsOBJ;
    }

    public void setMetricsOBJ(List<Metric> metricsOBJ) {
	this.metricsOBJ = metricsOBJ;
    }

    public List<RequirementPattern> getPatternsOBJ() {
	return patternsOBJ;
    }

    public void setPatternsOBJ(List<RequirementPattern> patternsOBJ) {
	this.patternsOBJ = patternsOBJ;
    }

    public List<ClassificationSchema> getSchemasOBJ() {
	return schemasOBJ;
    }

    public void setSchemasOBJ(List<ClassificationSchema> schemasOBJ) {
	this.schemasOBJ = schemasOBJ;
    }

    public List<PatternObjectCompleteDependency> getDependenciesOBJ() {
	return dependenciesOBJ;
    }

    public void setDependenciesOBJ(List<PatternObjectCompleteDependency> dependenciesOBJ) {
	this.dependenciesOBJ = dependenciesOBJ;
    }

    protected void sortMetrics(List<GenericMetricImportCreatorUnmarshaller> metrics,
	    Comparator<GenericMetricImportCreatorUnmarshaller> cmpMetrics) {
	if (metrics != null) {
	    logger.debug("Sorting metrics, by criteria simple and set");
	    Collections.sort(metrics, cmpMetrics);
	    logger.debug("sorted");
	}
    }

}
