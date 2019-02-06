package edu.upc.gessi.rptool.rest.dtos.patternelements;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.FixedPart;

@JsonInclude(Include.NON_NULL)
public class FixedPartDTO extends PatternItemDTO {

    public FixedPartDTO(FixedPart fp, Long requirementPatternId, Long versionId, Long formId,
	    String artifactsRelation) {

	super(fp, requirementPatternId, versionId, formId, artifactsRelation);
    }
}
