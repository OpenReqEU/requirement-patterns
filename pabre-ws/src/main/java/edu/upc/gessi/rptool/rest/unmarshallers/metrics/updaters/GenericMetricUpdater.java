package edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

import java.io.IOException;

public abstract class GenericMetricUpdater {
    protected Metric m;
    protected Metric aux;
    protected String metricJson;

    public GenericMetricUpdater(Metric m, String metricJson) {
	this.m = m;
	this.metricJson = metricJson;
    }

    private void updateCommonMetricFields() throws IntegrityException {
	if (aux.getName() != null)
	    m.setName(aux.getName());
	if (aux.getDescription() != null)
	    m.setDescription(aux.getDescription());
	if (aux.getComments() != null)
	    m.setComments(aux.getComments());
	if (aux.getSources() != null)
	    m.setSources(aux.getSources());
    }

    protected abstract void unmarshall() throws IOException, IntegrityException, ValueException, SemanticallyIncorrectException;

    public void update() throws Exception {
	unmarshall();
	updateCommonMetricFields();
	updateParticularMetricFields();
	save();

    }

    protected void save() {
	GenericDataController.update(m);
    }

    protected abstract void updateParticularMetricFields() throws ValueException, SemanticallyIncorrectException;
}
