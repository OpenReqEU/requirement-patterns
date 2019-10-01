package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.config.Control;
import edu.upc.gessi.rptool.domain.metrics.TimePointMetric;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class TimeMetricImportUnmarshaller extends TimeMetricUnmarshaller {

    private String stringDate;

    public TimeMetricImportUnmarshaller(@JsonProperty(value = "id", required = false) Long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "date", required = false) String date,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sourcesByIdentifier)
	    throws SemanticallyIncorrectException {
	super(name, description, comments);
	if (date.length() != 19) {
	    throw new SemanticallyIncorrectException("Date malformed");
	}
	this.id = id == null ? 0 : id;
	this.stringDate = date;
	this.sourcesByIdentifier = sourcesByIdentifier;

    }

    @Override
    public void myBuild() {
	try {
	    ((TimePointMetric) metric).setTimeAttrib(stringDate);
	} catch (NullPointerException e) {
		Control.getInstance().showErrorMessage(e.getMessage());
	}
    }

}
