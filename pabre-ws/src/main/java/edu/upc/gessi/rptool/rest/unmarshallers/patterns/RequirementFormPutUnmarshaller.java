package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class RequirementFormPutUnmarshaller extends RequirementFormUnmarshaller {

    private static int toInt(Integer i) {
	return i != null ? i : 0;
    }

    private static boolean toBool(Boolean b) {
	return b != null && b;
    }

    private Integer numInstancesAux;
    private Integer statsNumInstancesAux;
    private Integer statsNumAssociatesAux;
    private Boolean availableAux;

    @JsonCreator
    public RequirementFormPutUnmarshaller(@JsonProperty(value = "name", required = false) String name,
	    @JsonProperty(value = "description", required = false) String description,
	    @JsonProperty(value = "comments", required = false) String comments,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "author", required = false) String author,
	    @JsonProperty(value = "modificationDate", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss") Date modificationDate,
	    @JsonProperty(value = "numInstances", required = false) Integer numInstances,
	    @JsonProperty(value = "statsNumInstances", required = false) Integer statsNumInstances,
	    @JsonProperty(value = "statsNumAssociates", required = false) Integer statsNumAssociates,
	    @JsonProperty(value = "available", required = false) Boolean available) {
	super(name, description, comments, sources, author, modificationDate, toInt(numInstances),
		toInt(statsNumInstances), toInt(statsNumAssociates), 0, null, null, toBool(available));
	this.numInstancesAux = numInstances;
	this.statsNumAssociatesAux = statsNumAssociates;
	this.statsNumInstancesAux = statsNumInstances;
	this.availableAux = available;

    }

    @Override
    protected void setParts() {
        //not implemented WHY?
    }

    @Override
    protected void setFullFields() {
        //not implemented WHY?
    }

    @Override
    protected void setName() {
	rf.setNameNoCheck(name);
    }

    public boolean availableIsPresent() {
	return availableAux != null;
    }

    public boolean numInstancesIsPresent() {
	return numInstancesAux != null;
    }

    public boolean statsNumInstancesIsPresent() {
	return statsNumInstancesAux != null;

    }

    public boolean statsNumAssociatesIsPresent() {
	return statsNumAssociatesAux != null;
    }

}
