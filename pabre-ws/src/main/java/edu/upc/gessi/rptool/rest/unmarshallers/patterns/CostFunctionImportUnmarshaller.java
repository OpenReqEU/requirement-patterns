package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.CostFunction;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;

public class CostFunctionImportUnmarshaller {
    protected long id;
    protected String name;
    protected String function;

    @JsonCreator
    public CostFunctionImportUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = false) String name,
	    @JsonProperty(value = "function", required = true) String function) {
	this.id = id;
	this.name = name;
	this.function = function;
    }

    public CostFunction build() {
        return new CostFunction(id, name, function);
    }

    public CostFunction build(RequirementPatternVersion rpv) {
        return new CostFunction(id, name, function, rpv);
    }

    public boolean checkAllItemsContainsID() {
	return id != 0;
    }
}
