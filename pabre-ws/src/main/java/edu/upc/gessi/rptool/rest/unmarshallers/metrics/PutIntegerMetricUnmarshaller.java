package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.IntegerMetric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;

public class PutIntegerMetricUnmarshaller extends IntegerMetricUnmarshaller {

    @JsonCreator
    public PutIntegerMetricUnmarshaller(@JsonProperty(value = "name", required = false) String name,
	    @JsonProperty(value = "description", required = false) String description,
	    @JsonProperty(value = "comments", required = false) String comments,
	    @JsonProperty(value = "minValue", required = false) Integer minValue,
	    @JsonProperty(value = "maxValue", required = false) Integer maxValue,
	    @JsonProperty(value = "defaultValue", required = false) Integer defaultValue,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sourcesByIdentifier) {
	super(name, description, comments, minValue, maxValue, defaultValue, sources, sourcesByIdentifier);

    }

    @Override
    protected void setMinMaxDefaultValues() throws ValueException {
	((IntegerMetric) metric).setMinNoCheck(minValue);
	((IntegerMetric) metric).setMaxNoCheck(maxValue);
	((IntegerMetric) metric).setDefaultValueNoCheck(defaultValue);
    }

    @Override
    protected void setMetricName() throws IntegrityException {
	metric.setName(name);
    }

}
