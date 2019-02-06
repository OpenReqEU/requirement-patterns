package edu.upc.gessi.rptool.rest.dtos.importexport.classifications;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.schema.ClassificationObject;

@JsonInclude(Include.NON_NULL)
public abstract class ClassificationObjectExportDTO {
    private long id;
    private String description;
    private String comments;
    @JsonProperty(value = "sourcesByIdentifier")
    private List<String> sources;

    public ClassificationObjectExportDTO(ClassificationObject s) {
	this.id = s.getId();
	this.description = s.getDescription();
	this.comments = s.getComments();
	this.sources = s.getSourcesIdentifier();
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
