package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class FixedPartImportUnmarshaller extends FixedPartUnmarshaller {

    protected Set<ParameterImportUnmarshaller> parameters;

    @JsonCreator
    public FixedPartImportUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "patternText", required = true) String patternText,
	    @JsonProperty(value = "questionText", required = true) String questionText,
	    @JsonProperty(value = "numInstances", required = true) int numInstances,
	    @JsonProperty(value = "available", required = true) boolean available,
	    @JsonProperty(value = "statsNumInstances", required = true) int statsNumInstances,
	    @JsonProperty(value = "parameters", required = false) Set<ParameterImportUnmarshaller> parameters,
	    @JsonProperty(value = "artifactsRelation", required = false) String artifacts)
	    throws SemanticallyIncorrectException {
	super(id, patternText, questionText, numInstances, available, statsNumInstances, artifacts);
	this.parameters = parameters;
    }

    @Override
    protected void assignParameters() throws SemanticallyIncorrectException {
	if (parameters != null) {
	    params = new HashSet<>();
	    for (ParameterImportUnmarshaller pm : parameters) {
		params.add(pm.build());
	    }
	}
    }

    public boolean checkAllItemsContainsID() {
	boolean b = id != 0;
	if (parameters != null) {
	    for (ParameterImportUnmarshaller par : parameters) {
		if (!par.checkAllItemsContainsID()) {
		    b = false;
		}
	    }
	}

	return b;
    }

}
