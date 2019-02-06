package edu.upc.gessi.rptool.rest.dtos.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.metrics.DomainMetric;
import edu.upc.gessi.rptool.domain.metrics.FloatMetric;
import edu.upc.gessi.rptool.domain.metrics.IntegerMetric;
import edu.upc.gessi.rptool.domain.metrics.SetMetric;
import edu.upc.gessi.rptool.domain.metrics.StringMetric;
import edu.upc.gessi.rptool.domain.metrics.TimePointMetric;
import edu.upc.gessi.rptool.domain.metrics.Type;

@JsonInclude(Include.ALWAYS)
public class SetMetricDTO extends MetricDTO {

    private MetricDTO simple;

    public SetMetricDTO(SetMetric m) {
	super(m);
	if (m.getSimple().getType() == Type.DOMAIN) {
	    this.simple = new DomainMetricDTO((DomainMetric) m.getSimple());
	} else if (m.getSimple().getType() == Type.FLOAT) {
	    this.simple = new FloatMetricDTO((FloatMetric) m.getSimple());
	} else if (m.getSimple().getType() == Type.INTEGER) {
	    this.simple = new IntegerMetricDTO((IntegerMetric) m.getSimple());
	} else if (m.getSimple().getType() == Type.STRING) {
	    this.simple = new StringMetricDTO((StringMetric) m.getSimple());
	} else if (m.getSimple().getType() == Type.TIME) {
	    this.simple = new TimePointMetricDTO((TimePointMetric) m.getSimple());
	} else {
	    this.simple = new MetricDTO(m);
	}
    }

    public MetricDTO getSimple() {
	return simple;
    }

    public void setSimple(MetricDTO simple) {
	this.simple = simple;
    }
}
