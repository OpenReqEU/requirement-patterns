package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class RequirementPatternPutUnmarshaller extends RequirementPatternUnmarshaller {

    private Boolean editable;

    @JsonCreator
    public RequirementPatternPutUnmarshaller(@JsonProperty(value = "name", required = false) String name,
	    @JsonProperty(value = "description", required = false) String description,
	    @JsonProperty(value = "comments", required = false) String comments,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "editable", required = false) Boolean editable) throws IOException {
	super(name, description, comments, sources, null, editable);
	this.editable = editable;
    }

    @Override
    protected void setVersions() throws SemanticallyIncorrectException {

    }

    @Override
    protected void setName() {
	rp.setNameNoCheck(name);
    }

    public boolean getEditableIsPresent() {
	return editable != null;
    }

}
