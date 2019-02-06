package edu.upc.gessi.rptool.rest.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.CostFunction;

@JsonInclude(Include.NON_NULL)
public class CostFunctionDTO {

    /*
     * ATRIBUTES
     */

    private long id;
    private String name;
    private String function;

    /*
     * CREATORS
     */
    public CostFunctionDTO(CostFunction cf) {
	this.id = cf.getId();
	this.name = cf.getName();
	this.function = cf.getFunction();
    }

    /*
     * GET'S AND SET'S METHODS
     */

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
