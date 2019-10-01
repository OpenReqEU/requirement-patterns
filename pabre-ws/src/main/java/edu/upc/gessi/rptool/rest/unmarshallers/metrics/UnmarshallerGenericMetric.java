package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Set;

import javax.ws.rs.NotFoundException;

import edu.upc.gessi.rptool.data.IdToDomainObject;
import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public abstract class UnmarshallerGenericMetric {
    protected Metric metric;
    protected Set<Long> sources; // initialize in the creator
    protected Set<String> sourcesByIdentifier; // initialize in the creator
    protected long id;
    protected String name, description, comments;

    /**
     * Should initialize metric with the correspondent type
     */
    public abstract void instantiateInternalMetric();

    /**
     * Should assign common values of metric
     * 
     * @throws SemanticallyIncorrectException
     * @throws ValueException
     */
    public abstract void myBuild() throws SemanticallyIncorrectException, ValueException;

    /**
     * Assigns sources by the given sources with ID.
     * 
     * @throws SemanticallyIncorrectException
     */
    protected void assignSources() throws SemanticallyIncorrectException {
	if (sources != null) {
	    try {
		for (Source s : IdToDomainObject.getSources(sources))
		    metric.addSource(s);
	    } catch (NotFoundException e) {
		throw new SemanticallyIncorrectException("invalid source id in metric");
	    }
	}
	if (sourcesByIdentifier != null) {
	    try {
		Set<Source> ss = null;
		ss = IdToDomainObject.getSourcesByIdentifiers(sourcesByIdentifier);
		for (Source s : ss)
		    metric.addSource(s);
	    } catch (NotFoundException e) {
		throw new SemanticallyIncorrectException("invalid source identifier in metric");
	    }
	}
    }

    public Metric build() throws IntegrityException, SemanticallyIncorrectException, ValueException {
	instantiateInternalMetric();
	assignSources();
	setMetricCommonValues();
	myBuild();
	return metric;
    }

    protected void setMetricName() throws IntegrityException {
	metric.setName(name);
    }

    private void setMetricCommonValues() throws IntegrityException {
	setId();
	setMetricName();
	metric.setDescription(description);
	metric.setComments(comments);
    }

    private void setId() {
	metric.setId(id);

    }
}
