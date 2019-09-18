package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.FloatMetric;
import edu.upc.gessi.rptool.exceptions.ValueException;

public class FloatMetricUnmarshaller extends UnmarshallerGenericMetric {

    protected Float minValue;
    protected Float maxValue;
    protected Float defaultValue;

    @JsonCreator
    public FloatMetricUnmarshaller(@JsonProperty(value = "id", required = false) Long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "minValue", required = true) Float minValue,
	    @JsonProperty(value = "maxValue", required = true) Float maxValue,
	    @JsonProperty(value = "defaultValue", required = false) Float defaultValue,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sourcesByIdentifier) {
	this.id = id == null ? 0 : id;
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.minValue = minValue;
	this.maxValue = maxValue;
	this.defaultValue = defaultValue;
	this.sources = sources;
	this.sourcesByIdentifier = sourcesByIdentifier;

    }

    public FloatMetricUnmarshaller(String name, String description, String comments, Float minValue, Float maxValue,
	    Float defaultValue, Set<Long> sources, Set<String> sourcesByIdentifier) {
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.minValue = minValue;
	this.maxValue = maxValue;
	this.defaultValue = defaultValue;
	this.sources = sources;
	this.sourcesByIdentifier = sourcesByIdentifier;

    }

    public FloatMetricUnmarshaller(String name, String description, String comments, Float minValue, Float maxValue,
	    Float defaultValue) {
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.minValue = minValue;
	this.maxValue = maxValue;
	this.defaultValue = defaultValue;
    }

    @Override
    public void instantiateInternalMetric() {
	metric = new FloatMetric();

    }

    protected void setMinMaxDefaultValues() throws ValueException {
	((FloatMetric) metric).setMinMax(minValue, maxValue);
	((FloatMetric) metric).setDefaultValue(defaultValue);
    }

    @Override
    public void myBuild() throws ValueException {
	setMinMaxDefaultValues();
    }

}
