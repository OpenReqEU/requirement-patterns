package edu.upc.gessi.rptool.rest.dtos.metrics;

import java.net.URI;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.metrics.Metric;

@JsonInclude(Include.ALWAYS)
public class MetricDTO extends MetricObjectDTO {

    @InjectLink(value = "metrics/${instance.id}", style = Style.ABSOLUTE)
    private URI uri;
    private String name;
    private String type;

    public MetricDTO(Metric m) {
	super(m);
	this.name = m.getName();
	this.type = m.getType().toString();
    }

    public URI getUri() {
	return uri;
    }

    public void setUri(URI uri) {
	this.uri = uri;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}
