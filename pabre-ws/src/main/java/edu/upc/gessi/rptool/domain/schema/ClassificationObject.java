package edu.upc.gessi.rptool.domain.schema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.Identificable;
import edu.upc.gessi.rptool.domain.Source;

@Entity
@Table(name = "CLASSIFICATION_OBJECT")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ClassificationObject implements Identificable {

    /*
     * ATTRIBUTES
     */

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "customGenerator")
    private long id;

    @Column(name = "DESCRIPTION", nullable = false, length = 2000)
    private String description;

    @Column(name = "COMMENTS", nullable = false, length = 2000)
    private String comments;

    @ManyToMany
    @JoinTable(name = "SOURCES_AND_CLASSIFICATION_OBJECT", joinColumns = {
	    @JoinColumn(name = "CO_ID") }, inverseJoinColumns = { @JoinColumn(name = "SOURCE_ID") })
    private Set<Source> sources;

    /*
     * CREATORS
     */

    public ClassificationObject() {
	this.description = null;
	this.comments = null;
	this.sources = new HashSet<Source>();
    }

    public ClassificationObject(String description, String comments) {
	this.description = description;
	this.comments = comments;
	this.sources = new HashSet<>();
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

    public void clearAndSetSources(Set<Source> s) {
	sources.clear();
	sources.addAll(s);
    }

    public void setSources(Set<Source> s) {
	sources = s;
    }

    public List<String> getSourcesIdentifier() {
	Set<String> s = new HashSet<>();
	for (Source source : sources) {
	    s.add(source.getIdentifier());
	}
	return new ArrayList<String>(s);
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
	ClassificationObject other = (ClassificationObject) obj;
	if (id != other.id)
	    return false;
	return true;
    }

}