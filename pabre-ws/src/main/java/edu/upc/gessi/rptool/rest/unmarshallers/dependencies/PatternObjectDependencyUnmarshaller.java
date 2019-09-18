package edu.upc.gessi.rptool.rest.unmarshallers.dependencies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.ObjectDataController;
import edu.upc.gessi.rptool.domain.DependencyDirectionDomain;
import edu.upc.gessi.rptool.domain.DependencyTypeDomain;
import edu.upc.gessi.rptool.domain.PatternObjectDependency;
import edu.upc.gessi.rptool.domain.patternelements.PatternObject;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class PatternObjectDependencyUnmarshaller {

    private long id;
    private String stringType;
    private String stringDirection;
    protected DependencyTypeDomain depType;
    protected DependencyDirectionDomain depDirection;
    private long idDependency;
    private PatternObjectDependency pod;

    @JsonCreator
    public PatternObjectDependencyUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "dependencyType", required = true) String dependencyType,
	    @JsonProperty(value = "dependencyDirection", required = true) String dependencyDirection,
	    @JsonProperty(value = "idDependency", required = true) long idDependendency) {
	this.id = id;
	this.idDependency = idDependendency;
	this.stringType = dependencyType.toLowerCase();
	this.stringDirection = dependencyDirection.toLowerCase();
    }

    public PatternObjectDependencyUnmarshaller(String dependencyType, String dependencyDirection, long idDependendency){
	this.idDependency = idDependendency;
	this.stringType = dependencyType.toLowerCase();
	this.stringDirection = dependencyDirection.toLowerCase();

    }

    protected void setEnums() throws SemanticallyIncorrectException {
	// lanza una excepcion si el numero esta fuera del rango de la enumeracion

	depType = DependencyTypeDomain.fromString(stringType);
	if (depType == null) {
	    throw new SemanticallyIncorrectException("invalid dependecyType value");
	}

	depDirection = DependencyDirectionDomain.fromString(stringDirection);

	if (depDirection == null) {
	    throw new SemanticallyIncorrectException("invalid dependencyDirection value");
	}

    }

    protected void setDependency() throws SemanticallyIncorrectException {
	PatternObject po = null;
	po = ObjectDataController.getPatternObject(idDependency);
	if (po == null)
	    throw new SemanticallyIncorrectException("invalid idDependency");
	pod.setDependency(po);
    }

    public PatternObjectDependency build() throws SemanticallyIncorrectException {

	setEnums();
	pod = new PatternObjectDependency();
	pod.setDependencyType(depType);
	pod.setDependencyDirection(depDirection);
	setDependency();
	pod.setId(id);
	return pod;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public long getIdDependency() {
	return idDependency;
    }

    public void setIdDependency(long idDependency) {
	this.idDependency = idDependency;
    }

    public String getStringType() {
	return stringType;
    }

    public void setStringType(String stringType) {
	this.stringType = stringType;
    }

    public String getStringDirection() {
	return stringDirection;
    }

    public void setStringDirection(String stringDirection) {
	this.stringDirection = stringDirection;
    }

}
