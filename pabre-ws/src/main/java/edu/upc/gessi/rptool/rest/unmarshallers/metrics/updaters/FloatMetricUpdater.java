package edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.upc.gessi.rptool.domain.metrics.FloatMetric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.PutFloatMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.utilities.Deserializer;

public class FloatMetricUpdater extends GenericMetricUpdater {

    public FloatMetricUpdater(FloatMetric m, String metricJson) {
	super(m, metricJson);
    }

    @Override
    protected void unmarshall() throws IntegrityException, ValueException, SemanticallyIncorrectException,
	    IOException {
	PutFloatMetricUnmarshaller unm;
	unm = Deserializer.deserialize(metricJson, PutFloatMetricUnmarshaller.class);
	aux = unm.build();

    }

    @Override
    protected void updateParticularMetricFields() throws ValueException {
	FloatMetric auxFM = (FloatMetric) aux;
	FloatMetric mFM = (FloatMetric) m;
	if (auxFM.getMinValue() != null && auxFM.getMaxValue() != null) {
	    if (auxFM.getDefaultValue() == null) {// van los 2
		mFM.setDefaultValueNoCheck(null);
		mFM.setHasDefault(false);
		mFM.setMinMax(auxFM.getMinValue(), auxFM.getMaxValue());
	    } else { // van los 3
		mFM.setHasDefault(false);
		mFM.setDefaultValueNoCheck(null);
		mFM.setValues(auxFM.getMinValue(), auxFM.getMaxValue(), true, auxFM.getDefaultValue());
	    }
	} else {
	    if (auxFM.getMinValue() != null) mFM.setMinValue(auxFM.getMinValue());
	    if (auxFM.getDefaultValue() != null) mFM.setDefaultValue(auxFM.getDefaultValue());
	    if (auxFM.getMaxValue() != null) mFM.setMaxValue(auxFM.getMaxValue());
	}

    }

}
