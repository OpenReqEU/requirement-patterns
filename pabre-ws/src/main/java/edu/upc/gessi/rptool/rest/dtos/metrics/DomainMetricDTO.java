package edu.upc.gessi.rptool.rest.dtos.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.metrics.DomainMetric;
import edu.upc.gessi.rptool.domain.metrics.DomainMetricValue;

@JsonInclude(Include.ALWAYS)
public class DomainMetricDTO extends MetricDTO {
    private String[] possibleValues;
    private String defaultValue;

    public DomainMetricDTO(DomainMetric dm) {
	super(dm);
	this.possibleValues = new String[dm.getPossibleValues().size()];

	for (DomainMetricValue dmv : dm.getPossibleValues()) {
	    // Add values by the order
	    this.possibleValues[dmv.getOrder()] = dmv.getValue();
	}

	if (dm.getDefaultValue() != null)
	    this.defaultValue = dm.getDefaultValue().getValue();
    }

    public String[] getPossibleValues() {
	return possibleValues;
    }

    public void setPossibleValues(String[] possibleValues) {
	this.possibleValues = possibleValues;
    }

    public String getDefaultValue() {
	return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
	this.defaultValue = defaultValue;
    }
}
