package edu.upc.gessi.rptool.domain.metrics;

import java.util.HashSet;
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
@Table(name = "METRIC_OBJECT")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MetricObject implements Identificable {

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
    @JoinTable(name = "SOURCES_AND_METRIC_OBJECT", joinColumns = {
	    @JoinColumn(name = "METRIC_ID") }, inverseJoinColumns = { @JoinColumn(name = "SOURCE_ID") })
    private Set<Source> sources;

    /*
     * CREATORS
     */

    public MetricObject() {
	description = null;
	comments = null;
	sources = new HashSet<Source>();
    }

    public MetricObject(String description2, String comments2) {
	this.description = description2;
	this.comments = comments2;
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