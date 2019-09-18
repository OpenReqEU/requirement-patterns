package edu.upc.gessi.rptool.rest.dtos.patternelements;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.Keyword;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;

@JsonInclude(Include.NON_NULL)
public class RequirementPatternVersionDTO extends RequirementPatternVersionReducedDTO {

    private String goal;

    private Integer numInstances;
    private Boolean available;
    private Integer statsNumInstances;
    private Integer statsNumAssociates;
    private String artifactRelation;

    private Set<RequirementFormDTO> forms;
    private Set<String> keywords;

    private RequirementPatternDTO requirementPattern;

    // new attributes

    public RequirementPatternVersionDTO(RequirementPatternVersion rpv) {
	super(rpv);
	this.goal = rpv.getGoal();
	this.reason = rpv.getReason();
	this.numInstances = rpv.getNumInstances();
	this.available = rpv.getAvailable();
	this.statsNumInstances = rpv.getStatsNumInstances();
	this.statsNumAssociates = rpv.getStatsNumAssociates();
	this.artifactRelation = rpv.getArtifactRelation();

	this.keywords = new HashSet<>();
	for (Keyword kw : rpv.getKeywords()) {
	    this.keywords.add(kw.getName());
	}
	this.id = rpv.getId();
	RequirementPattern rp = rpv.getRequirementPattern();

	this.forms = new TreeSet<>();
	for (RequirementForm rf : rpv.getForms()) {
	    this.forms.add(new RequirementFormDTO(rf, rp.getId(), rpv.getId()));
	}

	this.requirementPattern = new RequirementPatternDTO(rp.getId(), rp.getName(), null, null, null,
		(rp.getEditable() != 0), null, null, null, null, rp.findLastVersion().getAvailable(), null, null, null,
		null, null, null);
	this.requirementPatternId = rp.getId();

    }

    // esto es temporal, hasta eliminar completamente las dependencias antiguas

    @Override
    public URI getUri() {
	return uri;
    }

    @Override
    public void setUri(URI uri) {
	this.uri = uri;
    }

    @Override
    public String getAuthor() {
	return author;
    }

    @Override
    public void setAuthor(String author) {
	this.author = author;
    }

    public String getGoal() {
	return goal;
    }

    public void setGoal(String goal) {
	this.goal = goal;
    }

    @Override
    public String getReason() {
	return reason;
    }

    @Override
    public void setReason(String reason) {
	this.reason = reason;
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

    public void setAvailable(boolean available) {
	this.available = available;
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

    public Set<String> getKeywords() {
	return keywords;
    }

    public void setKeywords(Set<String> keywords) {
	this.keywords = keywords;
    }

    public Set<RequirementFormDTO> getForms() {
	return forms;
    }

    public void setForms(Set<RequirementFormDTO> forms) {
	this.forms = forms;
    }

    public Date getVersionDate() {
	return versionDate;
    }

    public void setVersionDate(Date versionDate) {
	this.versionDate = versionDate;
    }

    public void setAvailable(Boolean available) {
	this.available = available;
    }


    public RequirementPatternDTO getRequirementPattern() {
	return requirementPattern;
    }

    public void setRequirementPattern(RequirementPatternDTO requirementPattern) {
	this.requirementPattern = requirementPattern;
    }

    @Override
    public long getRequirementPatternId() {
	return requirementPatternId;
    }

    @Override
    public void setRequirementPatternId(long requirementPatternId) {
	this.requirementPatternId = requirementPatternId;
    }

    public int compareTo(RequirementPatternVersionReducedDTO arg0) {
        RequirementPatternVersionDTO aux = (RequirementPatternVersionDTO) arg0;
	return this.versionDate.compareTo(aux.getVersionDate());
    }

    // Added lines

    public String getArtifactsRelation() {
	return this.artifactRelation;
    }

    public void setArtifactsRelation(String art) {
	this.artifactRelation = art;
    }

}
