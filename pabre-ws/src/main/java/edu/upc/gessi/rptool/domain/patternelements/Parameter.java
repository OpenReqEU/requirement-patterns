package edu.upc.gessi.rptool.domain.patternelements;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;
import edu.upc.gessi.rptool.domain.metrics.Metric;

@Entity
@Table(name = "PARAMETER")
public class Parameter extends PatternObject implements Comparable<Parameter> {

    /*
     * ATTRIBUTES
     */

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CORRECTNESS_CONDITION", nullable = true, length = 2000)
    private String correctnessCondition;

    @Column(name = "DESCRIPTION", nullable = false, length = 2000)
    private String description;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @JoinColumn(name = "METRIC_ID")
    public Metric metric;

    /*
     * CREATORS
     */

    public Parameter(Metric metric) {
	name = null;
	correctnessCondition = null;
	this.metric = metric;
	description = null;
    }

    public Parameter() {
	name = null;
	correctnessCondition = null;
	metric = null;
	description = null;
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public void setName(String n) {
	name = n;
    }

    public String getName() {
	return name;
    }

    public void setCorrectnessCondition(String cc) {
	correctnessCondition = cc;
    }

    public String getCorrectnessCondition() {
	return correctnessCondition;
    }

    public void setDescription(String d) {
	description = d;
    }

    public String getDescription() {
	return description;
    }

    public void setMetric(Metric m) {
	metric = m;
    }

    public Metric getMetric() {
	return metric;
    }

    /*
     * OTHER METHODS
     */

    /**
     * The method used to convert one parameter to string
     * 
     * @return - The parameter's name
     */
    @Override
    public String toString() {
	return name;
    }

    /**
     * Method used to compare 2 parameters: this parameter and the parameter
     * receives as parameter
     * 
     * @param obj
     *            Parameter that we want to compare
     * @return - The comparison result, ignoring case, between the names of the
     *         parameters
     */
    @Override
    public int compareTo(Parameter arg0) {
	if (arg0 == null)
	    throw new NullPointerException("Comparison between null objects is not allowed");
	if (!this.getName().equals(arg0.getName()))
	    return this.getName().compareToIgnoreCase(arg0.getName());
	else
	    return 1;
    }

    @Override
    public Set<PatternObjectCompleteDependency> getAllInnerDependencies() {
	return new HashSet<>();
    }

    @Override
    public String getNameToShowOnDependencies() {
	return this.name;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((correctnessCondition == null) ? 0 : correctnessCondition.hashCode());
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	result = prime * result + ((metric == null) ? 0 : metric.hashCode());
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
	Parameter other = (Parameter) obj;
	if (correctnessCondition == null) {
	    if (other.correctnessCondition != null) return false;
	} else if (!correctnessCondition.equals(other.correctnessCondition))
	    return false;
	if (description == null) {
	    if (other.description != null) return false;
	} else if (!description.equals(other.description))
	    return false;
	if (metric == null) {
	    if (other.metric != null) return false;
	} else if (!metric.equals(other.metric))
	    return false;
	if (name == null) {
	    if (other.name != null) return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

}