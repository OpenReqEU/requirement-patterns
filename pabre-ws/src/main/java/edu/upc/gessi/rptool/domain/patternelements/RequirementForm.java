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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;
import edu.upc.gessi.rptool.exceptions.IntegrityException;

@Entity
@Table(name = "REQUIREMENT_FORM")
public class RequirementForm extends PatternElement implements Comparable<RequirementForm> {

    /*
     * ATTRIBUTES
     */

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "AUTHOR", nullable = false)
    private String author;

    @Column(name = "VERSION_DATE", nullable = false)
    private Date version;

    @Column(name = "NUM_INSTANCES", nullable = false)
    private Integer numInstances;

    @Column(name = "AVAILABLE", nullable = false)
    private Boolean available;

    @Column(name = "STATS_NUM_INSTANCES", nullable = false)
    private Integer statsNumInstances;

    @Column(name = "STATS_NUM_ASSOCIATES", nullable = false)
    private Integer statsNumAssociates;

    @Column(name = "POS", nullable = false)
    private Short pos;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "FIXED_PART", unique = true, nullable = false)
    private FixedPart fixedPart;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "FORM_ID")
    private Set<ExtendedPart> extendedParts;

    /*
     * CREATORS
     */

    public RequirementForm(FixedPart fixed) {
	author = null;
	version = null;
	numInstances = 0;
	available = Boolean.FALSE;
	statsNumInstances = 0;
	statsNumAssociates = 0;
	fixedPart = fixed;
	extendedParts = new HashSet<>();
    }

    public RequirementForm() {
	author = null;
	version = null;
	numInstances = 0;
	available = Boolean.FALSE;
	statsNumInstances = 0;
	statsNumAssociates = 0;
	fixedPart = null;
	extendedParts = new HashSet<>();
	pos = null;
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

    public void setNameNoCheck(String newname) {
	this.name = newname;
    }

    public Short getPos() {
	return pos;
    }

    public void setPos(Short pos) {
	this.pos = pos;
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public Date getVersion() {
	return version;
    }

    public void setVersion(Date version) {
	this.version = version;
    }

    public Integer getNumInstances() {
	return numInstances;
    }

    public void setNumInstances(Integer n) {
	this.numInstances = n;
    }

    public boolean isUsed() {
	Integer numInstancesAux = getNumInstances();

		return numInstancesAux != null && numInstancesAux >= 1;
    }

    public Boolean getAvailable() {
	return available;
    }

    public void setAvailable(Boolean a) {
	this.available = a;
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

    public Set<ExtendedPart> getExtendedParts() {
	return extendedParts;
    }

    public void setExtendedParts(Set<ExtendedPart> s) {
	extendedParts = s;
    }

    public FixedPart getFixedPart() {
	return fixedPart;
    }

    public void setFixedPart(FixedPart f) {
	fixedPart = f;
    }

    /*
     * OTHER METHODS
     */

    /**
     * Adds the extended part received as parameter to the set of extended parts of
     * this requirement form.
     * 
     * @param extended
     *            The extended part to be added
     * @throws IntegrityException
     *             We launch an integrity exception if this requirement form already
     *             contains another extended part with the same name as the extended
     *             part received as parameter.
     */
    public void addExtendedPart(ExtendedPart extended) throws IntegrityException {
	if (testNameExtendedPart(extended.getName())) {
	    extendedParts.add(extended);
	} else {
	    throw new IntegrityException(
		    "You cannot add this extended pattern: the form already contains a extended pattern with name \""
			    + extended.getName() + "\".");
	}
    }

    public void addExtendedPartNoValidation(ExtendedPart extended) {
	extendedParts.add(extended);
    }

    /**
     * Removes the extended part received as parameter to the set of extended parts
     * of this requirement form.
     * 
     * @param extended
     *            The extended part to be removed
     */
    public void removeExtendedPart(ExtendedPart extended) {
	extendedParts.remove(extended);
    }

    /**
     * Method used to know if this Requirement Form already contains a Extended Part
     * with the name received as parameter.
     * 
     * @param n
     *            The name that we want to test
     * @return - True, if any other Extended Part of this Requirement Form have the
     *         name n - False, in another case
     */
    private boolean testNameExtendedPart(String n) {
	Iterator<ExtendedPart> iter = extendedParts.iterator();
	while (iter.hasNext()) {
	    String aux = iter.next().getName();
	    if (n.equals(aux)) return false;
	}
	return true;
    }

    /**
     * Gets the Pattern Item in this Requirement Form with the id received as
     * parameter.
     * 
     * @param id
     *            The id of the Pattern Item that we want to get.
     * @return - If this Requirement Form has a Pattern Item (the Fixed Part or one
     *         of the Extended Parts) with the id received as parameter, the method
     *         return it. - Otherwise, the method return null.
     */
    public PatternItem getPatternItemById(long id) {
	if (this.getFixedPart().getId() == id)
	    return this.getFixedPart();
	else {
	    Iterator<ExtendedPart> i = this.getExtendedParts().iterator();
	    while (i.hasNext()) {
		ExtendedPart ep = i.next();
		if (ep.getId() == id)
		    return ep;
	    }
	}
	return null;
    }

    /**
     * Method used to compare 2 requirement forms: this requirement form and the
     * requirement form received as parameter
     * 
     * @param obj
     *            Requirement Form that we want to compare
     * @return - The comparison result, ignoring case, between the names of the
     *         requirement forms
     */
    @Override
    public int compareTo(RequirementForm o) {
	if (o == null)
	    throw new NullPointerException("Comparison between null objects is not allowed");

	return this.getName().compareToIgnoreCase(o.getName());
    }

    @Override
    protected Set<PatternObjectCompleteDependency> getAllInnerDependencies() {
	Set<PatternObjectCompleteDependency> s = new HashSet<>();
	s.addAll(fixedPart.getAllDependencies());

	for (ExtendedPart extPart : extendedParts) {
	    s.addAll(extPart.getAllDependencies());
	}
	return s;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((author == null) ? 0 : author.hashCode());
	result = prime * result + ((available == null) ? 0 : available.hashCode());
	result = prime * result + ((extendedParts == null) ? 0 : extendedParts.hashCode());
	result = prime * result + ((fixedPart == null) ? 0 : fixedPart.hashCode());
	result = prime * result + ((numInstances == null) ? 0 : numInstances.hashCode());
	result = prime * result + ((pos == null) ? 0 : pos.hashCode());
	result = prime * result + ((statsNumAssociates == null) ? 0 : statsNumAssociates.hashCode());
	result = prime * result + ((statsNumInstances == null) ? 0 : statsNumInstances.hashCode());
	result = prime * result + ((version == null) ? 0 : version.hashCode());
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
	RequirementForm other = (RequirementForm) obj;
	if (author == null) {
	    if (other.author != null) return false;
	} else if (!author.equals(other.author))
	    return false;
	if (available == null) {
	    if (other.available != null) return false;
	} else if (!available.equals(other.available))
	    return false;
	if (extendedParts == null) {
	    if (other.extendedParts != null) return false;
	} else if (!extendedParts.equals(other.extendedParts))
	    return false;
	if (fixedPart == null) {
	    if (other.fixedPart != null) return false;
	} else if (!fixedPart.equals(other.fixedPart))
	    return false;
	if (numInstances == null) {
	    if (other.numInstances != null) return false;
	} else if (!numInstances.equals(other.numInstances))
	    return false;
	if (pos == null) {
	    if (other.pos != null) return false;
	} else if (!pos.equals(other.pos))
	    return false;
	if (statsNumAssociates == null) {
	    if (other.statsNumAssociates != null) return false;
	} else if (!statsNumAssociates.equals(other.statsNumAssociates))
	    return false;
	if (statsNumInstances == null) {
	    if (other.statsNumInstances != null) return false;
	} else if (!statsNumInstances.equals(other.statsNumInstances))
	    return false;
	if (version == null) {
	    if (other.version != null) return false;
	} else if (!version.equals(other.version))
	    return false;
	return true;
    }

}
