package edu.upc.gessi.rptool.domain.patternelements;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.RedundancyException;

@Entity
@Table(name = "REQUIREMENT_PATTERN")
public class RequirementPattern extends PatternElement implements Comparable<RequirementPattern> {

    /*
     * ATTRIBUTES
     */

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
	    CascadeType.REFRESH }, fetch = FetchType.EAGER, targetEntity = Classifier.class)
    @JoinTable(name = "CLASSIFIER_PATTERN", joinColumns = { @JoinColumn(name = "PATTERN_ID") }, inverseJoinColumns = {
	    @JoinColumn(name = "CLASSIFIER_ID") })
    private Set<Classifier> classifiers;

    @OneToMany(mappedBy = "requirementPattern", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RequirementPatternVersion> versions;

    @Column(name = "EDITABLE", nullable = false)
    public int editable;

    /*
     * CREATORS
     */

    public RequirementPattern(RequirementForm form) {
	RequirementPatternVersion v = new RequirementPatternVersion(form, this);
	classifiers = new HashSet<>();
	versions = new HashSet<>();
	versions.add(v);
    }

    public RequirementPattern() {

	classifiers = new HashSet<>();
	versions = new HashSet<>();

    }

    public RequirementPattern(Boolean noversions) {
	if (noversions) {
	    classifiers = new HashSet<>();
	    versions = new HashSet<>();
	} else {
	    new RequirementPattern();
	}
    }

    @Override
    public Set<PatternObjectCompleteDependency> getAllInnerDependencies() {
	HashSet<PatternObjectCompleteDependency> s = new HashSet<>();
	for (RequirementPatternVersion smv : versions) {
	    s.addAll(smv.getAllDependencies());
	}
	return s;
    }

    public Set<PatternObjectCompleteDependency> getAllPatternDependencies() {
	Set<PatternObjectCompleteDependency> s = new HashSet<>();
	s.addAll(this.getAllDependencies());
	return s;
    }

    /*
     * OTHER METHODS
     */

    /**
     * Returns the last version of the Pattern
     */
    public RequirementPatternVersion findLastVersion() {

	if (versions == null) {
	    return null;
	}
	Object[] versionsAux = this.versions.toArray();
	Date iniDate = ((RequirementPatternVersion) versionsAux[0]).getVersionDate();
	RequirementPatternVersion last = ((RequirementPatternVersion) versionsAux[0]);
	for (int i = 0; i < versionsAux.length; i++) {
	    if (((RequirementPatternVersion) versionsAux[i]).getVersionDate().compareTo(iniDate) > 0) {
		last = ((RequirementPatternVersion) versionsAux[i]);
		iniDate = last.getVersionDate();
	    }
	}

	return last;
    }

    /**
     * Returns the specified version
     */
    public RequirementPatternVersion findVersion(long id) {

	if (versions == null) {
	    return null;
	}
	Object[] versionsAux = this.versions.toArray();
	RequirementPatternVersion last = ((RequirementPatternVersion) versionsAux[0]);
	for (int i = 0; i < versionsAux.length; i++) {
	    if (((RequirementPatternVersion) versionsAux[i]).getId() == id) {
		last = ((RequirementPatternVersion) versionsAux[i]);
	    }
	}
	return last;
    }

    /**
     * Adds the requirement version received as parameter to the set of requirement
     * versions of this requirement pattern.
     * 
     * @param version
     *            The requirement version to be added
     */

    public void addVersion(RequirementPatternVersion version) {
	versions.add(version);
    }

    /**
     * Removes the requirement version received as parameter to the set of
     * requirement forms of this requirement pattern.
     * 
     * @param version
     *            The requirement version to be removed
     * @throws IntegrityException
     *             We launch an integrity exception if the requirement version that
     *             we want to remove is the unique requirement version in the set of
     *             the requirement versions of this pattern (the set of requirement
     *             versions has to have 1 requirement version at least).
     */

    public void removeVersion(RequirementPatternVersion version) throws IntegrityException {
	// At least 1 form
	if (versions.size() > 1)
	    versions.remove(version);
	else
	    throw new IntegrityException(
		    "The version cannot be removed: there must be at least one version for each pattern.");
    }

    /**
     * Adds the internal classifier received as parameter to the set of basic
     * classifiers of this requirement pattern.
     * 
     * @param classifierToAdd
     *            The basic classifier to be added
     * @throws RedundancyException
     *             We launch a redundancy exception if this requirement pattern
     *             already contains the internal classifier received as parameter.
     */
    public void addClassifier(Classifier classifierToAdd) throws RedundancyException {
	if (classifiers.contains(classifierToAdd))
	    throw new RedundancyException();
	classifiers.add(classifierToAdd);
    }

    /**
     * Removes the internal classifier received as parameter to the set of basic
     * classifiers of this requirement pattern.
     * 
     * @param classifierToRemove
     *            The internal classifier to be removed
     */
    public void removeClassifier(Classifier classifierToRemove) {
	classifiers.remove(classifierToRemove);
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public String getName() {
	return name;
    }

    public void setName(String newname) throws IntegrityException {
	// The name of the PatternObject can't be an empty value
	// If new name is null or it has a length equal to 0 (an empty string),
	// we launch an integrity exception
	if (newname == null || newname.trim().length() == 0 || newname.equals(""))
	    throw new IntegrityException("Name cannot be empty");
	name = newname;
    }

    /**
     * Method used to create instance with null values to update
     * 
     * @param name2
     */
    public void setNameNoCheck(String name2) {
	this.name = name2;

    }

    public int getEditable() {
	return editable;
    }

    public void setEditable(int a) {
	editable = a;
    }

    public void clearAndSetBasicClassifiers(Set<Classifier> s) {
	classifiers.clear();
	if (s != null)
	    classifiers.addAll(s);
    }

    public Set<Classifier> getClassifiers() {
	return classifiers;
    }

    public void setClassifiers(Set<Classifier> classifiers) {
	this.classifiers = classifiers;
    }

    public Set<RequirementPatternVersion> getVersions() {
	return versions;
    }

    public void clearAndSetVersions(Set<RequirementPatternVersion> k) {
	versions.clear();
	if (k != null) {
	    versions.addAll(k);
	}
    }

    public void setVersions(Set<RequirementPatternVersion> k) {
	this.versions = k;
    }

    @Override
    public String toString() {
	return "RequirementPattern [classifiers=" + classifiers + ", versions=" + versions + ", editable=" + editable
		+ "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
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
	RequirementPattern other = (RequirementPattern) obj;
	if (name == null) {
	    if (other.name != null) return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

    @Override
    public int compareTo(RequirementPattern o) {
	return this.getName().compareTo(o.getName());
    }

}