package edu.upc.gessi.rptool.rest.dtos.importexport.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.Type;

@JsonInclude(Include.NON_NULL)
public class MetricExportDTO {
    private String type;
    private GenericMetricExportDTO metric;

    public MetricExportDTO() {
    }

    public MetricExportDTO(Metric m) {
	Type metricType = m.getType();
	this.type = metricType.toString();
	if (metricType == Type.FLOAT) {
	    metric = new FloatMetricExportDTO(m);
	} else if (metricType == Type.INTEGER) {
	    metric = new IntegerMetricExportDTO(m);
	} else if (metricType == Type.STRING || metricType == Type.DOMAIN) {
	    metric = new StringMetricExportDTO(m);
	} else {
	    metric = new GenericMetricExportDTO(m);
	}
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public GenericMetricExportDTO getMetric() {
	return metric;
    }

    public void setMetric(GenericMetricExportDTO metric) {
	this.metric = metric;
    }

}