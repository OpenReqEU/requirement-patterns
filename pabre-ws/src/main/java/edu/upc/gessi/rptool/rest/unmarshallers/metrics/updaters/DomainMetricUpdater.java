package edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters;

import java.io.IOException;
import java.util.Set;

import edu.upc.gessi.rptool.domain.metrics.DomainMetric;
import edu.upc.gessi.rptool.domain.metrics.DomainMetricValue;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.PossibleValueException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.PutDomainMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.utilities.Deserializer;

public class DomainMetricUpdater extends GenericMetricUpdater {
    public DomainMetricUpdater(DomainMetric m, String metricJson) {
	super(m, metricJson);
    }

    @Override
    protected void unmarshall() throws IOException, SemanticallyIncorrectException, IntegrityException, ValueException {
	PutDomainMetricUnmarshaller unm;
	unm = Deserializer.deserialize(metricJson, PutDomainMetricUnmarshaller.class);
	aux = unm.build();

    }

    @Override
    protected void updateParticularMetricFields() throws SemanticallyIncorrectException {
	Set<DomainMetricValue> values = ((DomainMetric) aux).getPossibleValues();
	DomainMetricValue value = ((DomainMetric) aux).getDefaultValue();
	DomainMetric dm = (DomainMetric) m;
	if (values != null && !values.isEmpty()) {
	    dm.clearPossibleValuesAndAddPossible(values);
	}

	try {
	    if (value != null) dm.safeSetDefaultValue(value);
	} catch (PossibleValueException e1) {
	    throw new SemanticallyIncorrectException("defaultValue not in possibleValues");
	}

    }
}
