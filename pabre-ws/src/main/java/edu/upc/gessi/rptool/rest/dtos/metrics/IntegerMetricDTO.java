package edu.upc.gessi.rptool.rest.dtos.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.metrics.IntegerMetric;

@JsonInclude(Include.ALWAYS)
public class IntegerMetricDTO extends MetricDTO {
    private Integer minValue;
    private Integer maxValue;
    private Integer defaultValue;

    public IntegerMetricDTO(IntegerMetric m) {
	super(m);
	this.minValue = m.getMinValue();
	this.maxValue = m.getMaxValue();
	if (m.hasDefaultValue()) {
	    this.defaultValue = m.getDefaultValue();
	} else {
	    this.defaultValue = null;
	}
    }

    public Integer getMinValue() {
	return minValue;
    }

    public void setMinValue(Integer minValue) {
	this.minValue = minValue;
    }

    public Integer getMaxValue() {
	return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
	this.maxValue = maxValue;
    }

    public Integer getDefaultValue() {
	return defaultValue;
    }

    public void setDefaultValue(Integer defaultValue) {
	this.defaultValue = defaultValue;
    }
}
