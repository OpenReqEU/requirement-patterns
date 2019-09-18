package edu.upc.gessi.rptool.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.exceptions.ValueException;

@Entity
@Table(name = "SOURCE")
public class Source implements Comparable<Source>, Identificable {

    /*
     * ATTRIBUTES
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "customGenerator")
    private long id;

    @Column(name = "IDENTIFIER", unique = true, nullable = false)
    protected String identifier;

    @Column(name = "REFERENCE", nullable = true, length = 2000)
    protected String reference;

    @Column(name = "TYPE", nullable = true, length = 2000)
    protected String type;

    @Column(name = "COMMENTS", nullable = true, length = 2000)
    protected String comments;

    /*
     * CREATORS
     */

    public Source() {
	identifier = null;
	reference = null;
	type = null;
	comments = null;
    }

    public Source(long id, String identifier, String reference, String type, String comments) {
	super();
	this.id = id;
	this.identifier = identifier;
	this.reference = reference;
	this.type = type;
	this.comments = comments;
    }

    @JsonCreator
    public Source(@JsonProperty(value = "identifier", required = true) String identifier,
	    @JsonProperty(value = "reference", required = false) String reference,
	    @JsonProperty(value = "type", required = false) String type,
	    @JsonProperty(value = "comments", required = false) String comments) {
	this.identifier = identifier;
	this.reference = reference;
	this.type = type;
	this.comments = comments;
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public long getId() {
	return id;
    }

    public void setId(long l) {
	id = l;
    }

    public String getIdentifier() {
	return identifier;
    }

    public void setIdentifier(String identifier) {
	this.identifier = identifier;
    }

    public String getReference() {
	return reference;
    }

    public void setReference(String reference) {
	this.reference = reference;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    /*
     * OTHER METHODS
     */

    /**
     * The method used to convert one source to string
     * 
     * @return - If this source has an identifier, the method return it - Another
     *         case, the string that corresponds to the object of this source
     */
    @Override
    public String toString() {
	if (getIdentifier() != null)
	    return getIdentifier();
	// In this case, "super" is class Object
	return super.toString();
    }

    /**
     * Method used to see if 2 sources are the same
     * 
     * @param obj
     *            Source that we want to compare
     * @return - True, if the id of the two sources are equal - False, in another
     *         case
     */
    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof Source))
	    return false;

	Source src = (Source) obj;
	return src.getId() == this.getId();
    }

    /**
     * Method used to obtain the hashCode of an instance of class Source
     */
    @Override
    public int hashCode() {
	return Long.valueOf(this.getId()).hashCode();
    }

    /**
     * Method used to compare 2 sources: this source and the source receives as
     * parameter
     * 
     * @param src
     *            Source that we want to compare
     * @return - The comparison result, ignoring case, between the identifiers of
     *         the sources
     */
    @Override
    public int compareTo(Source src) {
	if (src == null)
	    throw new NullPointerException("Comparison between null objects is not allowed");

	return this.getIdentifier().compareToIgnoreCase(src.getIdentifier());
    }

    /**
     * Method used to create a new Source with the same state (same value for his
     * attributes) than original.
     * 
     * @return a new Source with the same value for his attributes than original
     * @throws ValueException
     */
    public Source copy() {
	Source newSource = new Source();

	newSource.setId(this.getId());
	newSource.setIdentifier(this.getIdentifier());
	newSource.setReference(this.getReference());
	newSource.setType(this.getType());
	newSource.setComments(this.getComments());

	return newSource;
    }
}