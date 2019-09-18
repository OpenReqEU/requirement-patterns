package edu.upc.gessi.rptool.rest.unmarshallers;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.ObjectDataController;
import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;
import edu.upc.gessi.rptool.domain.PatternObjectDependency;
import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.domain.patternelements.PatternObject;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.dependencies.PatternObjectDependencyUnmarshaller;

public class DependenciesImportUnmarshaller {
    private long idDependant;
    private Set<PatternObjectDependencyUnmarshaller> dependencies;

    @JsonCreator
    public DependenciesImportUnmarshaller(@JsonProperty(value = "idDependant", required = true) long dependencyType,
	    @JsonProperty(value = "dependencies", required = true) Set<PatternObjectDependencyUnmarshaller> dependencyDirection) {
	this.idDependant = dependencyType;
	this.dependencies = dependencyDirection;
    }

    public long getIdDependant() {
	return idDependant;
    }

    public void setIdDependant(long idDependant) {
	this.idDependant = idDependant;
    }

    public Set<PatternObjectDependencyUnmarshaller> getDependencies() {
	return dependencies;
    }

    public void setDependencies(Set<PatternObjectDependencyUnmarshaller> dependencies) {
	this.dependencies = dependencies;
    }

    public Set<PatternObjectCompleteDependency> buildAndSave() throws SemanticallyIncorrectException {
	Set<PatternObjectCompleteDependency> spocd = new HashSet<>();
	Set<PatternObjectDependency> spod = new HashSet<>();
	PatternObject p = retrievePatternObject(idDependant);
	for (PatternObjectDependencyUnmarshaller aux : dependencies) {
	    PatternObjectDependency pod = aux.build();
	    spod.add(pod);
	    PatternObjectCompleteDependency pocd = new PatternObjectCompleteDependency(p, pod);
	    spocd.add(pocd);
	}
	ObjectDataController.saveNewPatternObjectDependency(p, spod);
	return spocd;
    }

    /**
     * Gets PatternObject from the DB, throws {@link NotFoundException} when the
     * PatternObject is not found
     * 
     * @param id
     *            ID of the required PatternObject
     * @return Requested {@link Parameter}
     */
    private PatternObject retrievePatternObject(long id) {
	PatternObject p = ObjectDataController.getPatternObject(id);
	if (p == null)
	    throw new NotFoundException("PatternObject [" + id + "] not found");
	return p;
    }

}
