package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.MetricDataController;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class ParameterUnmarshaller {
    protected Parameter p;
    protected long id;
    protected String name;
    protected String correctnessCondition;
    protected String description;
    protected Long metricId;

    @JsonCreator
    public ParameterUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "correctnessCondition", required = false) String correctnessCondition,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "metricId", required = true) Long metricId) {
	this.id = id;
	this.correctnessCondition = correctnessCondition;
	this.name = name;
	this.description = description;
	this.metricId = metricId;
    }

    public ParameterUnmarshaller(String name, String correctnessCondition, String description) {
	this.name = name;
	this.correctnessCondition = correctnessCondition;
	this.description = description;
    }

    public ParameterUnmarshaller(long id, String name, String correctnessCondition, String description) {
	this.id = id;
	this.name = name;
	this.correctnessCondition = correctnessCondition;
	this.description = description;
    }

    public Parameter build() throws SemanticallyIncorrectException {
	Metric m = retreiveMetris();
	p = new Parameter();
	p.setName(name);
	p.setCorrectnessCondition(correctnessCondition);
	p.setDescription(description);
	p.setMetric(m);
	p.setId(id);
	return p;
    }

    protected Metric retreiveMetris() throws SemanticallyIncorrectException {
	Metric m = MetricDataController.getMetric(metricId);
	if (m == null)
	    throw new SemanticallyIncorrectException("invalid metric id in parameter");
	return m;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCorrectnessCondition() {
	return correctnessCondition;
    }

    public void setCorrectnessCondition(String correctnessCondition) {
	this.correctnessCondition = correctnessCondition;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Long getMetricId() {
	return metricId;
    }

    public void setMetricId(Long metricId) {
	this.metricId = metricId;
    }

}
