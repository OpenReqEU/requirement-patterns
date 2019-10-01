package edu.upc.gessi.rptool.rest.dtos.patternelements;

import java.net.URI;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.CostFunction;
import edu.upc.gessi.rptool.domain.patternelements.Keyword;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.rest.dtos.CostFunctionDTO;
import edu.upc.gessi.rptool.rest.dtos.SourceDTO;

@JsonInclude(Include.NON_NULL)
public class RequirementPatternDTO extends PatternElementDTO
	implements Comparator<RequirementPatternDTO>, Comparable<RequirementPatternDTO> {

    private String name;
    private String author;
    private Boolean available;
    private Boolean editable;
    private Set<RequirementFormDTO> forms;
    private Set<CostFunctionDTO> costFunction;
    private String goal;
    private Set<String> keywords;
    private Integer numInstances;
    private String reason;
    private Integer statsNumAssociates;
    private Integer statsNumInstances;

    @InjectLink(value = "patterns/${instance.id}", style = Style.ABSOLUTE)
    private URI uri;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private Date versionDate;

    @JsonIgnore
    private Long versionId;
    private Set<RequirementPatternVersionDTO> versions;

    @InjectLink(value = "patterns/${instance.id}/versions/${instance.versionId}", style = Style.ABSOLUTE)
    private URI versionUri;

    public RequirementPatternDTO(long id, String name, String description, String comments, Set<SourceDTO> sources,
	    Boolean editable, String author, String goal, String reason, Integer numInstances, Boolean available,
	    Integer statsNumInstances, Integer statsNumAssociates, Set<String> keywords,
	    Set<RequirementPatternVersionDTO> versions, Date versionDate, Long versionId) {
	super(id, description, comments, sources);
	this.name = name;
	this.editable = editable;
	this.author = author;
	this.goal = goal;
	this.reason = reason;
	this.numInstances = numInstances;
	this.available = available;
	this.statsNumInstances = statsNumInstances;
	this.statsNumAssociates = statsNumAssociates;
	this.keywords = keywords;
	this.versions = versions;
	this.versionDate = versionDate;
	this.versionId = versionId;
    }

    public RequirementPatternDTO(RequirementPattern rp) {
	super(rp);
	RequirementPatternVersion rpv = rp.findLastVersion();
	this.name = rp.getName();
	this.editable = (rp.getEditable() != 0);
	this.author = rpv.getAuthor();
	this.goal = rpv.getGoal();
	this.reason = rpv.getReason();
	this.numInstances = rpv.getNumInstances();
	this.available = rpv.getAvailable();
	this.statsNumInstances = rpv.getStatsNumInstances();
	this.statsNumAssociates = rpv.getStatsNumAssociates();

	this.forms = new TreeSet<RequirementFormDTO>();
	for (RequirementForm rf : rpv.getForms()) {
	    this.forms.add(new RequirementFormDTO(rf, this.getId(), rp.findLastVersion().getId()));
	}
	this.costFunction = new HashSet<>();
	for (CostFunction cf : rpv.getCostFunction()) {
	    this.costFunction.add(new CostFunctionDTO(cf));
	}

	this.keywords = new HashSet<String>();
	for (Keyword kw : rp.findLastVersion().getKeywords()) {
	    this.keywords.add(kw.getName());
	}

	this.versions = new TreeSet<RequirementPatternVersionDTO>();
	for (RequirementPatternVersion rpv2 : rp.getVersions()) {
	    this.versions.add(new RequirementPatternVersionDTO(rpv2));
	}

	this.versionId = rpv.getId();
	this.versionDate = rpv.getVersionDate();
    }

    /**
     * This method set to <strong>null</strong> all the values except: Id, name,
     * editable, availability.
     */
    public void reduceFields() {

	setDescription(null);
	setComments(null);
	setSources(null);
	setAuthor(null);
	setGoal(null);
	setReason(null);
	setNumInstances(null);
	setStatsNumAssociates(null);
	setStatsNumInstances(null);
	setKeywords(null);
	setVersions(null);
	setVersionDate(null);
	setVersionId(null);
	setForms(null);
	setCostFunction(null);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getAuthor() {
	return author;
    }

    public Boolean getAvailable() {
	return available;
    }

    public Boolean getEditable() {
	return editable;
    }

    public Set<RequirementFormDTO> getForms() {
	return forms;
    }

    public Set<CostFunctionDTO> getCostFunction() {
	return costFunction;
    }

    public void setCostFunction(Set<CostFunctionDTO> costFunction) {
	this.costFunction = costFunction;
    }

    public String getGoal() {
	return goal;
    }

    public Set<String> getKeywords() {
	return keywords;
    }

    public Integer getNumInstances() {
	return numInstances;
    }

    public String getReason() {
	return reason;
    }

    public Integer getStatsNumAssociates() {
	return statsNumAssociates;
    }

    public Integer getStatsNumInstances() {
	return statsNumInstances;
    }

    public URI getUri() {
	return uri;
    }

    public Date getVersionDate() {
	return versionDate;
    }

    public Long getVersionId() {
	return versionId;
    }

    public Set<RequirementPatternVersionDTO> getVersions() {
	return versions;
    }

    public URI getVersionUri() {
	if (versionId == null)
	    return null;
	return versionUri;
    }

    public void setVersionId(Long id) {
	this.versionId = id;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public void setAvailable(boolean available) {
	this.available = available;
    }

    public void setAvailable(Boolean available) {
	this.available = available;
    }

    public void setEditable(boolean editable) {
	this.editable = editable;
    }

    public void setEditable(Boolean editable) {
	this.editable = editable;
    }

    public void setForms(Set<RequirementFormDTO> forms) {
	this.forms = forms;
    }

    public void setGoal(String goal) {
	this.goal = goal;
    }

    public void setKeywords(Set<String> keywords) {
	this.keywords = keywords;
    }

    public void setNumInstances(Integer numInstances) {
	this.numInstances = numInstances;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public void setStatsNumAssociates(Integer statsNumAssociates) {
	this.statsNumAssociates = statsNumAssociates;
    }

    public void setStatsNumInstances(Integer statsNumInstances) {
	this.statsNumInstances = statsNumInstances;
    }

    public void setUri(URI uri) {
	this.uri = uri;
    }

    public void setVersionDate(Date versionDate) {
	this.versionDate = versionDate;
    }

    public void setVersions(Set<RequirementPatternVersionDTO> versions) {
	this.versions = versions;
    }

    public void setVersionUri(URI versionUri) {
	this.versionUri = versionUri;
    }

    @Override
    public int compareTo(RequirementPatternDTO o2) {
	return this.getName().compareTo(o2.getName());
    }

    @Override
    public int compare(RequirementPatternDTO rpDTO0, RequirementPatternDTO rpDTO1) {
	return rpDTO0.compareTo(rpDTO1);
    }

    @Override
    public String toString() {
	return "RequirementPatternDTO [author=" + author + ", available=" + available + ", editable=" + editable
		+ ", forms=" + forms + ", goal=" + goal + ", keywords=" + keywords + ", numInstances=" + numInstances
		+ ", reason=" + reason + ", statsNumAssociates=" + statsNumAssociates + ", statsNumInstances="
		+ statsNumInstances + ", uri=" + uri + ", versionDate=" + versionDate + ", versionId=" + versionId
		+ ", versions=" + versions + ", versionUri=" + versionUri + ", id=" + id + "]";
    }

}
