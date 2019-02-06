package edu.upc.gessi.rptool.rest.unmarshallers.dependencies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class PutPatternObjectDependencyUnmarshaller extends PatternObjectDependencyUnmarshaller {

    @JsonCreator
    public PutPatternObjectDependencyUnmarshaller(
	    @JsonProperty(value = "dependencyType", required = true) String dependencyType,
	    @JsonProperty(value = "dependencyDirection", required = true) String dependencyDirection)
	    throws SemanticallyIncorrectException {
	super(dependencyType, dependencyDirection, 0);
    }

    @Override
    protected void setDependency() {

    }
}
