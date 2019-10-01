package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.exceptions.MissingCreatorPropertyException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.utilities.Deserializer;

public class GenericMetricImportCreatorUnmarshaller {

    protected long id;
    protected String type;
    protected Object metricJson;
    protected UnmarshallerGenericMetric metric;

    @JsonCreator
    public GenericMetricImportCreatorUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "type", required = true) String type,
	    @JsonProperty(value = "metric", required = true) Object metricJson) {
	this.id = id;
	this.type = type;
	this.metricJson = metricJson;
    }

    public Metric build() throws MissingCreatorPropertyException, IntegrityException, ValueException,
	    SemanticallyIncorrectException, JsonMappingException {
	try {

	    switch (type.toLowerCase()) {
	    case "float":
		metric = Deserializer.convertObject(metricJson, FloatMetricUnmarshaller.class);
		break;
	    case "integer":
		metric = Deserializer.convertObject(metricJson, IntegerMetricUnmarshaller.class);
		break;
	    case "set":
		metric = Deserializer.convertObject(metricJson, SetMetricUnmarshaller.class);
		break;
	    case "time":
		metric = Deserializer.convertObject(metricJson, TimeMetricImportUnmarshaller.class);
		break;
	    case "domain":
		metric = Deserializer.convertObject(metricJson, DomainMetricUnmarshaller.class);
		break;
	    case "string":
		metric = Deserializer.convertObject(metricJson, StringMetricUnmarshaller.class);
		break;
	    default:
		throw new MissingCreatorPropertyException("Unsupported Type");
	    }
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	    throw new JsonMappingException(null, e.getMessage());// Throw exception has Semmantically incorrect
	}
	Metric m = metric.build();
	return m;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public UnmarshallerGenericMetric getMetric() {
	return metric;
    }

    public void setMetric(UnmarshallerGenericMetric metric) {
	this.metric = metric;
    }

}
