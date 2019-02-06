package edu.upc.gessi.rptool.rest.dtos;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.PatternObjectDependency;
import edu.upc.gessi.rptool.domain.patternelements.PatternObject;
import edu.upc.gessi.rptool.rest.dtos.patternelements.ReducedPatternObjectDTO;

@JsonInclude(Include.NON_NULL)
public class PatternObjectDTO extends ReducedPatternObjectDTO {

    private Set<PatternObjectDependencyDTO> dependencies;

    public Set<PatternObjectDependencyDTO> getDependencies() {
	return dependencies;
    }

    public void setDependencies(Set<PatternObjectDependencyDTO> dependencies) {
	this.dependencies = dependencies;
    }

    /*
     * CREATORS
     */
    public PatternObjectDTO(PatternObject pO) {

	super(pO.getId());
	dependencies = new HashSet<>();
	for (PatternObjectDependency d : pO.getDependencies()) {
	    this.dependencies.add(new PatternObjectDependencyDTO(d, id));
	}

    }

    /*
     * GET'S AND SET'S METHODS
     */

}
