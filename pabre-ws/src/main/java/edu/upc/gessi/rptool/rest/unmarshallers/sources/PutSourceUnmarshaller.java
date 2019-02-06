package edu.upc.gessi.rptool.rest.unmarshallers.sources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PutSourceUnmarshaller extends SourceUnmarshaller {

    @JsonCreator
    public PutSourceUnmarshaller(@JsonProperty(value = "identifier", required = false) String identifier,
	    @JsonProperty(value = "reference", required = false) String reference,
	    @JsonProperty(value = "type", required = false) String type,
	    @JsonProperty(value = "comments", required = false) String comments) {
	super(identifier, reference, type, comments);
    }
}
