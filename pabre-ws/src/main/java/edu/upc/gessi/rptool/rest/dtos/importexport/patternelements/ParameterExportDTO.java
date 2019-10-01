package edu.upc.gessi.rptool.rest.dtos.importexport.patternelements;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.Parameter;

@JsonInclude(Include.NON_NULL)
public class ParameterExportDTO {
    private long id;
    private String name;
    private String correctnessCondition;
    private String description;
    private String metricName;

    public ParameterExportDTO(Parameter p) {
	this.id = p.getId();
	this.name = p.getName();
	this.correctnessCondition = p.getCorrectnessCondition();
	this.description = p.getDescription();
	this.metricName = p.getMetric().getName();
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
	return name;
    }

    public String getCorrectnessCondition() {
	return correctnessCondition;
    }

    public void setCorrectnessCondition(String correctnessCondition) {
	this.correctnessCondition = correctnessCondition;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
	return description;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getMetricName() {
	return metricName;
    }

}
