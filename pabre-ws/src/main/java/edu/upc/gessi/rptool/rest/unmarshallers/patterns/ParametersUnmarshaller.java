package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class ParametersUnmarshaller {
    private Set<ParameterUnmarshaller> spu;

    @JsonCreator
    public ParametersUnmarshaller(@JsonProperty(required = true) Set<ParameterUnmarshaller> parameters)

    {
	spu = parameters;

    }

    public Set<Parameter> build() throws SemanticallyIncorrectException {
	Set<Parameter> ret = new HashSet<>();
	if (spu != null) {
	    for (ParameterUnmarshaller aux : spu) {
		ret.add(aux.build());
	    }
	}

	return ret;
    }
}
