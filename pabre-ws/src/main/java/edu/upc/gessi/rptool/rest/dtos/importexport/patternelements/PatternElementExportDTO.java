package edu.upc.gessi.rptool.rest.dtos.importexport.patternelements;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.PatternElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public abstract class PatternElementExportDTO {

    protected long id;
    protected String description;
    protected String comments;
    @JsonProperty(value = "sourcesByIdentifier")
    protected List<String> sources;

    public PatternElementExportDTO(PatternElement s) {

	this.id = s.getId();
	this.description = s.getDescription();
	this.comments = s.getComments();
	this.sources = s.getSourcesIdentifier();

    }

    public PatternElementExportDTO(PatternElement s, List<String> sources) {
	this.id = s.getId();
	this.description = s.getDescription();
	this.comments = s.getComments();
	this.sources = sources;
    }

    public PatternElementExportDTO(long id, String description, String comments, List<String> sources) {
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

    public List<String> getSources() {
	return sources;
    }

    public void setSources(List<String> sources) {
	this.sources = sources;
    }
}
