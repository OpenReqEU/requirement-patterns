package edu.upc.gessi.rptool.rest.dtos.patternelements;

import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;
import edu.upc.gessi.rptool.domain.patternelements.FixedPart;

@JsonInclude(Include.NON_NULL)
public class RequirementFormPartsDTO {
    private FixedPartDTO fixedPart;
    private Set<ExtendedPartDTO> extendedParts;

    public RequirementFormPartsDTO(FixedPart fixedPart, Set<ExtendedPart> extendedParts, Long requirementPatternId,
	    Long versionId, Long formId) {
	super();
	this.fixedPart = new FixedPartDTO(fixedPart, requirementPatternId, versionId, formId,
		fixedPart.getArtifactRelation());
	this.extendedParts = new TreeSet<>();
	for (ExtendedPart ep : extendedParts) {
	    this.extendedParts
		    .add(new ExtendedPartDTO(ep, requirementPatternId, versionId, formId, ep.getArtifactRelation()));
	}
    }

    public FixedPartDTO getFixedPart() {
	return fixedPart;
    }

    public void setFixedPart(FixedPartDTO fixedPart) {
	this.fixedPart = fixedPart;
    }

    public Set<ExtendedPartDTO> getExtendedParts() {
	return extendedParts;
    }

    public void setExtendedParts(Set<ExtendedPartDTO> extendedParts) {
	this.extendedParts = extendedParts;
    }
}
