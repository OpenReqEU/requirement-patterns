package edu.upc.gessi.rptool.rest.dtos.patternelements;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;

@JsonInclude(Include.NON_NULL)
public class ExtendedPartDTO extends PatternItemDTO implements Comparable<ExtendedPartDTO> {

    private String name;
    private Short pos;

    public ExtendedPartDTO(ExtendedPart ep, Long requirementPatternId, Long versionId, Long formId,
	    String artifactsRelation) {
	super(ep, requirementPatternId, versionId, formId, artifactsRelation);
	this.name = ep.getName();
	this.pos = ep.getPos();
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setPos(Short pos) {
	this.pos = pos;
    }

    public Short getPos() {
	return pos;
    }

    @Override
    public int compareTo(ExtendedPartDTO o) {
	if (o == null)
	    throw new NullPointerException("Comparison between null objects is not allowed");

	try {
	    return this.pos - o.getPos();
	} catch (NullPointerException e) {
	    return 0;
	}
    }
}
