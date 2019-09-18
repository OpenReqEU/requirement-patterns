package edu.upc.gessi.rptool.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;

@Entity
@Table(name = "EXTERNAL_OBJECT")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ExternalObject implements Identificable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "customGenerator")
    protected long id;

    @ManyToOne
    @JoinColumn(name = "PATTERN_VERSION_ID")
    protected RequirementPatternVersion patternVersion;

    public ExternalObject() {
	super();
    }

    public ExternalObject(RequirementPatternVersion patternVersion) {
	super();
	this.patternVersion = patternVersion;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public RequirementPatternVersion getPatternVersion() {
	return patternVersion;
    }

    public void setPatternVersion(RequirementPatternVersion patternVersion) {
	this.patternVersion = patternVersion;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ExternalObject other = (ExternalObject) obj;
	if (id != other.id) return false;
	return true;
    }

    @Override
    public String toString() {
	return "ExternalObject [id=" + id + ", patternVersion=" + patternVersion.getReason() + "]";
    }

}