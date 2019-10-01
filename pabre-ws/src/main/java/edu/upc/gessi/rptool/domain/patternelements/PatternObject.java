package edu.upc.gessi.rptool.domain.patternelements;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.Identificable;
import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;
import edu.upc.gessi.rptool.domain.PatternObjectDependency;

@Entity
@Table(name = "PATTERN_OBJECT")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PatternObject implements Identificable {

    /*
     * ATTRIBUTES
     */

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "customGenerator")
    protected long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "PATTERN_OBJECT_TO_DEPENDENCY", joinColumns = {
	    @JoinColumn(name = "PATTERN_OBJECT_ID") }, inverseJoinColumns = { @JoinColumn(name = "DEPENDENCY_ID") })
    private Set<PatternObjectDependency> dependencies;

    public PatternObject() {
	dependencies = new HashSet<>();
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public void setDependencies(Set<PatternObjectDependency> s) {
	dependencies = s;
    }

    public Set<PatternObjectDependency> getDependencies() {
	return dependencies;
    }

    public PatternObjectDependency getDependency(long idDependency) {
	for (PatternObjectDependency pod : dependencies) {
	    if (pod.getDependency().getId() == idDependency) return pod;
	}
	return null;
    }

    /**
     * This method returns complete dependencies inside a patternObject
     * 
     * @return Set of all the dependencies inside of the {@link PatternObject}
     */
    protected abstract Set<PatternObjectCompleteDependency> getAllInnerDependencies();

    public String getNameToShowOnDependencies() {
	return "-";
    }

    /**
     * This method returns all the dependencies of this object, includes this object
     * dependencies and the all the inner childs.
     * 
     * @return Set of all the dependencies of this {@link PatternObject}
     */
    public Set<PatternObjectCompleteDependency> getAllDependencies() {
	Set<PatternObjectCompleteDependency> s = new HashSet<>();
	for (PatternObjectDependency patternObjectDependency : dependencies) {
	    PatternObjectCompleteDependency pocd = new PatternObjectCompleteDependency(this, patternObjectDependency);
	    s.add(pocd);
	}
	s.addAll(getAllInnerDependencies());
	return s;
    }

    public boolean deleteDependency(long idDependency) {
	PatternObjectDependency pod = getDependency(idDependency);
	boolean deleted = true;
	if (pod == null)
	    deleted = false;
	else
	    dependencies.remove(pod);

	return deleted;

    }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	PatternObject other = (PatternObject) obj;
		return id == other.id;
	}

}