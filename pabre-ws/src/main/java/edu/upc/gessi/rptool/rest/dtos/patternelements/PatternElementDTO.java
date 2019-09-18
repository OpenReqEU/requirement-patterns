package edu.upc.gessi.rptool.rest.dtos.patternelements;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.patternelements.PatternElement;
import edu.upc.gessi.rptool.rest.dtos.SourceDTO;

@JsonInclude(Include.NON_NULL)
public abstract class PatternElementDTO extends ReducedPatternObjectDTO {

    private String description;
    private String comments;
    private Set<SourceDTO> sources;

    public PatternElementDTO(PatternElement s) {

	super(s.getId());
	this.description = s.getDescription();
	this.comments = s.getComments();
	this.sources = new HashSet<>();

	Set<Source> sourcesAux = s.getSources();
	for (Source source : sourcesAux) {
	    this.sources.add(new SourceDTO(source, true));
	}
    }

    public PatternElementDTO(PatternElement s, Set<SourceDTO> sources) {
	super(s.getId());
	this.description = s.getDescription();
	this.comments = s.getComments();
	this.sources = sources;
    }

    public PatternElementDTO(long id, String description, String comments, Set<SourceDTO> sources) {

	super(id);
	this.description = description;
	this.comments = comments;
	this.sources = sources;
    }

    @Override
    public long getId() {
	return id;
    }

    @Override
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
