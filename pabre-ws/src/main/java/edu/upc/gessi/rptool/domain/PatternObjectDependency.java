package edu.upc.gessi.rptool.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.patternelements.PatternObject;

@Entity
@Table(name = "PATTERN_OBJECT_DEPENDENCIY")
public class PatternObjectDependency implements Identificable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "customGenerator")
    private long id;

    /**
     * This field indicates the dependency object
     */
    @ManyToOne
    @JoinColumn(name = "ID_PATTERN_OBJECT")
    private PatternObject dependency;

    @Column(name = "TYPE")
    @Enumerated(value = EnumType.ORDINAL)
    private DependencyTypeDomain dependencyType;

    @Column(name = "DIRECTION")
    @Enumerated(value = EnumType.ORDINAL)
    private DependencyDirectionDomain dependencyDirection;

    public PatternObjectDependency() {
    }

    public PatternObjectDependency(PatternObject d, DependencyTypeDomain dT, DependencyDirectionDomain dD) {
	dependency = d;
	dependencyType = dT;
	dependencyDirection = dD;
    }

    public PatternObject getDependency() {
	return dependency;
    }

    public void setDependency(PatternObject d) {
	dependency = d;
    }

    public DependencyTypeDomain getDependencyType() {
	return dependencyType;
    }

    public void setDependencyType(DependencyTypeDomain dT) {
	dependencyType = dT;
    }

    public DependencyDirectionDomain getDependencyDirection() {
	return dependencyDirection;
    }

    public void setDependencyDirection(DependencyDirectionDomain dD) {
	dependencyDirection = dD;
    }

    public long getId() {
	return id;
    }

    public void setId(long i) {
	id = i;
    }
}
