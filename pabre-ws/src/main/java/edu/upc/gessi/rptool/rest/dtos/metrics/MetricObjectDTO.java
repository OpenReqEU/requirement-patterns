package edu.upc.gessi.rptool.rest.dtos.metrics;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.metrics.MetricObject;
import edu.upc.gessi.rptool.rest.dtos.SourceDTO;

@JsonInclude(Include.NON_NULL)
public abstract class MetricObjectDTO {
    private long id;
    private String description;
    private String comments;
    private Set<SourceDTO> sources;

    public MetricObjectDTO(MetricObject s) {

	this.id = s.getId();
	this.description = s.getDescription();
	this.comments = s.getComments();
	this.sources = new HashSet<SourceDTO>();

	Set<Source> sources = s.getSources();
	for (Source source : sources) {
	    this.sources.add(new SourceDTO(source, true));
	}
    }

    public MetricObjectDTO(MetricObject s, Set<SourceDTO> sources) {
	this.id = s.getId();
	this.description = s.getDescription();
	this.comments = s.getComments();
	this.sources = sources;
    }

    public MetricObjectDTO(long id, String name, String description, String comments, Set<SourceDTO> sources) {
	super();
	this.id = id;
	this.description = description;
	this.comments = comments;
	this.sources = sources;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public Set<SourceDTO> getSources() {
	return sources;
    }

    public void setSources(Set<SourceDTO> sources) {
	this.sources = sources;
    }
}
