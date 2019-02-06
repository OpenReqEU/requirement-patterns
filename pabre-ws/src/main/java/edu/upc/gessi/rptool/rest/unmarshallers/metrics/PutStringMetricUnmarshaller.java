package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.StringMetric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;

public class PutStringMetricUnmarshaller extends StringMetricUnmarshaller {

    @JsonCreator
    public PutStringMetricUnmarshaller(@JsonProperty(value = "name", required = false) String name,
	    @JsonProperty(value = "description", required = false) String description,
	    @JsonProperty(value = "comments", required = false) String comments,
	    @JsonProperty(value = "defaultValue", required = false) String defaultValue,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources) {
	super(name, description, comments, defaultValue, sources);
    }

    @Override
    protected void setDefaultValue() throws ValueException {

	((StringMetric) metric).setDefaultValueNoCheck(defaultValue);
    }

    @Override
    protected void setMetricName() throws IntegrityException {
	metric.setName(name);
    }
}
