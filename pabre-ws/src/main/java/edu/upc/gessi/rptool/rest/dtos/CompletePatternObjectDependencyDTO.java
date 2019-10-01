package edu.upc.gessi.rptool.rest.dtos;

import java.net.URI;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.gessi.rptool.domain.DependencyDirectionDomain;
import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;

public class CompletePatternObjectDependencyDTO {

    @InjectLink(value = "patternObjects/${instance.dependingObjectId}/dependencies/${instance.dependentObjectId}", style = Style.ABSOLUTE)
    private URI uri;

    private long dependingObjectId;

    private String dependingObjectClass;

    private String dependingObjectName;

    private String dependencyType;

    private DependencyDirectionDomain dependencyDirection;

    private long dependentObjectId;

    private String dependentObjectClass;

    private String dependentObjectName;

    public CompletePatternObjectDependencyDTO(RequirementPattern rp, PatternObjectCompleteDependency d) {

	this.dependingObjectId = d.getPatternObject().getId();

	this.dependingObjectClass = d.getPatternObject().getClass().getSimpleName();

	this.dependingObjectName = d.getPatternObject().getNameToShowOnDependencies();

	this.dependencyType = d.getPatternObjectDependency().getDependencyType().toString();

	this.dependencyDirection = d.getPatternObjectDependency().getDependencyDirection();

	this.dependentObjectId = d.getPatternObjectDependency().getDependency().getId();

	this.dependentObjectClass = d.getPatternObjectDependency().getDependency().getClass().getSimpleName();

	this.dependentObjectName = d.getPatternObjectDependency().getDependency().getNameToShowOnDependencies();

    }

    public URI getUri() {
	return uri;
    }

    public void setUri(URI uri) {
	this.uri = uri;
    }

    public long getDependingObjectId() {
	return dependingObjectId;
    }

    public void setDependingObjectId(long dependingObjectId) {
	this.dependingObjectId = dependingObjectId;
    }

    public String getDependingObjectClass() {
	return dependingObjectClass;
    }

    public void setDependingObjectClass(String dependingObjectClass) {
	this.dependingObjectClass = dependingObjectClass;
    }

    public String getDependencyType() {
	return dependencyType;
    }

    public void setDependencyType(String dependencyType) {
	this.dependencyType = dependencyType;
    }

    public DependencyDirectionDomain getDependencyDirection() {
	return dependencyDirection;
    }

    public void setDependencyDirection(DependencyDirectionDomain dependencyDirection) {
	this.dependencyDirection = dependencyDirection;
    }

    public long getDependentObjectId() {
	return dependentObjectId;
    }

    public void setDependentObjectId(long dependentObjectId) {
	this.dependentObjectId = dependentObjectId;
    }

    public String getDependentObjectClass() {
	return dependentObjectClass;
    }

    public void setDependentObjectClass(String dependentObjectClass) {
	this.dependentObjectClass = dependentObjectClass;
    }

    public String getDependingObjectName() {
	return dependingObjectName;
    }

    public void setDependingObjectName(String dependingObjectName) {
	this.dependingObjectName = dependingObjectName;
    }

    public String getDependentObjectName() {
	return dependentObjectName;
    }

    public void setDependentObjectName(String dependentObjectName) {
	this.dependentObjectName = dependentObjectName;
    }

}
