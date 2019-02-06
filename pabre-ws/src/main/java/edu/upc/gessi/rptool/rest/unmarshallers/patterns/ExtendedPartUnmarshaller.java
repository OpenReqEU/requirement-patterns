package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class ExtendedPartUnmarshaller extends PatternItemUnmarhsaller {

    protected short pos;
    protected String name;

    @JsonCreator
    public ExtendedPartUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "patternText", required = true) String patternText,
	    @JsonProperty(value = "questionText", required = true) String questionText,
	    @JsonProperty(value = "numInstances", required = true) int numInstances,
	    @JsonProperty(value = "available", required = true) boolean available,
	    @JsonProperty(value = "statsNumInstances", required = true) int statsNumInstances,
	    @JsonProperty(value = "parameters", required = false) Set<ParameterUnmarshaller> parameters,
	    @JsonProperty(value = "artifactsRelation", required = false) String artifacts,
	    @JsonProperty(value = "pos", required = true) short pos) throws SemanticallyIncorrectException {

	checkPos(pos);
	this.id = id;
	this.parameters = parameters;
	setBasicValues(name, patternText, questionText, numInstances, available, statsNumInstances, artifacts, pos);
    }

    public ExtendedPartUnmarshaller(String name, String patternText, String questionText, int numInstances,
	    boolean available, int statsNumInstances, Set<ParameterUnmarshaller> parameters, String artifacts,
	    short pos) throws SemanticallyIncorrectException {

	checkPos(pos);
	this.parameters = parameters;
	setBasicValues(name, patternText, questionText, numInstances, available, statsNumInstances, artifacts, pos);
    }

    public ExtendedPartUnmarshaller(long id, String name, String patternText, String questionText, int numInstances,
	    boolean available, int statsNumInstances, String artifacts, short pos)
	    throws SemanticallyIncorrectException {
	checkPos(pos);
	this.id = id;
	setBasicValues(name, patternText, questionText, numInstances, available, statsNumInstances, artifacts, pos);
    }

    public ExtendedPartUnmarshaller(String name, String patternText, String questionText, int numInstances,
	    boolean available, int statsNumInstances, String artifacts, short pos)
	    throws SemanticallyIncorrectException {

	checkPos(pos);
	setBasicValues(name, patternText, questionText, numInstances, available, statsNumInstances, artifacts, pos);
    }

    protected void setBasicValues(String name, String patternText, String questionText, int numInstances,
	    boolean available, int statsNumInstances, String artifacts, short pos) {
	this.patternText = patternText;
	this.questionText = questionText;
	this.numInstances = numInstances;
	this.available = available;
	this.statsNumInstances = statsNumInstances;
	this.artifactsRelation = artifacts;
	this.pos = pos;
	this.name = name;
    }

    protected void checkPos(short pos) throws SemanticallyIncorrectException {
	if (pos < 0)
	    throw new SemanticallyIncorrectException("pos must be => 0 in extended part");
    }

    public short getPos() {
	return pos;
    }

    @Override
    public int hashCode() {
	return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
	return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    protected void checkSemanticallErrors() throws SemanticallyIncorrectException {
	if (numInstances < 0)
	    throw new SemanticallyIncorrectException("numInstances must be => 0 in extended part");
	if (statsNumInstances < 0)
	    throw new SemanticallyIncorrectException("statsNumInstances must be => 0  in extended part");

    }

    @Override
    public void instantiateInternal() {
	item = new ExtendedPart();

    }

    @Override
    public void setDependantValues() {
	((ExtendedPart) item).setName(name);
	((ExtendedPart) item).setPos(pos);

    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setPos(short pos) {
	this.pos = pos;
    }

}
