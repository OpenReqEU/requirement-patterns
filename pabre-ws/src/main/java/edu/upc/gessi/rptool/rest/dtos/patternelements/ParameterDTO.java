package edu.upc.gessi.rptool.rest.dtos.patternelements;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.metrics.SetMetric;
import edu.upc.gessi.rptool.domain.metrics.Type;
import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.rest.dtos.metrics.MetricDTO;
import edu.upc.gessi.rptool.rest.dtos.metrics.SetMetricDTO;

@JsonInclude(Include.NON_NULL)
public class ParameterDTO extends ReducedPatternObjectDTO {

    private String name;
    private String correctnessCondition;
    private String description;
    private MetricDTO metric;

    public ParameterDTO(Parameter p) {
	super(p.getId());
	this.name = p.getName();
	this.correctnessCondition = p.getCorrectnessCondition();
	this.description = p.getDescription();
	if (p.getMetric().getType() == Type.SET) {
	    this.metric = new SetMetricDTO((SetMetric) p.getMetric());
	} else {
	    this.metric = new MetricDTO(p.getMetric());
	}
    }

    public String getCorrectnessCondition() {
	return correctnessCondition;
    }

    public String getDescription() {
	return description;
    }

    @Override
    public long getId() {
	return id;
    }

    public MetricDTO getMetric() {
	return metric;
    }

    public String getName() {
	return name;
    }

    public void setCorrectnessCondition(String correctnessCondition) {
	this.correctnessCondition = correctnessCondition;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public void setId(long id) {
	this.id = id;
    }

    public void setMetric(MetricDTO metric) {
	this.metric = metric;
    }

    public void setName(String name) {
	this.name = name;
    }

}
