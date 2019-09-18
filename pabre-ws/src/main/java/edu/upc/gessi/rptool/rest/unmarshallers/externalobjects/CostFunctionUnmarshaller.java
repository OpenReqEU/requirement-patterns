package edu.upc.gessi.rptool.rest.unmarshallers.externalobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.CostFunction;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class CostFunctionUnmarshaller {

    private long id;
    private String name;
    private String function;

    @JsonCreator
    public CostFunctionUnmarshaller(@JsonProperty(value = "id", required = false) Long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "function", required = true) String function) {
	this.id = id == null ? 0 : id;
	this.name = name;
	this.function = function;

    }

    public CostFunction build() {

        CostFunction cf = new CostFunction();
	cf.setId(id);
	cf.setName(name);
	cf.setFunction(function);
	return cf;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getFunction() {
	return function;
    }

    public void setFunction(String function) {
	this.function = function;
    }

}
