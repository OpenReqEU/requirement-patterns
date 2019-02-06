package edu.upc.gessi.rptool.domain.patternelements;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.Identificable;

@Entity
@Table(name = "KEYWORD")
public class Keyword implements Comparable<Keyword>, Identificable {

    /*
     * ATTRIBUTES
     */

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "customGenerator")
    private long id;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    /*
     * CREATORS
     */

    public Keyword(String keyword) {
	setName(keyword);
    }

    public Keyword(long id, String name) {
	super();
	this.id = id;
	this.name = name;
    }

    public Keyword() {
	name = null;
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public long getId() {
	return id;
    }

    public void setId(long i) {
	id = i;
    }

    public String getName() {
	return name;
    }

    public void setName(String keyword) {
	name = keyword;
    }

    /*
     * OTHER METHODS
     */

    /**
     * The method used to convert one keyword to string
     * 
     * @return - The keyword's name
     */
    @Override
    public String toString() {
	return name;
    }

    /**
     * Method used to see if 2 keywords are the same. Two keywords are equals if
     * they are the same identifier
     * 
     * @param obj
     *            Keyword that we want to compare
     * @return - True, if the id of the two keywords are equal - False, in another
     *         case
     */
    @Override
    public boolean equals(Object obj) {
	if (obj == null || !(obj instanceof Keyword))
	    return false;

	Keyword key = (Keyword) obj;
	return key.getId() == this.getId();
    }

    @Override
    public int hashCode() {
	return new Long(this.getId()).hashCode();
    }

    /**
     * Method used to compare 2 keywords: this keyword and the keyword receives as
     * parameter
     * 
     * @param obj
     *            Keyword that we want to compare
     * @return - The comparison result, ignoring case, between the identifiers of
     *         the keywords
     */
    @Override
    public int compareTo(Keyword o) {
	if (o == null)
	    throw new NullPointerException("Comparison between null objects is not allowed");

	return this.getName().compareToIgnoreCase(o.getName());
    }

}