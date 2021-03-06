package edu.upc.gessi.rptool.rest.dtos;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.schema.ClassificationObject;

@JsonInclude(Include.NON_NULL)
public abstract class ClassificationObjectDTO {
    private long id;
    private String description;
    private String comments;
    private Set<SourceDTO> sources;

    public ClassificationObjectDTO(ClassificationObject s) {
	this.id = s.getId();
	this.description = s.getDescription();
	this.comments = s.getComments();
	this.sources = new HashSet<SourceDTO>();

	Set<Source> sources = s.getSources();
	for (Source source : sources) {
	    this.sources.add(new SourceDTO(source, true));
	}
    }

    public ClassificationObjectDTO(ClassificationObject s, Set<SourceDTO> sources) {
	this.id = s.getId();
	this.description = s.getDescription();
	this.comments = s.getComments();
	this.sources = sources;
    }

    public ClassificationObjectDTO(long id, String description, String comments, Set<SourceDTO> sources) {
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
	return description;
    }

    public String getComments() {
	return comments;
    }

    public void setSources(Set<SourceDTO> sources) {
        this.sources = sources;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public Set<SourceDTO> getSources() {
	return sources;
    }
}
