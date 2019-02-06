package edu.upc.gessi.rptool.rest.dtos.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.metrics.StringMetric;

@JsonInclude(Include.ALWAYS)
public class StringMetricDTO extends MetricDTO {
    String defaultValue;

    public StringMetricDTO(StringMetric m) {
	super(m);
	this.defaultValue = m.getDefaultValue();
    }

    public String getDefaultValue() {
	return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
	this.defaultValue = defaultValue;
    }
}
