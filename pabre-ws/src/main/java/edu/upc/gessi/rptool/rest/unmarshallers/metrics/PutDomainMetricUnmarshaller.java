package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.ArrayList;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.DomainMetric;
import edu.upc.gessi.rptool.domain.metrics.DomainMetricValue;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class PutDomainMetricUnmarshaller extends DomainMetricUnmarshaller {

    @JsonCreator
    public PutDomainMetricUnmarshaller(@JsonProperty(value = "name", required = false) String name,
	    @JsonProperty(value = "description", required = false) String description,
	    @JsonProperty(value = "comments", required = false) String comments,
	    @JsonProperty(value = "defaultValue", required = false) String defaultValueString,
	    @JsonProperty(value = "possibleValues", required = false) ArrayList<String> possibleValues,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sourcesByIdentifier)
	    throws SemanticallyIncorrectException {

	super(name, description, comments, defaultValueString, possibleValues, sources, sourcesByIdentifier);

    }

    @Override
    protected void checkPossibleValues() throws SemanticallyIncorrectException {

	if (possibleValues != null && defaultValueString != null && possibleValues.size() > 0
		&& !possibleValues.contains(defaultValueString))
	    throw new SemanticallyIncorrectException("defaultValue not in possibleValues");

	if (possibleValues != null && possibleValues.isEmpty()) {
	    throw new SemanticallyIncorrectException("possibleValues is empty");
	}
    }

    @Override
    protected void setDefaultValues() {
	if (defaultValueString != null) {
	    ((DomainMetric) metric).setDefaultValue(new DomainMetricValue(defaultValueString));
	}
    }

    @Override
    protected void setMetricName() throws IntegrityException {
	metric.setName(name);
    }
}
