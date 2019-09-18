package edu.upc.gessi.rptool.rest.dtos;

import java.net.URI;

import org.apache.log4j.Logger;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.upc.gessi.rptool.domain.DependencyDirectionDomain;
import edu.upc.gessi.rptool.domain.PatternObjectDependency;

public class PatternObjectDependencyDTO {

    static final Logger logger = Logger.getLogger(PatternObjectDependencyDTO.class);
    private String dependencyType;
    private String dependencyClass;
    private String dependencyDirection;

    @InjectLink(value = "patternObjects/${instance.patternObjectId}/dependencies/${instance.dependencyId}", style = Style.ABSOLUTE)
    private URI uri;
    @JsonIgnore
    private long dependencyId;
    @JsonIgnore
    private long patternObjectId;
    @JsonIgnore
    private DependencyDirectionDomain dependencyDirectionModel;

    public URI getUri() {
	return uri;
    }

    public void setUri(URI uri) {
	this.uri = uri;
    }

    public PatternObjectDependencyDTO(PatternObjectDependency d, long patternObjectId) {

	dependencyType = d.getDependencyType().toString().toUpperCase();

	String dClass = d.getDependency().getClass().getSimpleName();
	setDependencyClass(dClass);
	this.patternObjectId = patternObjectId;
	this.dependencyId = d.getDependency().getId();
	this.dependencyDirection = d.getDependencyDirection().toString().toUpperCase();
	logger.debug("tipo de dependency direction " + d.getDependencyDirection());
    }

    public PatternObjectDependencyDTO() {
	dependencyDirectionModel = null;
	setDependencyType(null);
	setDependencyClass(null);
    }

    public String getDependencyType() {
	return dependencyType;
    }

    public void setDependencyType(String dT) {
	dependencyType = dT;
    }

    public String getDependencyClass() {
	return dependencyClass;
    }

    public void setDependencyClass(String dependencyClass) {
	this.dependencyClass = dependencyClass;
    }

    public String getDependencyDirection() {
	return dependencyDirection;
    }

    public void setDependencyDirection(String dependencyDirection) {
	this.dependencyDirection = dependencyDirection;
    }

    public long getDependencyId() {
	return dependencyId;
    }

    public void setDependencyId(long dependencyId) {
	this.dependencyId = dependencyId;
    }

    public long getPatternObjectId() {
	return patternObjectId;
    }

    public void setPatternObjectId(long patternObjectId) {
	this.patternObjectId = patternObjectId;
    }

}
