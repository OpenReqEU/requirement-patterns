package edu.upc.gessi.rptool.rest.dtos.importexport;

import java.util.List;

import edu.upc.gessi.rptool.rest.dtos.importexport.classifications.SchemaExportDTO;
import edu.upc.gessi.rptool.rest.dtos.importexport.metrics.MetricExportDTO;
import edu.upc.gessi.rptool.rest.dtos.importexport.patternelements.RequirementPatternExportDTO;

public class ExportDTO {
    private List<SourceExportDTO> sources;
    private List<KeywordExportDTO> keywords;
    private List<MetricExportDTO> metrics;
    private List<RequirementPatternExportDTO> patterns;
    private List<SchemaExportDTO> schemas;
    private List<DependenciesExportDTO> dependencies;

    public ExportDTO() {
    }

    public ExportDTO(List<SourceExportDTO> sources, List<KeywordExportDTO> keywords, List<MetricExportDTO> metrics,
	    List<RequirementPatternExportDTO> patterns, List<SchemaExportDTO> schemas,
	    List<DependenciesExportDTO> dependencies) {
	super();
	this.sources = sources;
	this.keywords = keywords;
	this.metrics = metrics;
	this.patterns = patterns;
	this.schemas = schemas;
	this.dependencies = dependencies;
    }

    public List<SourceExportDTO> getSources() {
	return sources;
    }

    public void setSources(List<SourceExportDTO> sources) {
	this.sources = sources;
    }

    public List<KeywordExportDTO> getKeywords() {
	return keywords;
    }

    public void setKeywords(List<KeywordExportDTO> keywords) {
	this.keywords = keywords;
    }

    public List<MetricExportDTO> getMetrics() {
	return metrics;
    }

    public void setMetrics(List<MetricExportDTO> metrics) {
	this.metrics = metrics;
    }

    public List<RequirementPatternExportDTO> getPatterns() {
	return patterns;
    }

    public void setPatterns(List<RequirementPatternExportDTO> patterns) {
	this.patterns = patterns;
    }

    public List<SchemaExportDTO> getSchemas() {
	return schemas;
    }

    public void setSchemas(List<SchemaExportDTO> schemas) {
	this.schemas = schemas;
    }

    public List<DependenciesExportDTO> getDependencies() {
	return dependencies;
    }

    public void setDependencies(List<DependenciesExportDTO> dependencies) {
	this.dependencies = dependencies;
    }

}
