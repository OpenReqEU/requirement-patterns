package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.FloatMetric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;

public class PutFloatMetricUnmarshaller extends FloatMetricUnmarshaller {

    @JsonCreator
    public PutFloatMetricUnmarshaller(@JsonProperty(value = "name", required = false) String name,
	    @JsonProperty(value = "description", required = false) String description,
	    @JsonProperty(value = "comments", required = false) String comments,
	    @JsonProperty(value = "minValue", required = false) Float minValue,
	    @JsonProperty(value = "maxValue", required = false) Float maxValue,
	    @JsonProperty(value = "defaultValue", required = false) Float defaultValue,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sourcesByIdentifier)

    {
	super(name, description, comments, minValue, maxValue, defaultValue, sources, sourcesByIdentifier);

    }

    @Override
    protected void setMinMaxDefaultValues() throws ValueException {
	((FloatMetric) metric).setMinNoCheck(minValue);
	((FloatMetric) metric).setMaxNoCheck(maxValue);
	((FloatMetric) metric).setDefaultValueNoCheck(defaultValue);
    }

    @Override
    protected void setMetricName() throws IntegrityException {
	metric.setName(name);
    }

}
