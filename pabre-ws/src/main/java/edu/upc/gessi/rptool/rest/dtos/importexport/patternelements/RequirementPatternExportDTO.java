package edu.upc.gessi.rptool.rest.dtos.importexport.patternelements;

import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;

@JsonInclude(Include.NON_NULL)
public class RequirementPatternExportDTO extends PatternElementExportDTO {

    private String name;

    private Boolean editable;

    private Set<RequirementPatternVersionExportDTO> versions;

    public RequirementPatternExportDTO(RequirementPattern rp) {
	super(rp);
	this.name = rp.getName();
	this.editable = rp.getEditable() == 0 ? false : true;
	this.versions = new TreeSet<RequirementPatternVersionExportDTO>();
	for (RequirementPatternVersion rpv : rp.getVersions()) {
	    this.versions.add(new RequirementPatternVersionExportDTO(rpv));
	}

    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Boolean getEditable() {
	return editable;
    }

    public void setEditable(Boolean editable) {
	this.editable = editable;
    }

    public Set<RequirementPatternVersionExportDTO> getVersions() {
	return versions;
    }

    public void setVersions(Set<RequirementPatternVersionExportDTO> versions) {
	this.versions = versions;
    }

}
