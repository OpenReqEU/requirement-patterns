package edu.upc.gessi.rptool.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;

@Entity
@Table(name = "COST_FUNCTION")
public class CostFunction extends ExternalObject {

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "COMPLETE_FUNCTION", length = 2000, nullable = false)
    private String function;

    public CostFunction() {
	super();
    }

    public CostFunction(String name, String function) {
	super();
	this.name = name;
	this.function = function;
    }

    public CostFunction(long id, String name, String function) {
	super();
	this.id = id;
	this.name = name;
	this.function = function;
    }

    public CostFunction(String name, String function, RequirementPatternVersion patternVersion) {
	super(patternVersion);
	this.name = name;
	this.function = function;
    }

    public CostFunction(long id, String name, String function, RequirementPatternVersion patternVersion) {
	super(patternVersion);
	this.id = id;
	this.name = name;
	this.function = function;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getFunction() {
	return function;
    }

    public void setFunction(String function) {
	this.function = function;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((function == null) ? 0 : function.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	CostFunction other = (CostFunction) obj;
	if (function == null) {
	    if (other.function != null)
		return false;
	} else if (!function.equals(other.function))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "CostFunction [name=" + name + ", function=" + function + ", ID=" + getId() + "]";
    }

}