package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.MetricDataController;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class ParameterImportUnmarshaller extends ParameterUnmarshaller {
    protected String metricName;
    protected Long metricIdAux;

    @JsonCreator
    public ParameterImportUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "correctnessCondition", required = false) String correctnessCondition,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "metricName", required = false) String metricName,
	    @JsonProperty(value = "metricId", required = false) Long metricId) {
	super(id, name, correctnessCondition, description);
	this.metricName = metricName;
	this.metricIdAux = metricId;

    }

    @Override
    protected Metric retreiveMetris() throws SemanticallyIncorrectException {
	Metric m = null;
	if (metricName != null && !metricName.equals("")) {
	    m = MetricDataController.getMetric(metricName);
	    if (m == null) throw new SemanticallyIncorrectException("invalid metric name in parameter");
	} else if (metricIdAux != 0) {
	    m = MetricDataController.getMetric(metricIdAux);
	    if (m == null) throw new SemanticallyIncorrectException("invalid metric id in parameter");
	} else {
	    throw new SemanticallyIncorrectException("Metric ID or Metric name should be provided");
	}

	return m;
    }

    public boolean checkAllItemsContainsID() {
	return id != 0;
    }

}
