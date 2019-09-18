package edu.upc.gessi.rptool.rest.dtos.importexport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;
import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.patternelements.Keyword;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;

@JsonInclude(Include.NON_NULL)
public class ImportDTO {
    private Map<String, Long> sourceIDs;
    private Map<String, Long> keywordsIDs;
    private Map<String, Long> metricsIDs;
    private Map<String, Long> patternsIDs;
    private Map<String, Long> schemasIDs;
    private Map<String, Long> dependenciesIDs;

    public ImportDTO(Map<String, Long> sourceIDs, Map<String, Long> keywordsIDs, Map<String, Long> metricsIDs,
	    Map<String, Long> patternsIDs, Map<String, Long> schemasIDs, Map<String, Long> dependenciesIDs) {
	this.sourceIDs = sourceIDs;
	this.keywordsIDs = keywordsIDs;
	this.metricsIDs = metricsIDs;
	this.patternsIDs = patternsIDs;
	this.schemasIDs = schemasIDs;
	this.dependenciesIDs = dependenciesIDs;
    }

    public ImportDTO(List<Source> sources, List<Keyword> keywords, List<Metric> metrics,
	    List<RequirementPattern> patterns, List<ClassificationSchema> schemas,
	    List<PatternObjectCompleteDependency> dependencies) {
	sourceIDs = new HashMap<>();
	keywordsIDs = new HashMap<>();
	metricsIDs = new HashMap<>();
	patternsIDs = new HashMap<>();
	schemasIDs = new HashMap<>();
	dependenciesIDs = new HashMap<>();

	for (int i = 0; i < sources.size(); i++) {
	    sourceIDs.put(sources.get(i).getIdentifier(), sources.get(i).getId());
	}
	for (int i = 0; i < keywords.size(); i++) {
	    keywordsIDs.put(keywords.get(i).getName(), keywords.get(i).getId());
	}
	for (int i = 0; i < metrics.size(); i++) {
	    metricsIDs.put(metrics.get(i).getName(), metrics.get(i).getId());
	}
	for (int i = 0; i < patterns.size(); i++) {
	    patternsIDs.put(patterns.get(i).getName(), patterns.get(i).getId());
	}
	for (int i = 0; i < schemas.size(); i++) {
	    schemasIDs.put(schemas.get(i).getName(), schemas.get(i).getId());
	}

	for (int i = 0; i < dependencies.size(); i++) {
	    PatternObjectCompleteDependency pocd = dependencies.get(i);
	    String nameToShow = pocd.getPatternObject().getId() + "-"
		    + pocd.getPatternObjectDependency().getDependency().getId();
	    dependenciesIDs.put(nameToShow, pocd.getPatternObjectDependency().getId());
	}
    }

    public Map<String, Long> getSourceIDs() {
	return sourceIDs;
    }

    public void setSourceIDs(Map<String, Long> sourceIDs) {
	this.sourceIDs = sourceIDs;
    }

    public Map<String, Long> getKeywordsIDs() {
	return keywordsIDs;
    }

    public void setKeywordsIDs(Map<String, Long> keywordsIDs) {
	this.keywordsIDs = keywordsIDs;
    }

    public Map<String, Long> getMetricsIDs() {
	return metricsIDs;
    }

    public void setMetricsIDs(Map<String, Long> metricsIDs) {
	this.metricsIDs = metricsIDs;
    }

    public Map<String, Long> getPatternsIDs() {
	return patternsIDs;
    }

    public void setPatternsIDs(Map<String, Long> patternsIDs) {
	this.patternsIDs = patternsIDs;
    }

    public Map<String, Long> getSchemasIDs() {
	return schemasIDs;
    }

    public void setSchemasIDs(Map<String, Long> schemasIDs) {
	this.schemasIDs = schemasIDs;
    }

    public Map<String, Long> getDependenciesIDs() {
	return dependenciesIDs;
    }

    public void setDependenciesIDs(Map<String, Long> dependenciesIDs) {
	this.dependenciesIDs = dependenciesIDs;
    }

}