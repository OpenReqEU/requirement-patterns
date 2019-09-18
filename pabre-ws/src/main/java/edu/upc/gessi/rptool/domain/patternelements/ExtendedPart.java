package edu.upc.gessi.rptool.domain.patternelements;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * This class represents a extended part, 
 * a subclass of Pattern Item with a new 
 * attribute: name.
 */
@Entity
@Table(name = "EXTENDED_PATTERN")
public class ExtendedPart extends PatternItem implements Comparable<ExtendedPart> {

    /*
     * ATTRIBUTES
     */

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "POS", nullable = true)
    private Short pos;

    /*
     * CREATORS
     */

    public ExtendedPart() {
	super();
	name = null;
	pos = null;
    }

    /*
     * GET'S AND SET'S METHODS
     */
    public void setPos(Short pos) {
	this.pos = pos;
    }

    public Short getPos() {
	return pos;
    }

    public void setName(String s) {
	name = s;
    }

    public String getName() {
	return name;
    }

    /*
     * OTHER METHODS
     */

    /**
     * The method used to convert one extended part to string
     * 
     * @return - If name of extended part have more than 55 chars, we only return
     *         the first 55 chars with suspension points - Otherwise, we return
     *         complete name of extended part
     */
    @Override
    public String toString() {
	int numChars = 55;
	if (name.length() > numChars) {
	    return name.substring(0, numChars) + "...";
	} else {
	    return name;
	}
    }

    /**
     * Method used to compare 2 extended parts: this extended part and the extended
     * part receives as parameter
     * 
     * @param obj
     *            Extended part that we want to compare
     * @return - The comparison result, ignoring case, between the names of the
     *         extended parts
     */
    @Override
    public int compareTo(ExtendedPart o) {
	if (o == null)
	    throw new NullPointerException("Comparison between null objects is not allowed");

	return this.getName().compareToIgnoreCase(o.getName());
    }

    @Override
    public String getNameToShowOnDependencies() {
	return this.name;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((pos == null) ? 0 : pos.hashCode());
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
	ExtendedPart other = (ExtendedPart) obj;
	if (name == null) {
	    if (other.name != null) return false;
	} else if (!name.equals(other.name))
	    return false;
	if (pos == null) {
	    if (other.pos != null) return false;
	} else if (!pos.equals(other.pos))
	    return false;
	return true;
    }

}