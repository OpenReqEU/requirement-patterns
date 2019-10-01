package edu.upc.gessi.rptool.rest.dtos.importexport.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.IntegerMetric;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.Type;

@JsonInclude(Include.NON_NULL)
public class IntegerMetricExportDTO extends GenericMetricExportDTO {

    // FLOAT
    @JsonProperty(value = "minValue")
    private Integer minValue;
    @JsonProperty(value = "maxValue")
    private Integer maxValue;
    @JsonProperty(value = "defaultValue")
    private Integer defaultValueFloat;

    public IntegerMetricExportDTO(Metric m) {
	super(m);
	switch (m.getType()) {
	case INTEGER:
	    type = Type.INTEGER;
	    IntegerMetric im = (IntegerMetric) m;
	    this.minValue = im.getMinValue();
	    this.maxValue = im.getMaxValue();
	    if (im.hasDefaultValue()) {
		this.defaultValueFloat = im.getDefaultValue();
	    }

	    break;
	default:
	    break;
	}
    }

}
