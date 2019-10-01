package edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.SetMetric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.PutSetMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.utilities.Deserializer;

public class SetMetricUpdater extends GenericMetricUpdater {

    public SetMetricUpdater(Metric m, String metricJson) {
	super(m, metricJson);
    }

    @Override
    protected void unmarshall() throws JsonParseException, JsonMappingException, IOException, IntegrityException,
	    ValueException, SemanticallyIncorrectException {
	PutSetMetricUnmarshaller unm;
	unm = Deserializer.deserialize(metricJson, PutSetMetricUnmarshaller.class);
	aux = unm.build();

    }

    @Override
    protected void updateParticularMetricFields() throws ValueException {
	if (((SetMetric) aux).getSimple() != null) {

	    ((SetMetric) m).setSimple(((SetMetric) aux).getSimple());
	}

    }

}
