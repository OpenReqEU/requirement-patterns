package edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.upc.gessi.rptool.domain.metrics.IntegerMetric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.PutIntegerMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.utilities.Deserializer;

public class IntegerMetricUpdater extends GenericMetricUpdater {

    public IntegerMetricUpdater(IntegerMetric m, String metricJson) {
	super(m, metricJson);
    }

    @Override
    protected void unmarshall() throws IOException, IntegrityException,
	    ValueException, SemanticallyIncorrectException {
	PutIntegerMetricUnmarshaller unm;
	unm = Deserializer.deserialize(metricJson, PutIntegerMetricUnmarshaller.class);
	aux = unm.build();

    }

    @Override
    protected void updateParticularMetricFields() throws ValueException {
	IntegerMetric mIM = (IntegerMetric) m;
	IntegerMetric auxIM = (IntegerMetric) aux;
	if (auxIM.getMinValue() != null && auxIM.getMaxValue() != null) {
	    if (((IntegerMetric) aux).getDefaultValue() == null) {
		mIM.setDefaultValueNoCheck(null);
		mIM.setHasDefault(false);
		mIM.setMinMax(auxIM.getMinValue(), auxIM.getMaxValue());
	    } else {
		mIM.setHasDefault(false);
		mIM.setDefaultValueNoCheck(null);
		mIM.setValues(auxIM.getMinValue(), auxIM.getMaxValue(), true, auxIM.getDefaultValue());
	    }
	} else {
	    if (auxIM.getMinValue() != null) mIM.setMinValue(auxIM.getMinValue());
	    if (auxIM.getDefaultValue() != null) mIM.setDefaultValue(auxIM.getDefaultValue());
	    if (auxIM.getMaxValue() != null) mIM.setMaxValue(((IntegerMetric) aux).getMaxValue());
	}

    }
}
