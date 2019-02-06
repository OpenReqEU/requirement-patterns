package edu.upc.gessi.rptool.rest.dtos.importexport.patternelements;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;

@JsonInclude(Include.NON_NULL)
public class RequirementFormExportDTO extends PatternElementExportDTO implements Comparable<RequirementFormExportDTO> {

    private String name;
    private String author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
    private Date modificationDate;
    private Integer numInstances;
    private Integer statsNumInstances;
    private Integer statsNumAssociates;
    private Short pos;
    private Boolean available;
    private FixedPartExportDTO fixedPart;
    private Set<ExtendedPartExportDTO> extendedParts;

    public RequirementFormExportDTO(RequirementForm rf, Long requirementPatternId, Long versionId) {
	super(rf);
	this.name = rf.getName();
	this.author = rf.getAuthor();
	this.modificationDate = rf.getVersion();
	this.numInstances = rf.getNumInstances();
	this.statsNumInstances = rf.getStatsNumInstances();
	this.statsNumAssociates = rf.getStatsNumAssociates();
	this.pos = rf.getPos();
	this.available = rf.getAvailable();
	this.fixedPart = new FixedPartExportDTO(rf.getFixedPart(), rf.getFixedPart().getArtifactRelation());
	this.extendedParts = new TreeSet<ExtendedPartExportDTO>();
	for (ExtendedPart ep : rf.getExtendedParts()) {
	    this.extendedParts.add(new ExtendedPartExportDTO(ep, ep.getArtifactRelation()));
	}

    }

    @Override
    public int compareTo(RequirementFormExportDTO o) {
	if (o == null)
	    throw new NullPointerException("Comparison between null objects is not allowed");
	try {
	    return this.pos - o.getPos();
	} catch (NullPointerException e) {
	    return 0;
	}
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

    public void setAuthor(String author) {
	this.author = author;
    }

    public Date getModificationDate() {
	return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
	this.modificationDate = modificationDate;
    }

    public Integer getNumInstances() {
	return numInstances;
    }

    public void setNumInstances(Integer numInstances) {
	this.numInstances = numInstances;
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

    public Short getPos() {
	return pos;
    }

    public void setPos(Short pos) {
	this.pos = pos;
    }

    public Boolean getAvailable() {
	return available;
    }

    public void setAvailable(Boolean available) {
	this.available = available;
    }

    public FixedPartExportDTO getFixedPart() {
	return fixedPart;
    }

    public void setFixedPart(FixedPartExportDTO fixedPart) {
	this.fixedPart = fixedPart;
    }

    public Set<ExtendedPartExportDTO> getExtendedParts() {
	return extendedParts;
    }

    public void setExtendedParts(Set<ExtendedPartExportDTO> extendedParts) {
	this.extendedParts = extendedParts;
    }

}
