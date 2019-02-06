package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.MetricDataController;
import edu.upc.gessi.rptool.domain.metrics.SetMetric;
import edu.upc.gessi.rptool.domain.metrics.SimpleMetric;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class SetMetricUnmarshaller extends UnmarshallerGenericMetric {

    protected Long idSimple;

    @JsonCreator
    public SetMetricUnmarshaller(@JsonProperty(value = "id", required = false) Long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "idSimple", required = true) Long idSimple,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sourcesByIdentifier) {
	this.id = id == null ? 0 : id;
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.sources = sources;
	this.sourcesByIdentifier = sourcesByIdentifier;
	this.idSimple = idSimple;

    }

    public SetMetricUnmarshaller(String name, String description, String comments, Long idSimple, Set<Long> sources,
	    Set<String> sourcesByIdentifier) {
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.sources = sources;
	this.sourcesByIdentifier = sourcesByIdentifier;
	this.idSimple = idSimple;

    }

    public SetMetricUnmarshaller(String name, String description, String comments, Set<Long> sources,
	    Set<String> sourcesByIdentifier) {
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.sources = sources;
	this.sourcesByIdentifier = sourcesByIdentifier;
    }

    public SetMetricUnmarshaller(Long id, String name, String description, String comments, Set<Long> sources,
	    Set<String> sourcesByIdentifier) {
	this.id = id == null ? 0 : id;
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.sources = sources;
	this.sourcesByIdentifier = sourcesByIdentifier;
    }

    @Override
    public void instantiateInternalMetric() {
	metric = new SetMetric();

    }

    @Override
    public void myBuild() throws SemanticallyIncorrectException {
	if (!isSimpleMetricNull()) {
	    try {
		SimpleMetric sm = searchSimpleMetric();
		if (sm == null)
		    throw new SemanticallyIncorrectException("invalid simple metric");
		((SetMetric) metric).setSimple(sm);
	    } catch (ClassCastException e) {
		throw new SemanticallyIncorrectException("invalid simple metric type");
	    }
	}

    }

    protected boolean isSimpleMetricNull() {
	return idSimple == null;
    }

    /**
     * This method search the simple metric to link
     * 
     * @return Searched simple metric
     */
    protected SimpleMetric searchSimpleMetric() {
	SimpleMetric sm = null;
	if (idSimple != null) {
	    sm = (SimpleMetric) MetricDataController.getMetric(idSimple);
	}

	return sm;
    }

}
