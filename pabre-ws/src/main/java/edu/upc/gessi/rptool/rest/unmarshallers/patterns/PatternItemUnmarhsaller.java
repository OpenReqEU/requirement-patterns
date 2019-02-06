package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.HashSet;
import java.util.Set;

import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.domain.patternelements.PatternItem;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public abstract class PatternItemUnmarhsaller {
    protected long id;
    protected PatternItem item;
    protected String patternText;
    protected String questionText;
    protected Integer numInstances;
    protected Boolean available;
    protected Integer statsNumInstances;
    protected Set<ParameterUnmarshaller> parameters;
    protected String artifactsRelation;
    protected HashSet<Parameter> params;

    public PatternItem build() throws SemanticallyIncorrectException {
	checkSemanticallErrors();
	instantiateInternal();
	assignParameters();
	setCommonValues();
	setDependantValues();
	setId();
	return item;
    }

    protected abstract void checkSemanticallErrors() throws SemanticallyIncorrectException;

    protected void assignParameters() throws SemanticallyIncorrectException {
	if (parameters != null) {
	    params = new HashSet<>();
	    for (ParameterUnmarshaller pm : parameters) {
		params.add(pm.build());
	    }
	}
    }

    private void setId() {
	item.setId(this.id);
    }

    private void setCommonValues() {
	item.setParameters(params);
	item.setPatternText(patternText);
	item.setQuestionText(questionText);
	item.setNumInstances(numInstances);
	item.setAvailable(available);
	item.setStatsNumInstances(statsNumInstances);
	item.setArtifactRelation(artifactsRelation);
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
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

    public void setQuestionText(String questionText) {
	this.questionText = questionText;
    }

    public Integer getNumInstances() {
	return numInstances;
    }

    public void setNumInstances(Integer numInstances) {
	this.numInstances = numInstances;
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

    public Set<ParameterUnmarshaller> getParameters() {
	return parameters;
    }

    public void setParameters(Set<ParameterUnmarshaller> parameters) {
	this.parameters = parameters;
    }

    public String getArtifactsRelation() {
	return artifactsRelation;
    }

    public void setArtifactsRelation(String artifactsRelation) {
	this.artifactsRelation = artifactsRelation;
    }

    public abstract void instantiateInternal();

    public abstract void setDependantValues();

}
