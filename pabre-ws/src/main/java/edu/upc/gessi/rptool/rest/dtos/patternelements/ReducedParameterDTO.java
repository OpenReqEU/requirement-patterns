package edu.upc.gessi.rptool.rest.dtos.patternelements;

import java.net.URI;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.Parameter;

@JsonInclude(Include.NON_NULL)
public class ReducedParameterDTO {
    @InjectLink(value = "parameters/${instance.id}", style = Style.ABSOLUTE)
    private URI uri;

    @JsonIgnore
    private long id;

    private String name;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public long getMetricId() {
	return metricId;
    }

    public void setMetricId(long metricId) {
	this.metricId = metricId;
    }

    private String correctnessCondition;
    private String description;

    @InjectLink(value = "metrics/${instance.metricId}", style = Style.ABSOLUTE)
    private URI uriMetric;

    @JsonIgnore
    private long metricId;

    public ReducedParameterDTO(Parameter p) {
	id = p.getId();
	metricId = p.getMetric().getId();

	name = p.getName();
	correctnessCondition = p.getCorrectnessCondition();
	description = p.getDescription();

    }

    public URI getUri() {
	return uri;
    }

    public void setUri(URI uri) {
	this.uri = uri;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCorrectnessCondition() {
	return correctnessCondition;
    }

    public void setCorrectnessCondition(String correctnessCondition) {
	this.correctnessCondition = correctnessCondition;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public URI getUriMetric() {
	return uriMetric;
    }

    public void setUriMetric(URI uriMetric) {
	this.uriMetric = uriMetric;
    }

}
