package edu.upc.gessi.rptool.rest.unmarshallers.sources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class SourceUnmarshaller {
    protected long id;
    protected String identifier;
    protected String reference;
    protected String type;
    protected String comments;

    public SourceUnmarshaller(String identifier, String reference, String type, String comments) {
	super();
	this.identifier = identifier;
	this.reference = reference;
	this.type = type;
	this.comments = comments;
    }

    @JsonCreator
    public SourceUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "identifier", required = true) String identifier,
	    @JsonProperty(value = "reference", required = false) String reference,
	    @JsonProperty(value = "type", required = false) String type,
	    @JsonProperty(value = "comments", required = false) String comments) {
	this.id = id;
	this.identifier = identifier;
	this.reference = reference;
	this.type = type;
	this.comments = comments;
    }

    public Source build() throws SemanticallyIncorrectException {
	if (this.identifier != null && this.identifier.equals("")) {
	    throw new SemanticallyIncorrectException("Identifier cannot be empty");
	}
	return new Source(id, identifier, reference, type, comments);
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
