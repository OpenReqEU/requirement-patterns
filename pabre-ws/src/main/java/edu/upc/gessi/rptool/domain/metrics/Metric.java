package edu.upc.gessi.rptool.domain.metrics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.exceptions.IntegrityException;

/*
 * This abstract class represents the superclass Metric, 
 * which hierarchy all type of metric 
 */

@Entity
@Table(name = "METRIC")
public abstract class Metric extends MetricObject {

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    public abstract Type getType();

    /*
     * METHODS
     */
    public Metric() {
	name = null;
    }

    public Metric(@JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments) throws IntegrityException {
	super(description, comments);
	checkName(name);
	this.name = name;
    }

    /**
     * Method used to see if 2 metric are the same
     * 
     * @param obj
     *            Metric that we want to compare
     * @return - True, if the id of the two Metric are equal - False, in another
     *         case
     */
    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof Metric))
	    return false;

	Metric mtc = (Metric) obj;
	return mtc.getId() == this.getId();
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    private void checkName(String name) throws IntegrityException {
	if (name == null || name.trim().length() == 0)
	    throw new IntegrityException("You have to introduce a name");
    }

    /**
     * Method used to obtain the hashCode of an instance of class Metric
     */
    @Override
    public int hashCode() {
	return Long.valueOf(this.getId()).hashCode();
    }

}