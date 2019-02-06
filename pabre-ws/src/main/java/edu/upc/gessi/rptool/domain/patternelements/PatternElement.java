package edu.upc.gessi.rptool.domain.patternelements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;

import edu.upc.gessi.rptool.domain.Source;

@Entity
@Table(name = "PATTERN_ELEMENT")
public abstract class PatternElement extends PatternObject {

    /*
     * ATTRIBUTES
     */

    @Column(name = "DESCRIPTION", nullable = false, length = 2000)
    protected String description;

    @Column(name = "COMMENTS", nullable = false, length = 2000)
    protected String comments;

    @ManyToMany
    @JoinTable(name = "SOURCES_AND_PATTERN_ELEMENT", joinColumns = {
	    @JoinColumn(name = "PE_ID") }, inverseJoinColumns = { @JoinColumn(name = "SOURCE_ID") })
    private Set<Source> sources;

    /*
     * CREATORS
     */

    public PatternElement() {
	description = null;
	comments = null;
	sources = new HashSet<Source>();
    }

    @JsonCreator
    public PatternElement(String description, String comments) {
	this.description = description;
	this.comments = comments;
    }

    /*
     * GET'S AND SET'S METHODS
     */

    @Override
    public long getId() {
	return id;
    }

    @Override
    public void setId(long l) {
	id = l;
    }

    /*
     * public void setName(String newname) throws IntegrityException { // The name
     * of the PatternObject can't be an empty value // If new name is null or it has
     * a length equal to 0 (an empty string), // we launch an integrity exception if
     * (newname == null || newname.trim().length() == 0 || newname.equals("")) throw
     * new IntegrityException("Name cannot be empty"); name = newname; }
     */

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public Set<Source> getSources() {
	return sources;
    }

    public List<String> getSourcesIdentifier() {
	Set<String> s = new HashSet<>();
	for (Source source : sources) {
	    s.add(source.getIdentifier());
	}
	return new ArrayList<String>(s);
    }

    public void setSources(Set<Source> s) {
	sources = s;
    }

    /*
     * OTHER METHODS
     */

    /**
     * To add one source to the "sources" set
     * 
     * @param src
     *            Source that we want to add to the set
     */
    public void addSource(Source src) {
	sources.add(src);
    }

    /**
     * To remove one source to the "sources" set
     * 
     * @param src
     *            Source that we want to remove in the set
     */
    public void removeSource(Source src) {
	sources.remove(src);
    }

}