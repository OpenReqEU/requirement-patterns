package edu.upc.gessi.rptool.domain.patternelements;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.CostFunction;
import edu.upc.gessi.rptool.domain.ExternalObject;
import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;
import edu.upc.gessi.rptool.exceptions.IntegrityException;

@Entity
@Table(name = "REQUIREMENT_PATTERN_VERSION")
public class RequirementPatternVersion extends PatternObject {

    /*
     * ATTRIBUTES
     */

    @Column(name = "VERSION_DATE", nullable = false)
    private Date versionDate;

    @Column(name = "AUTHOR", nullable = false)
    private String author;

    @Column(name = "GOAL", nullable = false)
    private String goal;

    @Column(name = "REASON", nullable = true)
    private String reason;

    @Column(name = "NUM_INSTANCES", nullable = false)
    private Integer numInstances;

    @Column(name = "AVAILABLE", nullable = false)
    private Boolean available;

    @Column(name = "STATS_NUM_INSTANCES", nullable = false)
    private Integer statsNumInstances;

    @Column(name = "STATS_NUM_ASSOCIATES", nullable = false)
    private Integer statsNumAssociates;

    @Column(name = "ARTIFACTS_RELATION", nullable = true, length = 2000)
    private String artifactRelation;

    /* new field used for searching */
    @Column(name = "LEMMATIZED_VERSION", nullable = false, length = 2000)
    private String lemmatizedVersion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PATTERN_ID")
    private RequirementPattern requirementPattern;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "PATTERN_VERSION_ID")
    private Set<RequirementForm> forms;

    @ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
    @JoinTable(name = "VERSION_KEYWORD", joinColumns = {
	    @JoinColumn(name = "PATTERN_VERSION_ID") }, inverseJoinColumns = { @JoinColumn(name = "KEYWORD_ID") })
    private Set<Keyword> keywords;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "PATTERN_VERSION_ID")
    private Set<Dependency> versionDependencies;

    @OneToMany(orphanRemoval = true, mappedBy = "patternVersion", cascade = { CascadeType.ALL })
    private Set<ExternalObject> externalObjects;

    /*
     * CREATORS
     */

    public RequirementPatternVersion(RequirementForm form, RequirementPattern p) {

	versionDate = new Date();
	goal = null;
	author = null;
	numInstances = new Integer(0);
	available = new Boolean(false);
	statsNumInstances = new Integer(0);
	statsNumAssociates = new Integer(0);
	setRequirementPattern(p);
	forms = new HashSet<RequirementForm>();
	forms.add(form);
	keywords = new HashSet<Keyword>();
	versionDependencies = new HashSet<Dependency>();
	externalObjects = new HashSet<>();
	artifactRelation = null;
	lemmatizedVersion = "";
    }

    public RequirementPatternVersion() {

	versionDate = new Date();
	goal = null;
	author = null;
	numInstances = new Integer(0);
	available = new Boolean(false);
	statsNumInstances = new Integer(0);
	statsNumAssociates = new Integer(0);
	forms = new HashSet<RequirementForm>();
	requirementPattern = new RequirementPattern();
	keywords = new HashSet<Keyword>();
	versionDependencies = new HashSet<Dependency>();
	externalObjects = new HashSet<>();
	artifactRelation = null;
	lemmatizedVersion = "";
    }

    /*
     * OTHER METHODS
     */

    /**
     * Adds the requirement form received as parameter to the set of requirement
     * forms of this requirement pattern.
     * 
     * @param form
     *            The requirement form to be added
     * @throws IntegrityException
     *             We launch an integrity exception if this requirement pattern
     *             already contains another requirement form with the same name as
     *             the requirement form received as parameter.
     */
    public void addForm(RequirementForm form) throws IntegrityException {
	if (testNameForm(form.getName())) {
	    forms.add(form);
	} else {
	    throw new IntegrityException(
		    "This requirement form name is already in use in this pattern. Choose another one.");
	}
    }

    public void addFormNoValidation(RequirementForm form) {
	forms.add(form);
    }

    /**
     * Removes the requirement form received as parameter to the set of requirement
     * forms of this requirement pattern.
     * 
     * @param form
     *            The requirement form to be removed
     * @throws IntegrityException
     *             We launch an integrity exception if the requirement form that we
     *             want to remove is the unique requirement form in the set of the
     *             requirement forms of this pattern (the set of requirement forms
     *             has to have 1 requirement form at least).
     */
    public void removeForm(RequirementForm form) throws IntegrityException {
	if (forms.size() > 1)
	    forms.remove(form);
	else
	    throw new IntegrityException(
		    "The form cannot be removed: there must be at least one form for each pattern.");
    }

    /**
     * Method used to know if this Requirement Pattern already contains a
     * Requirement Form with the name received as parameter.
     * 
     * @param n
     *            The name that we want to test
     * @return - True, if any other Requirement Form of this Requirement Pattern
     *         have the name n - False, in another case
     */
    public boolean testNameForm(String n) {
	Iterator<RequirementForm> i = forms.iterator();
	while (i.hasNext()) {
	    if (i.next().getName().trim().compareTo(n.trim()) == 0)
		return false;
	}
	return true;
    }

    /**
     * Gets the Requirement Form in this Requirement Pattern with the id received as
     * parameter.
     * 
     * @param id
     *            The id of the Requirement Form that we want to get.
     * @return - If this Requirement Pattern has a Requirement Form with the id id
     *         received as parameter, the method return it. - Otherwise, the method
     *         return null.
     */
    public RequirementForm getFormById(long id) {
	Iterator<RequirementForm> i = this.getForms().iterator();
	while (i.hasNext()) {
	    RequirementForm f = i.next();
	    if (f.getId() == id)
		return f;
	}
	return null;
    }

    /**
     * Adds the keyword received as parameter to the set of keywords of this
     * requirement pattern.
     * 
     * @param keyword
     *            The keyword to be added
     */
    public void addKeyword(Keyword keyword) {
	keywords.add(keyword);
    }

    /**
     * Removes the keyword received as parameter to the set of keywords of this
     * requirement pattern.
     * 
     * @param keyword
     *            The keyword to be removed
     */
    public void removeKeyword(Keyword keyword) {
	keywords.remove(keyword);
    }

    public void checkAvailability() {
	// A requirement pattern is available if at least one of its forms is
	// available
	boolean someFormAvailable = false;

	Iterator<RequirementForm> i = getForms().iterator();
	while (i.hasNext() && !someFormAvailable) {
	    if (i.next().getAvailable())
		someFormAvailable = true;
	}
	setAvailable(someFormAvailable);
    }

    /**
     * Adds the dependency received as parameter to the set of dependencies of this
     * requirement pattern.
     * 
     * @param d
     *            The dependency to be added
     * @throws IntegrityException
     *             We launch an integrity exception if this requirement pattern
     *             isn't the first pattern of the dependency (the id of this
     *             requirement pattern isn't the same id that first requirement
     *             pattern of the dependency received as parameter).
     */
    public void addDependency(Dependency d) throws IntegrityException {
	if (d.getFrom().getId() != this.getId())
	    throw new IntegrityException("Dependency Error");
	versionDependencies.add(d);
    }

    @Override
    protected Set<PatternObjectCompleteDependency> getAllInnerDependencies() {
	Set<PatternObjectCompleteDependency> s = new HashSet<>();
	for (RequirementForm requiForm : forms) {
	    s.addAll(requiForm.getAllDependencies());
	}

	return s;
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public String getLemmatizedVersion() {
	return lemmatizedVersion;
    }

    public void setLemmatizedVersion(String lemmatizedVersion) {
	this.lemmatizedVersion = lemmatizedVersion;
    }

    public String getArtifactRelation() {
	return artifactRelation;
    }

    public void setArtifactRelation(String art) {
	artifactRelation = art;
    }

    public Date getVersionDate() {
	return versionDate;
    }

    public void setVersionDate() {
	versionDate = new Date();
    }

    public void setVersionDate(Date aux) {
	versionDate = aux;
    }

    public RequirementPattern getRequirementPattern() {
	return requirementPattern;
    }

    public void setRequirementPattern(RequirementPattern req) {
	requirementPattern = req;
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String a) {
	author = a;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String a) {
	reason = a;
    }

    public String getGoal() {
	return goal;
    }

    public void setGoal(String g) {
	goal = g;
    }

    public Integer getNumInstances() {
	return numInstances;
    }

    public void setNumInstances(Integer n) {
	numInstances = n;
    }

    public boolean isUsed() {
	Integer numInstances = getNumInstances();

	if (numInstances == null || numInstances < 1)
	    return false;
	else
	    return true;
    }

    public Boolean getAvailable() {
	return available;
    }

    public void setAvailable(Boolean a) {
	available = a;
    }

    public Integer getStatsNumInstances() {
	return statsNumInstances;
    }

    public void setStatsNumInstances(Integer statsNumInstances) {
	this.statsNumInstances = statsNumInstances;
    }

    public Integer getStatsNumAssociates() {
	return statsNumAssociates;
    }

    public void setStatsNumAssociates(Integer statsNumAssociates) {
	this.statsNumAssociates = statsNumAssociates;
    }

    public Set<RequirementForm> getForms() {
	return forms;
    }

    public void setForms(Set<RequirementForm> f) {
	forms = f;
    }

    public Set<Keyword> getKeywords() {
	return keywords;
    }

    public void setKeywords(Set<Keyword> k) {
	keywords = k;
    }

    public Set<Dependency> getVersionDependencies() {
	return versionDependencies;
    }

    public void setVersionDependencies(Set<Dependency> s) {
	versionDependencies = s;
    }

    public Set<CostFunction> getCostFunction() {
	HashSet<CostFunction> hcf = new HashSet<>();
	for (ExternalObject eo : externalObjects) {
	    if (eo instanceof CostFunction) {
		hcf.add((CostFunction) eo);
	    }
	}
	return hcf;
    }

    public Set<ExternalObject> getExternalObjects() {
	return externalObjects;
    }

    public void setExternalObjects(Set<ExternalObject> externalObjects) {
	this.externalObjects = externalObjects;
    }

    public void addExternalObject(ExternalObject ext) {
	this.externalObjects.add(ext);
	ext.setPatternVersion(this);
    }

    public void addExternalObjects(Set<ExternalObject> exts) {
	this.externalObjects.addAll(exts);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	RequirementPatternVersion other = (RequirementPatternVersion) obj;
	if (artifactRelation == null) {
	    if (other.artifactRelation != null)
		return false;
	} else if (!artifactRelation.equals(other.artifactRelation))
	    return false;
	if (author == null) {
	    if (other.author != null)
		return false;
	} else if (!author.equals(other.author))
	    return false;
	if (available == null) {
	    if (other.available != null)
		return false;
	} else if (!available.equals(other.available))
	    return false;
	if (externalObjects == null) {
	    if (other.externalObjects != null)
		return false;
	} else if (!externalObjects.equals(other.externalObjects))
	    return false;
	if (forms == null) {
	    if (other.forms != null)
		return false;
	} else if (!forms.equals(other.forms))
	    return false;
	if (goal == null) {
	    if (other.goal != null)
		return false;
	} else if (!goal.equals(other.goal))
	    return false;
	if (keywords == null) {
	    if (other.keywords != null)
		return false;
	} else if (!keywords.equals(other.keywords))
	    return false;
	if (requirementPattern == null) {
	    if (other.requirementPattern != null)
		return false;
	} else if (!requirementPattern.equals(other.requirementPattern))
	    return false;
	if (numInstances == null) {
	    if (other.numInstances != null)
		return false;
	} else if (!numInstances.equals(other.numInstances))
	    return false;
	if (reason == null) {
	    if (other.reason != null)
		return false;
	} else if (!reason.equals(other.reason))
	    return false;
	if (statsNumAssociates == null) {
	    if (other.statsNumAssociates != null)
		return false;
	} else if (!statsNumAssociates.equals(other.statsNumAssociates))
	    return false;
	if (statsNumInstances == null) {
	    if (other.statsNumInstances != null)
		return false;
	} else if (!statsNumInstances.equals(other.statsNumInstances))
	    return false;
	if (versionDate == null) {
	    if (other.versionDate != null)
		return false;
	} else if (!versionDate.equals(other.versionDate))
	    return false;
	if (versionDependencies == null) {
	    if (other.versionDependencies != null)
		return false;
	} else if (!versionDependencies.equals(other.versionDependencies))
	    return false;
	return true;
    }

}