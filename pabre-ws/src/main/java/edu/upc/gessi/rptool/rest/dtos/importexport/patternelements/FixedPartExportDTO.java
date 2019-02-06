package edu.upc.gessi.rptool.rest.dtos.importexport.patternelements;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.FixedPart;

@JsonInclude(Include.NON_NULL)
public class FixedPartExportDTO extends PatternItemExportDTO {
    public FixedPartExportDTO(FixedPart fp, String artifactsRelation) {
	super(fp, artifactsRelation);
    }
}
