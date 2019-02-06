package edu.upc.gessi.rptool.rest.dtos.importexport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.Source;

@JsonInclude(Include.NON_NULL)
public class SourceExportDTO {

    private long id;
    private String identifier;
    private String reference;
    private String type;
    private String comments;

    public SourceExportDTO(Source s) {
	super();
	this.id = s.getId();
	this.identifier = s.getIdentifier();
	this.reference = (s.getReference() != null) ? s.getReference() : "";
	this.type = (s.getType() != null) ? s.getType() : "";
	this.comments = (s.getComments() != null) ? s.getComments() : "";
    }

    public SourceExportDTO(String identifier, String reference, String type, String comments) {
	super();
	this.identifier = identifier;
	this.reference = reference;
	this.type = type;
	this.comments = comments;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getIdentifier() {
	return identifier;
    }

    public void setIdentifier(String identifier) {
	this.identifier = identifier;
    }

    public String getReference() {
	return reference;
    }

    public void setReference(String reference) {
	this.reference = reference;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

}
