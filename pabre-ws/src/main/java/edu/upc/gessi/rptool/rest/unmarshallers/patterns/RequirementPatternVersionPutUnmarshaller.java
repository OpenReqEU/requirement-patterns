package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequirementPatternVersionPutUnmarshaller extends RequirementPatternVersionUnmarshaller {

    private Boolean available;
    private Integer numInstances;
    private Integer statsNumInstances;
    private Integer statsNumAssociates;

    private static int toInt(Integer i) {
	return i != null ? i.intValue() : 0;
    }

    private static boolean toBool(Boolean b) {
	return b != null ? b.booleanValue() : false;
    }

    @JsonCreator
    public RequirementPatternVersionPutUnmarshaller(
	    @JsonProperty(value = "versionDate", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss") Date versionDate,
	    @JsonProperty(value = "author", required = false) String author,
	    @JsonProperty(value = "goal", required = false) String goal,
	    @JsonProperty(value = "reason", required = false) String reason,
	    @JsonProperty(value = "numInstances", required = false) Integer numInstances,
	    @JsonProperty(value = "available", required = false) Boolean available,
	    @JsonProperty(value = "statsNumInstances", required = false) Integer statsNumInstances,
	    @JsonProperty(value = "statsNumAssociates", required = false) Integer statsNumAssociates,
	    @JsonProperty(value = "keywords", required = false) Set<String> keywords,
	    @JsonProperty(value = "artifactsRelation", required = false) String artifactsRelation)

    {
	super(versionDate, author, goal, reason, toInt(numInstances), toBool(available), toInt(statsNumInstances),
		toInt(statsNumAssociates), keywords, artifactsRelation, null);
	this.available = available;
	this.numInstances = numInstances;
	this.statsNumInstances = statsNumInstances;
	this.statsNumAssociates = statsNumAssociates;

    }

    @Override
    protected void buildForms() {

    }

    @Override
    protected void checkFormsSize() {
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

    public boolean statsNumAssociatesIsPresent() {
	return statsNumAssociates != null;
    }
}
