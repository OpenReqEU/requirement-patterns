package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.exceptions.IntegrityException;

public class PutSetMetricUnmarshaller extends SetMetricUnmarshaller {

    @JsonCreator
    public PutSetMetricUnmarshaller(@JsonProperty(value = "name", required = false) String name,
	    @JsonProperty(value = "description", required = false) String description,
	    @JsonProperty(value = "comments", required = false) String comments,
	    @JsonProperty(value = "idSimple", required = false) Long idSimple,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sourcesByIdentifier) {
	super(name, description, comments, idSimple, sources, sourcesByIdentifier);
    }

    @Override
    protected void setMetricName() throws IntegrityException {
	metric.setName(name);
    }

}
