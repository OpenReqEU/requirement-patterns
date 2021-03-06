package edu.upc.gessi.rptool.domain.statistics;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.rest.dtos.SourceDTO;

@JsonInclude(Include.NON_NULL)
public abstract class GenericObjectDTO {
    @JsonIgnore
    private long id;
    private String name;
    private String description;
    private String comments;
    private Set<SourceDTO> sources;

    public GenericObjectDTO(GenericObject s) {
	this.id = s.getId();
	this.name = s.getName();
	this.description = s.getDescription();
	this.comments = s.getComments();
	this.sources = new HashSet<>();

	Set<Source> sourcesAux = s.getSources();
	for (Source source : sourcesAux) {
	    this.sources.add(new SourceDTO(source, true));
	}
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GenericObjectDTO(GenericObject s, Set<SourceDTO> sources) {
	this.id = s.getId();
	this.name = s.getName();
	this.description = s.getDescription();
	this.comments = s.getComments();
	this.sources = sources;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public GenericObjectDTO(long id, String name, String description, String comments, Set<SourceDTO> sources) {
	super();
	this.id = id;
	this.name = name;
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

    public Set<SourceDTO> getSources() {
	return sources;
    }

    public void setSources(Set<SourceDTO> sources) {
	this.sources = sources;
    }
}
