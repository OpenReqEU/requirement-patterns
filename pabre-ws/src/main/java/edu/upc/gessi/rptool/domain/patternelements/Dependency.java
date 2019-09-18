package edu.upc.gessi.rptool.domain.patternelements;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.upc.gessi.rptool.domain.DependencyTypeDomain;
import edu.upc.gessi.rptool.domain.Identificable;

@Entity
@Table(name = "DEPENDENCY", uniqueConstraints = {
	@UniqueConstraint(columnNames = { "TYPE", "DEPENDENCY_FROM", "DEPENDENCY_TO" }) })
public class Dependency implements Serializable, Comparable<Dependency>, Identificable {

    private static final long serialVersionUID = 1L;

    /*
     * ATTRIBUTES
     */

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "customGenerator")
    private long id;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private DependencyTypeDomain dependencyType;

    @ManyToOne
    @JoinColumn(name = "DEPENDENCY_FROM", nullable = false)
    private RequirementPatternVersion from;

    @ManyToOne
    @JoinColumn(name = "DEPENDENCY_TO", nullable = false)
    private RequirementPatternVersion to;

    /*
     * CREATORS
     */

    public Dependency(DependencyTypeDomain type, RequirementPatternVersion first, RequirementPatternVersion second) {
	this.dependencyType = type;
	this.from = first;
	this.to = second;
    }

    public Dependency() {
	dependencyType = null;
	from = null;
	to = null;
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public DependencyTypeDomain getDependencyType() {
	return dependencyType;
    }

    public void setDependencyType(DependencyTypeDomain dependencyType) {
	this.dependencyType = dependencyType;
    }

    public RequirementPatternVersion getFrom() {
	return from;
    }

    public void setFrom(RequirementPatternVersion from) {
	this.from = from;
    }

    public RequirementPatternVersion getTo() {
	return to;
    }

    public void setTo(RequirementPatternVersion to) {
	this.to = to;
    }

    /**
     * Method used to compare 2 dependencies: this dependency and the dependency
     * received as parameter
     * 
     * @param obj
     *            Dependency that we want to compare
     * @return - The comparison result, ignoring case, between the dependencies
     */
    @Override
    public int compareTo(Dependency o) {
	if (o == null)
	    throw new NullPointerException("Comparison between null objects is not allowed");

	int compareTypes = Integer.compare(this.getDependencyType().ordinal(), o.getDependencyType().ordinal());
	if (compareTypes != 0) {
	    return compareTypes;
	} else {
	    int compareFirst = Long.compare(this.getFrom().getId(), o.getFrom().getId());
	    if (compareFirst != 0) {
		return compareFirst;
	    } else {
            return Long.compare(this.getTo().getId(), o.getTo().getId());
	    }
	}
    }

}
