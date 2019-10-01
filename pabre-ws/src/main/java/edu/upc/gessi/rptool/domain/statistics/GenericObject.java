package edu.upc.gessi.rptool.domain.statistics;

import java.util.HashSet;
import java.util.Set;

import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.exceptions.IntegrityException;

public abstract class GenericObject {

    /*
     * ATTRIBUTES
     */

    private long id;
    private String name;
    private String description;
    private String comments;
    private Set<Source> sources;

    /*
     * CREATORS
     */

    public GenericObject() {
	name = null;
	description = null;
	comments = null;
	sources = new HashSet<>();
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

    public String getName() {
	return name;
    }

    public void setName(String newname) throws IntegrityException {
	// The name of the GenericObject can't be an empty value
	// If new name is null or it has a length equal to 0 (an empty string),
	// we launch an integrity exception
	if (newname == null || newname.trim().length() == 0)
	    throw new IntegrityException("You have to introduce a name");
	name = newname;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public void setSources(Set<Source> s) {
        sources = s;
    }

    public Set<Source> getSources() {
	return sources;
    }

    /*
     * OTHER METHODS
     */

    /**
     * The method used to convert one GenericObject to string
     * 
     * @return The name of the Generic Object
     */
    @Override
    public String toString() {
	return name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}