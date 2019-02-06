package edu.upc.gessi.rptool.rest.unmarshallers.dependencies;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.PatternObjectDependency;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class PatternObjectDependenciesUnmarshaller {

    private Set<PatternObjectDependencyUnmarshaller> dependencies;

    @JsonCreator
    public PatternObjectDependenciesUnmarshaller(
	    @JsonProperty(value = "dependencies", required = true) Set<PatternObjectDependencyUnmarshaller> dependencies2) {
	dependencies = dependencies2;
    }

    public Set<PatternObjectDependency> build() throws SemanticallyIncorrectException {
	Set<PatternObjectDependency> ret = new HashSet<>();
	if (dependencies != null) {
	    for (PatternObjectDependencyUnmarshaller aux : dependencies) {
		ret.add(aux.build());
	    }
	}
	return ret;
    }

    public Set<PatternObjectDependencyUnmarshaller> getPodu() {
	return dependencies;
    }

    public void setPodu(Set<PatternObjectDependencyUnmarshaller> podu) {
	this.dependencies = podu;
    }

}
