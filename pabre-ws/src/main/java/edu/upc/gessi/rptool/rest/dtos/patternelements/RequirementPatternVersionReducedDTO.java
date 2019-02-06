package edu.upc.gessi.rptool.rest.dtos.patternelements;

import java.net.URI;
import java.util.Date;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;

public class RequirementPatternVersionReducedDTO extends ReducedPatternObjectDTO
	implements Comparable<RequirementPatternVersionReducedDTO> {

    @InjectLink(value = "patterns/${instance.requirementPatternId}/versions/${instance.id}", style = Style.ABSOLUTE)
    protected URI uri;
    protected String author;
    protected String reason;
    @JsonIgnore
    protected long requirementPatternId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
    protected Date versionDate;

    public RequirementPatternVersionReducedDTO(RequirementPatternVersion rpv) {
	super(rpv.getId());
	author = rpv.getAuthor();
	reason = rpv.getReason() == null ? "" : rpv.getReason();
	requirementPatternId = rpv.getRequirementPattern().getId();
	versionDate = rpv.getVersionDate();

    }

    @Override
    public int compareTo(RequirementPatternVersionReducedDTO arg0) {
	int r = this.versionDate.compareTo(arg0.versionDate);
	if (r != 0)
	    return r;
	else
	    return (this.id.compareTo(arg0.id));
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public URI getUri() {
	return uri;
    }

    public void setUri(URI uri) {
	this.uri = uri;
    }

    public long getRequirementPatternId() {
	return requirementPatternId;
    }

    public void setRequirementPatternId(long requirementPatternId) {
	this.requirementPatternId = requirementPatternId;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

}
