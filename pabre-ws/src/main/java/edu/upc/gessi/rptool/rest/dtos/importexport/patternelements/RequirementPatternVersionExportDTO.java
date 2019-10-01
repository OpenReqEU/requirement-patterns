package edu.upc.gessi.rptool.rest.dtos.importexport.patternelements;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.CostFunction;
import edu.upc.gessi.rptool.domain.patternelements.Keyword;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;

@JsonInclude(Include.NON_NULL)
public class RequirementPatternVersionExportDTO implements Comparable<RequirementPatternVersionExportDTO> {

    protected long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
    protected Date versionDate;
    protected String author;
    protected String goal;
    protected String reason;
    protected Integer numInstances;
    protected Boolean available;
    protected Integer statsNumInstances;
    protected Integer statsNumAssociates;
    protected String artifactsRelation;
    protected Set<String> keywords;
    protected Set<RequirementFormExportDTO> forms;
    protected Set<CostFunctionExportDTO> costFunctions;

    public RequirementPatternVersionExportDTO(RequirementPatternVersion rpv) {
	this.id = rpv.getId();
	this.versionDate = rpv.getVersionDate();
	this.author = rpv.getAuthor();
	this.goal = rpv.getGoal();
	this.reason = rpv.getReason();
	this.numInstances = rpv.getNumInstances();
	this.available = rpv.getAvailable();
	this.statsNumInstances = rpv.getStatsNumInstances();
	this.statsNumAssociates = rpv.getStatsNumAssociates();
	this.artifactsRelation = rpv.getArtifactRelation();

	this.keywords = new HashSet<String>();
	for (Keyword kw : rpv.getKeywords()) {
	    this.keywords.add(kw.getName());
	}

	this.costFunctions = new HashSet<CostFunctionExportDTO>();
	for (CostFunction costFunction : rpv.getCostFunction()) {
	    this.costFunctions.add(new CostFunctionExportDTO(costFunction));
	}

	RequirementPattern rp = rpv.getRequirementPattern();
	this.forms = new TreeSet<RequirementFormExportDTO>();
	for (RequirementForm rf : rpv.getForms()) {
	    this.forms.add(new RequirementFormExportDTO(rf, rp.getId(), rpv.getId()));
	}

    }

    public int compareTo(RequirementPatternVersionExportDTO arg0) {
	return this.versionDate.compareTo(arg0.getVersionDate());
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
	return id;
    }

    public Date getVersionDate() {
	return versionDate;
    }

    public void setVersionDate(Date versionDate) {
	this.versionDate = versionDate;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
	return author;
    }

    public String getGoal() {
	return goal;
    }

    public void setGoal(String goal) {
	this.goal = goal;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
	return reason;
    }

    public void setNumInstances(Integer numInstances) {
        this.numInstances = numInstances;
    }

    public Integer getNumInstances() {
	return numInstances;
    }

    public Boolean getAvailable() {
	return available;
    }

    public void setAvailable(Boolean available) {
	this.available = available;
    }

    public void setStatsNumInstances(Integer statsNumInstances) {
        this.statsNumInstances = statsNumInstances;
    }

    public Integer getStatsNumInstances() {
	return statsNumInstances;
    }

    public Integer getStatsNumAssociates() {
	return statsNumAssociates;
    }

    public void setStatsNumAssociates(Integer statsNumAssociates) {
	this.statsNumAssociates = statsNumAssociates;
    }

    public void setCostFunctions(Set<CostFunctionExportDTO> costFunctions) {
        this.costFunctions = costFunctions;
    }

    public Set<CostFunctionExportDTO> getCostFunctions() {
	return costFunctions;
    }

    public String getArtifactsRelation() {
	return artifactsRelation;
    }

    public void setArtifactsRelation(String artifactsRelation) {
	this.artifactsRelation = artifactsRelation;
    }

    public Set<String> getKeywords() {
	return keywords;
    }

    public void setKeywords(Set<String> keywords) {
	this.keywords = keywords;
    }

    public Set<RequirementFormExportDTO> getForms() {
	return forms;
    }

    public void setForms(Set<RequirementFormExportDTO> forms) {
	this.forms = forms;
    }

}
