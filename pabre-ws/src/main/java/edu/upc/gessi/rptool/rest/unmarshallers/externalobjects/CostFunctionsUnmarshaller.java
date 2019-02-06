package edu.upc.gessi.rptool.rest.unmarshallers.externalobjects;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.CostFunction;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class CostFunctionsUnmarshaller {

    private Set<CostFunctionUnmarshaller> setFunctions;

    @JsonCreator
    public CostFunctionsUnmarshaller(
	    @JsonProperty(value = "functions", required = true) Set<CostFunctionUnmarshaller> dependencies) {
	setFunctions = dependencies;

    }

    public Set<CostFunction> build() throws SemanticallyIncorrectException {
	Set<CostFunction> ret = new HashSet<>();
	if (setFunctions != null) {
	    for (CostFunctionUnmarshaller aux : setFunctions) {
		ret.add(aux.build());
	    }
	}

	return ret;
    }

    public Set<CostFunctionUnmarshaller> getSetFunctions() {
	return setFunctions;
    }

    public void setSetFunctions(Set<CostFunctionUnmarshaller> setFunctions) {
	this.setFunctions = setFunctions;
    }

}
