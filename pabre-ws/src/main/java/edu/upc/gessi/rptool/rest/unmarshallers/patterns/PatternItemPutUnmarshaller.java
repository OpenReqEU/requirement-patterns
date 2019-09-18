package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import edu.upc.gessi.rptool.domain.patternelements.PatternItem;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class PatternItemPutUnmarshaller {

    private PatternItem item;
    private Boolean available;
    private Integer numInstances;
    private Integer statsNumInstances;

    //
    private String patternText;
    private String questionText;
    private Set<ParameterUnmarshaller> parameters;
    private String artifacts;
    private String name;

    private boolean isFixed;

    private static int toInt(Integer i) {
	return i != null ? i : 0;
    }

    private static boolean toBool(Boolean b) {
	return b != null && b;
    }

    @JsonCreator
    public PatternItemPutUnmarshaller(@JsonProperty(value = "patternText", required = false) String patternText,
	    @JsonProperty(value = "questionText", required = false) String questionText,
	    @JsonProperty(value = "numInstances", required = false) Integer numInstances,
	    @JsonProperty(value = "available", required = false) Boolean available,
	    @JsonProperty(value = "statsNumInstances", required = false) Integer statsNumInstances,
	    @JsonProperty(value = "parameters", required = false) Set<ParameterUnmarshaller> parameters,
	    @JsonProperty(value = "artifactsRelation", required = false) String artifacts,
	    @JsonProperty(value = "name", required = false) String name) {
	this.available = available;
	this.numInstances = numInstances;
	this.statsNumInstances = statsNumInstances;
	this.patternText = patternText;
	this.questionText = questionText;
	this.parameters = parameters;
	this.artifacts = artifacts;
	this.name = name;

    }

    public PatternItem build(boolean isFixed) throws SemanticallyIncorrectException {
	this.isFixed = isFixed;
	if (!this.isFixed) {
	    item = new ExtendedPartUnmarshaller(name, patternText, questionText, toInt(numInstances), toBool(available),
		    toInt(statsNumInstances), parameters, artifacts, (short) 0).build();
	} else {
	    item = new FixedPartUnmarshaller(patternText, questionText, toInt(numInstances), toBool(available),
		    toInt(statsNumInstances), parameters, artifacts).build();
	}

	return item;
    }

    /**
     * After build call this method to check if the fields are correct
     * 
     * @throws UnrecognizedPropertyException
     */
    public void checkFields() throws UnrecognizedPropertyException {
	if (isFixed && this.name != null) throw new UnrecognizedPropertyException(null, "unrecognized fields found", null, null, "", null);
	}

    public boolean availableIsPresent() {
	return available != null;
    }

    public boolean numInstancesIsPresent() {
	return numInstances != null;
    }

    public boolean statsNumInstancesIsPresent() {
	return statsNumInstances != null;

    }

    public Boolean getAvailable() {
	return available;
    }

    public void setAvailable(Boolean available) {
	this.available = available;
    }

    public Integer getNumInstances() {
	return numInstances;
    }

    public void setNumInstances(Integer numInstances) {
	this.numInstances = numInstances;
    }

    public Integer getStatsNumInstances() {
	return statsNumInstances;
    }

    public void setStatsNumInstances(Integer statsNumInstances) {
	this.statsNumInstances = statsNumInstances;
    }

    public PatternItem getItem() {
	return item;
    }

    public void setItem(PatternItem item) {
	this.item = item;
    }

}
