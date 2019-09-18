package edu.upc.gessi.rptool.rest.dtos.importexport.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.FloatMetric;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.Type;

@JsonInclude(Include.NON_NULL)
public class FloatMetricExportDTO extends GenericMetricExportDTO {

    // FLOAT
    @JsonProperty(value = "minValue")
    private Float minValue;
    @JsonProperty(value = "maxValue")
    private Float maxValue;
    @JsonProperty(value = "defaultValue")
    private Float defaultValueFloat;

    public FloatMetricExportDTO(Metric m) {
	super(m);
		if (m.getType() == Type.FLOAT) {
			type = Type.FLOAT;
			FloatMetric fm = (FloatMetric) m;
			this.minValue = fm.getMinValue();
			this.maxValue = fm.getMaxValue();
			if (fm.hasDefaultValue()) {
				this.defaultValueFloat = fm.getDefaultValue();
			}
		}
    }

}
