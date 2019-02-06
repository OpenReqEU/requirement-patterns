package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class RequirementFormPutUnmarshaller extends RequirementFormUnmarshaller {

    private static int toInt(Integer i) {
	return i != null ? i.intValue() : 0;
    }

    private static boolean toBool(Boolean b) {
	return b != null ? b.booleanValue() : false;
    }

    private Integer numInstances;
    private Integer statsNumInstances;
    private Integer statsNumAssociates;
    private Boolean available;

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
	    @JsonProperty(value = "available", required = false) Boolean available)
	    throws SemanticallyIncorrectException {
	super(name, description, comments, sources, author, modificationDate, toInt(numInstances),
		toInt(statsNumInstances), toInt(statsNumAssociates), 0, null, null, toBool(available));
	this.numInstances = numInstances;
	this.statsNumAssociates = statsNumAssociates;
	this.statsNumInstances = statsNumInstances;
	this.available = available;

    }

    @Override
    protected void setParts() {

    }

    @Override
    protected void setFullFields() {

    }

    @Override
    protected void setName() {
	rf.setNameNoCheck(name);
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
