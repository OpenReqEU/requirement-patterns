package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.config.Control;
import edu.upc.gessi.rptool.domain.metrics.TimePointMetric;

public class TimeMetricUnmarshaller extends UnmarshallerGenericMetric {

    protected Date date;

    @Override
    public void instantiateInternalMetric() {
	metric = new TimePointMetric();
    }

    @JsonCreator
    public TimeMetricUnmarshaller(@JsonProperty(value = "id", required = false) Long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "date", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") Date date,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sourcesByIdentifier) {
	this.id = id == null ? 0 : id;
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.date = date;
	this.sources = sources;
	this.sourcesByIdentifier = sourcesByIdentifier;
    }

    public TimeMetricUnmarshaller(String name, String description, String comments) {
	this.name = name;
	this.description = description;
	this.comments = comments;
    }

    public TimeMetricUnmarshaller(String name, String description, String comments, Date date2, Set<Long> sources) {
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.date = date2;
	this.sources = sources;
    }

    @Override
    public void myBuild() {
	if (date != null) {
	    try {
		((TimePointMetric) metric).setTimeAttrib(date);
	    } catch (NullPointerException e) {
			Control.getInstance().showErrorMessage(e.getMessage());
		throw e;
	    }
	}
    }

}
