package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.patternelements.FixedPart;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class FixedPartUnmarshaller extends PatternItemUnmarhsaller {

    @JsonCreator
    public FixedPartUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "patternText", required = true) String patternText,
	    @JsonProperty(value = "questionText", required = true) String questionText,
	    @JsonProperty(value = "numInstances", required = true) int numInstances,
	    @JsonProperty(value = "available", required = true) boolean available,
	    @JsonProperty(value = "statsNumInstances", required = true) int statsNumInstances,
	    @JsonProperty(value = "parameters", required = false) Set<ParameterUnmarshaller> parameters,
	    @JsonProperty(value = "artifactsRelation", required = false) String artifacts)
	    throws SemanticallyIncorrectException {
	this.id = id;
	this.patternText = patternText;
	this.questionText = questionText;
	this.numInstances = numInstances;
	this.available = available;
	this.statsNumInstances = statsNumInstances;
	this.parameters = parameters;
	this.artifactsRelation = artifacts;
    }

    public FixedPartUnmarshaller(String patternText, String questionText, int numInstances, boolean available,
	    int statsNumInstances, Set<ParameterUnmarshaller> parameters, String artifacts)
	    throws SemanticallyIncorrectException {
	this.patternText = patternText;
	this.questionText = questionText;
	this.numInstances = numInstances;
	this.available = available;
	this.statsNumInstances = statsNumInstances;
	this.parameters = parameters;
	this.artifactsRelation = artifacts;
    }

    public FixedPartUnmarshaller(long id, String patternText, String questionText, int numInstances, boolean available,
	    int statsNumInstances, String artifacts) throws SemanticallyIncorrectException {
	this.id = id;
	this.patternText = patternText;
	this.questionText = questionText;
	this.numInstances = numInstances;
	this.available = available;
	this.statsNumInstances = statsNumInstances;
	this.artifactsRelation = artifacts;
    }

    public FixedPartUnmarshaller(String patternText, String questionText, int numInstances, boolean available,
	    int statsNumInstances, String artifacts) throws SemanticallyIncorrectException {

	this.patternText = patternText;
	this.questionText = questionText;
	this.numInstances = numInstances;
	this.available = available;
	this.statsNumInstances = statsNumInstances;
	this.artifactsRelation = artifacts;
    }

    @Override
    protected void checkSemanticallErrors() throws SemanticallyIncorrectException {
	if (numInstances < 0)
	    throw new SemanticallyIncorrectException("numInstances must be in  => 0 fixed part");
	if (statsNumInstances < 0)
	    throw new SemanticallyIncorrectException("statsNumInstances must be in => 0 fixed part");
    }

    @Override
    public void instantiateInternal() {
	item = new FixedPart();

    }

    @Override
    public void setDependantValues() {
	// Nothing to set extra in Fixed part
    }

}
