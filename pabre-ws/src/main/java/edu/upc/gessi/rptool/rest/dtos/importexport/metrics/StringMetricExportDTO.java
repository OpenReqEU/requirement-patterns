package edu.upc.gessi.rptool.rest.dtos.importexport.metrics;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.DomainMetric;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.StringMetric;
import edu.upc.gessi.rptool.domain.metrics.Type;

@JsonInclude(Include.NON_NULL)
public class StringMetricExportDTO extends GenericMetricExportDTO {

    // DOMAIN && String(defaultValue)
    @JsonProperty(value = "defaultValue")
    protected String defaultValue;
    @JsonProperty(value = "possibleValues")
    protected List<String> possibleValues;

    public StringMetricExportDTO(Metric m) {
	super(m);
	switch (m.getType()) {
	case DOMAIN:
	    type = Type.DOMAIN;
	    DomainMetric dm = (DomainMetric) m;
	    if (dm.getDefaultValue() != null) {
		this.defaultValue = dm.getDefaultValue().getValue();
	    }
	    this.possibleValues = dm.getPossibleValuesAsString();
	    break;

	case STRING:
	    type = Type.STRING;
	    StringMetric stm = (StringMetric) m;
	    this.defaultValue = stm.getDefaultValue();

	    break;
	default:
	    break;
	}
    }

}
