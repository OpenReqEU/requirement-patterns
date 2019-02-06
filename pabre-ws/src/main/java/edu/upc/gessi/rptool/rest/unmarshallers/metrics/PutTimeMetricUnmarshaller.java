package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.TimePointMetric;
import edu.upc.gessi.rptool.exceptions.IntegrityException;

public class PutTimeMetricUnmarshaller extends TimeMetricUnmarshaller {
    public PutTimeMetricUnmarshaller(@JsonProperty(value = "name", required = false) String name,
	    @JsonProperty(value = "description", required = false) String description,
	    @JsonProperty(value = "comments", required = false) String comments,
	    @JsonProperty(value = "date", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") Date date,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources) {
	super(name, description, comments, date, sources);

    }

    @Override
    public void myBuild() {
	if (date != null) {
	    ((TimePointMetric) metric).setTimeAttrib(date);
	}
    }

    @Override
    protected void setMetricName() throws IntegrityException {
	metric.setName(name);
    }
}
