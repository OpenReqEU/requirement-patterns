package edu.upc.gessi.rptool.rest.dtos.importexport.patternelements;

import edu.upc.gessi.rptool.domain.CostFunction;

public class CostFunctionExportDTO {

    private String name;
    private String function;

    public CostFunctionExportDTO(CostFunction costFunction) {
	this.name = costFunction.getName();
	this.function = costFunction.getFunction();
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
