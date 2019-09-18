package edu.upc.gessi.rptool.domain.patternelements;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;

@Entity
@Table(name = "PATTERN_ITEM")
public abstract class PatternItem extends PatternObject {

    /*
     * ATTRIBUTES
     */
    @Column(name = "PATTERN_TEXT", nullable = false)
    private String patternText;

    @Column(name = "QUESTION_TEXT", nullable = true)
    private String questionText;

    @Column(name = "NUM_INSTANCES", nullable = false)
    private Integer numInstances;

    @Column(name = "AVAILABLE", nullable = false)
    private Boolean available;

    @Column(name = "STATS_NUM_INSTANCES", nullable = false)
    private Integer statsNumInstances;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinColumn(name = "PARAMETER_ITEM")
    private Set<Parameter> parameters;

    @Column(name = "ARTIFACTS_RELATION", nullable = true, length = 2000)
    private String artifactRelation;

    /*
     * CREATORS
     */

    public PatternItem() {
	patternText = "";
	parameters = new HashSet<>();
	questionText = "";
	numInstances = 0;
	available = Boolean.FALSE;
	statsNumInstances = 0;
	artifactRelation = null;
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public String getArtifactRelation() {
	return artifactRelation;
    }

    public void setArtifactRelation(String art) {
	artifactRelation = art;
    }

    public String getPatternText() {
	return patternText;
    }

    public void setPatternText(String patternText) {
	this.patternText = patternText;
    }

    public String getQuestionText() {
	return questionText;
    }

    public void setQuestionText(String question) {
	questionText = question;
    }

    public Integer getNumInstances() {
	return numInstances;
    }

    public void setNumInstances(Integer numInstances) {
	this.numInstances = numInstances;
    }

    public boolean isUsed() {
	Integer numInstancesAux = getNumInstances();

        return numInstancesAux != null && numInstancesAux >= 1;
    }

    public Boolean getAvailable() {
	return available;
    }

    public void setAvailable(Boolean available) {
	this.available = available;
    }

    public Integer getStatsNumInstances() {
	return statsNumInstances;
    }

    public void setStatsNumInstances(Integer statsNumInstances) {
	this.statsNumInstances = statsNumInstances;
    }

    public Set<Parameter> getParameters() {
	return parameters;
    }

    public void setParameters(Set<Parameter> params) {
	parameters = params;
    }

    /*
     * OTHER METHODS
     */

    /**
     * This method is useful to unset the question of this pattern item, setting the
     * question to null.
     */
    public void unsetQuestion() {
	questionText = null;
    }

    /**
     * This method is useful to get the parameter in this pattern item with the id
     * received as parameter.
     * 
     * @param idParameter
     *            The id of the parameter that we want to get.
     * @return - If there are a parameter with the id received as parameter in this
     *         pattern item, the method return it. - Otherwise, the method return
     *         null.
     */
    public Parameter getParameter(long idParameter) {
	Iterator<Parameter> iter = parameters.iterator();
	while (iter.hasNext()) {
	    Parameter p = iter.next();
	    if (p.getId() == idParameter) return p;
	}
	return null;
    }

    /**
     * Adds the parameter received as parameter to the set of parameters of this
     * pattern item.
     * 
     * @param param
     *            The parameter to be added
     */
    public void addParameter(Parameter param) {
	parameters.add(param);
    }

    /**
     * Removes the parameter received as parameter to the set of parameters of this
     * pattern item.
     * 
     * @param param
     *            The parameter to be removed
     */
    public void removeParameter(Parameter param) {
	parameters.remove(param);
    }

    /**
     * This method is useful to unset the set of parameters of this pattern item,
     * setting the set to a new empty hash set.
     */
    public void unsetParameters() {
	parameters = new HashSet<>();
    }

    /**
     * Method used to know if this Pattern Item already contains a Parameter with
     * the name n.
     * 
     * @param n
     *            The name that we want to test
     * @return - True, if any other Parameter of this Pattern Item have the name n -
     *         False, in another case
     */
    public boolean testNameParameter(String n) {

	Iterator<Parameter> iter = parameters.iterator();
	while (iter.hasNext()) {
	    Parameter param = iter.next();
	    if (n.compareTo(param.getName()) == 0) return false;
	}
	return true;
    }

    @Override
    public Set<PatternObjectCompleteDependency> getAllInnerDependencies() {
	HashSet<PatternObjectCompleteDependency> s = new HashSet<>();
	for (Parameter para : parameters) {
	    s.addAll(para.getAllDependencies());
	}
	return s;
    }

}