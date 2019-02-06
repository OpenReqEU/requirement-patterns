package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.StringMetric;
import edu.upc.gessi.rptool.exceptions.ValueException;

public class StringMetricUnmarshaller extends UnmarshallerGenericMetric {

    protected String defaultValue;

    @JsonCreator
    public StringMetricUnmarshaller(@JsonProperty(value = "id", required = false) Long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "defaultValue", required = false) String defaultValue,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sourcesByIdentifier) {
	this.id = id == null ? 0 : id;
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.sources = sources;
	this.defaultValue = defaultValue;
	this.sourcesByIdentifier = sourcesByIdentifier;
    }

    public StringMetricUnmarshaller(String name, String description, String comments, String defaultValue,
	    Set<Long> sources) {
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.sources = sources;
	this.defaultValue = defaultValue;
    }

    @Override
    public void instantiateInternalMetric() {
	metric = new StringMetric();

    }

    protected void setDefaultValue() throws ValueException {
	if (defaultValue != null)
	    ((StringMetric) metric).setDefaultValue(defaultValue);
    }

    @Override
    public void myBuild() throws ValueException {

	setDefaultValue();
    }

}
