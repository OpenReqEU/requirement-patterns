package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.DomainMetricValue;

public class PossibleValueUnmarshaller {
    private String value;

    @JsonCreator
    public PossibleValueUnmarshaller(@JsonProperty(value = "value", required = true) String value) {
	this.value = value;
    }

    public DomainMetricValue build() {
	return new DomainMetricValue(value);
    }
}
