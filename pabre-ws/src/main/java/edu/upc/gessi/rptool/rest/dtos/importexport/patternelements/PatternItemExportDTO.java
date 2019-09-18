package edu.upc.gessi.rptool.rest.dtos.importexport.patternelements;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.domain.patternelements.PatternItem;

@JsonInclude(Include.NON_NULL)
public class PatternItemExportDTO {

    private long id;
    private String patternText;
    private String questionText;
    private Integer numInstances;
    private Boolean available;
    private Integer statsNumInstances;
    private String artifactsRelation;
    private Set<ParameterExportDTO> parameters;

    public PatternItemExportDTO(PatternItem pi, String artifactsRelation) {

	this.id = pi.getId();
	this.patternText = pi.getPatternText();
	this.questionText = pi.getQuestionText();
	this.numInstances = pi.getNumInstances();
	this.available = pi.getAvailable();
	this.statsNumInstances = pi.getStatsNumInstances();
	this.artifactsRelation = artifactsRelation;
	this.parameters = new HashSet<>();
	for (Parameter p : pi.getParameters()) {
	    this.parameters.add(new ParameterExportDTO(p));
	}
    }

    public String getPatternText() {
	return patternText;
    }

    public void setPatternText(String patternText) {
	this.patternText = patternText;
    }

    public String getQuestionText() {
	return questionText;
    }

    public void setQuestionText(String questionText) {
	this.questionText = questionText;
    }

    public Integer getNumInstances() {
	return numInstances;
    }

    public void setNumInstances(Integer numInstances) {
	this.numInstances = numInstances;
    }

    public Boolean getAvailable() {
	return available;
    }

    public void setAvailable(Boolean available) {
	this.available = available;
    }

    public Integer getStatsNumInstances() {
	return statsNumInstances;
    }

    public void setStatsNumInstances(Integer statsNumInstances) {
	this.statsNumInstances = statsNumInstances;
    }

    public String getArtifactsRelation() {
	return artifactsRelation;
    }

    public void setArtifactsRelation(String artifactsRelation) {
	this.artifactsRelation = artifactsRelation;
    }

    public Set<ParameterExportDTO> getParameters() {
	return parameters;
    }

    public void setParameters(Set<ParameterExportDTO> parameters) {
	this.parameters = parameters;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

}
