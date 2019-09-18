package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.IntegerMetric;
import edu.upc.gessi.rptool.exceptions.ValueException;

public class IntegerMetricUnmarshaller extends UnmarshallerGenericMetric {

    protected Integer minValue;
    protected Integer maxValue;
    protected Integer defaultValue;

    @JsonCreator
    public IntegerMetricUnmarshaller(@JsonProperty(value = "id", required = false) Long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "minValue", required = true) Integer minValue,
	    @JsonProperty(value = "maxValue", required = true) Integer maxValue,
	    @JsonProperty(value = "defaultValue", required = false) Integer defaultValue,
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

    public IntegerMetricUnmarshaller(String name, String description, String comments, Integer minValue,
	    Integer maxValue, Integer defaultValue, Set<Long> sources, Set<String> sourcesByIdentifier) {
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.minValue = minValue;
	this.maxValue = maxValue;
	this.defaultValue = defaultValue;
	this.sources = sources;
	this.sourcesByIdentifier = sourcesByIdentifier;

    }

    @Override
    public void instantiateInternalMetric() {
	metric = new IntegerMetric();

    }

    protected void setMinMaxDefaultValues() throws ValueException {
	((IntegerMetric) metric).setMinMax(minValue, maxValue);
	((IntegerMetric) metric).setDefaultValue(defaultValue);
    }

    @Override
    public void myBuild() throws ValueException {

	setMinMaxDefaultValues();

    }

}
