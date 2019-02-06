package edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.TimePointMetric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.PutTimeMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.utilities.Deserializer;

public class TimeMetricUpdater extends GenericMetricUpdater {

    private boolean hasDate = false;

    public TimeMetricUpdater(Metric m, String metricJson, boolean hasDate) {
	super(m, metricJson);
	this.hasDate = hasDate;
    }

    @Override
    protected void unmarshall() throws JsonParseException, JsonMappingException, IOException, IntegrityException,
	    ValueException, SemanticallyIncorrectException {
	PutTimeMetricUnmarshaller unm;
	unm = Deserializer.deserialize(metricJson, PutTimeMetricUnmarshaller.class);
	aux = unm.build();

    }

    @Override
    protected void updateParticularMetricFields() throws ValueException {
	if (hasDate) {
	    TimePointMetric mTPM = (TimePointMetric) m;
	    TimePointMetric auxTPM = (TimePointMetric) aux;
	    mTPM.setYear(auxTPM.getYear());
	    mTPM.setMonth(auxTPM.getMonth());
	    mTPM.setDay(auxTPM.getDay());
	    mTPM.setHour(auxTPM.getHour());
	    mTPM.setMinute(auxTPM.getMinute());
	    mTPM.setSecond(auxTPM.getSecond());

	}

    }

}
