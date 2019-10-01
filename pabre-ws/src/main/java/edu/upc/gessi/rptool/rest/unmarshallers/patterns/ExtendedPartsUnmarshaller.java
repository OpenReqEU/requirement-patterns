package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;

public class ExtendedPartsUnmarshaller {
    private Set<ExtendedPartUnmarshaller> parts;

    @JsonCreator
    public ExtendedPartsUnmarshaller(@JsonProperty(required = true) Set<ExtendedPartUnmarshaller> parts) {
	this.parts = parts;
    }

    public Set<ExtendedPart> build() throws Exception {
	Set<ExtendedPart> ret = new HashSet<>();
	if (parts != null) {
	    for (ExtendedPartUnmarshaller aux : parts) {
		ret.add((ExtendedPart) aux.build());
	    }
	}
	return ret;
    }

    public Set<ExtendedPartUnmarshaller> getParts() {
	return parts;
    }

    public void setParts(Set<ExtendedPartUnmarshaller> parts) {
	this.parts = parts;
    }

}
