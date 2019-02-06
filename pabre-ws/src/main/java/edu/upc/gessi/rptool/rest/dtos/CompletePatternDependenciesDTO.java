package edu.upc.gessi.rptool.rest.dtos;

import java.util.HashSet;
import java.util.Set;

import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.rest.dtos.patternelements.ReducedPatternObjectDTO;

public class CompletePatternDependenciesDTO extends ReducedPatternObjectDTO {

    private Set<CompletePatternObjectDependencyDTO> dependencies;

    public CompletePatternDependenciesDTO(RequirementPattern rp, Set<PatternObjectCompleteDependency> l) {
	super(rp.getId());
	dependencies = new HashSet<>();
	for (PatternObjectCompleteDependency d : l) {
	    this.dependencies.add(new CompletePatternObjectDependencyDTO(rp, d));
	}
    }

    public Set<CompletePatternObjectDependencyDTO> getDependencies() {
	return dependencies;
    }

    public void setDependencies(Set<CompletePatternObjectDependencyDTO> dependencies) {
	this.dependencies = dependencies;
    }

}
