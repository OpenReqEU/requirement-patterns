package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class ExtendedPartImportUnmarshaller extends ExtendedPartUnmarshaller {

    protected Set<ParameterImportUnmarshaller> parametersAux;

    @JsonCreator
    public ExtendedPartImportUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "patternText", required = true) String patternText,
	    @JsonProperty(value = "questionText", required = true) String questionText,
	    @JsonProperty(value = "numInstances", required = true) int numInstances,
	    @JsonProperty(value = "available", required = true) boolean available,
	    @JsonProperty(value = "statsNumInstances", required = true) int statsNumInstances,
	    @JsonProperty(value = "parameters", required = false) Set<ParameterImportUnmarshaller> parameters,
	    @JsonProperty(value = "artifactsRelation", required = false) String artifacts,
	    @JsonProperty(value = "pos", required = true) short pos) throws SemanticallyIncorrectException {

	super(id, name, patternText, questionText, numInstances, available, statsNumInstances, artifacts, pos);
	this.parametersAux = parameters;
    }

    @Override
    protected void assignParameters() throws SemanticallyIncorrectException {
	if (parametersAux != null) {
	    params = new HashSet<>();
	    for (ParameterImportUnmarshaller pm : parametersAux) {
		params.add(pm.build());
	    }
	}
    }

    public boolean checkAllItemsContainsID() {
	boolean b = id != 0;

	for (ParameterImportUnmarshaller par : parametersAux) {
	    if (!par.checkAllItemsContainsID()) {
		b = false;
	    }
	}
	return b;
    }

}
