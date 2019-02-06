package edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.StringMetric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.PutStringMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.utilities.Deserializer;

public class StringMetricUpdater extends GenericMetricUpdater {
    private boolean hasDef = false;

    public StringMetricUpdater(Metric m, String metricJson, boolean hasDef) {
	super(m, metricJson);
	this.hasDef = hasDef;
    }

    @Override
    protected void unmarshall() throws JsonParseException, JsonMappingException, IOException, IntegrityException,
	    ValueException, SemanticallyIncorrectException {
	PutStringMetricUnmarshaller unm;
	unm = Deserializer.deserialize(metricJson, PutStringMetricUnmarshaller.class);
	aux = unm.build();

    }

    @Override
    protected void updateParticularMetricFields() throws ValueException {

	if (((StringMetric) aux).getDefaultValue() != null) {
	    ((StringMetric) m).setDefaultValue(((StringMetric) aux).getDefaultValue());
	} else if (hasDef) {
	    ((StringMetric) m).setDefaultValueNoCheck(null);
	}

    }
}
