package edu.upc.gessi.rptool.rest.dtos.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.metrics.FloatMetric;

@JsonInclude(Include.ALWAYS)
public class FloatMetricDTO extends MetricDTO {
    private Float minValue;
    private Float maxValue;
    private Float defaultValue;

    public FloatMetricDTO(FloatMetric m) {
	super(m);
	this.minValue = m.getMinValue();
	this.maxValue = m.getMaxValue();
	if (m.hasDefaultValue()) {
	    this.defaultValue = m.getDefaultValue();
	} else {
	    this.defaultValue = null;
	}

    }

    public Float getMinValue() {
	return minValue;
    }

    public void setMinValue(Float minValue) {
	this.minValue = minValue;
    }

    public Float getMaxValue() {
	return maxValue;
    }

    public void setMaxValue(Float maxValue) {
	this.maxValue = maxValue;
    }

    public Float getDefaultValue() {
	return defaultValue;
    }

    public void setDefaultValue(Float defaultValue) {
	this.defaultValue = defaultValue;
    }
}
