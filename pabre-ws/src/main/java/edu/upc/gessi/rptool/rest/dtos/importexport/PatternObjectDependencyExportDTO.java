package edu.upc.gessi.rptool.rest.dtos.importexport;

import edu.upc.gessi.rptool.domain.PatternObjectDependency;

public class PatternObjectDependencyExportDTO {

    private long id;
    private String dependencyType;
    private String dependencyDirection;
    private long idDependency;

    public PatternObjectDependencyExportDTO(PatternObjectDependency d) {
	this.id = d.getId();
	this.dependencyType = d.getDependencyType().toString().toUpperCase();
	this.dependencyDirection = d.getDependencyDirection().toString().toUpperCase();
	this.idDependency = d.getDependency().getId();

    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getDependencyType() {
	return dependencyType;
    }

    public void setDependencyType(String dependencyType) {
	this.dependencyType = dependencyType;
    }

    public String getDependencyDirection() {
	return dependencyDirection;
    }

    public void setDependencyDirection(String dependencyDirection) {
	this.dependencyDirection = dependencyDirection;
    }

    public long getIdDependency() {
	return idDependency;
    }

    public void setIdDependency(long idDependency) {
	this.idDependency = idDependency;
    }

}
