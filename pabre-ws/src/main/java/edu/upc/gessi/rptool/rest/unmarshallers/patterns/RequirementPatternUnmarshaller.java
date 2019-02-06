package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.IdToDomainObject;
import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class RequirementPatternUnmarshaller {
    protected long id;
    protected RequirementPattern rp;
    protected Set<Long> sources;
    protected List<RequirementPatternVersionUnmarshaller> versions;
    protected String name;
    protected String comments;
    protected String description;
    protected boolean editable;

    @JsonCreator
    public RequirementPatternUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "versions", required = true) ArrayList<RequirementPatternVersionUnmarshaller> versions,
	    @JsonProperty(value = "editable", required = true) boolean editable) throws IOException {
	this.id = id;
	this.name = name;
	this.comments = comments;
	this.description = description;
	this.editable = editable;
	this.versions = versions;
	this.sources = sources;
    }

    public RequirementPatternUnmarshaller(String name, String description, String comments, Set<Long> sources,
	    ArrayList<RequirementPatternVersionUnmarshaller> versions, Boolean editable) {
	super();
	this.sources = sources;
	this.versions = versions;
	this.name = name;
	this.comments = comments;
	this.description = description;
	if (editable != null) {
	    this.editable = editable;
	}

    }

    public RequirementPatternUnmarshaller(long id, String name, String comments, String description, boolean editable) {
	super();
	this.id = id;
	this.name = name;
	this.comments = comments;
	this.description = description;
	this.editable = editable;
    }

    protected void setVersions() throws IntegrityException, SemanticallyIncorrectException {

	for (RequirementPatternVersionUnmarshaller rpvu : versions) {
	    RequirementPatternVersion rpv = rpvu.build();
	    rpv.setRequirementPattern(rp);
	    rp.addVersion(rpv);
	}
    }

    protected void checkNumberOfVersions() throws SemanticallyIncorrectException {
	if (versions != null && versions.size() == 0)
	    throw new SemanticallyIncorrectException("no requirement pattern versions provided in pattern");
    }

    protected void setSources() throws SemanticallyIncorrectException {
	if (sources != null) {
	    try {
		for (Source s : IdToDomainObject.getSources(sources))
		    rp.addSource(s);
	    } catch (NotFoundException e) {
		throw new SemanticallyIncorrectException("invalid source id in pattern");
	    }
	}

    }

    protected void setName() throws IntegrityException {
	rp.setName(name);
    }

    public RequirementPattern build() throws IntegrityException, SemanticallyIncorrectException {
	rp = new RequirementPattern();
	rp.setId(id);
	setName();
	rp.setComments(comments);
	rp.setDescription(description);
	rp.setEditable(editable ? 1 : 0);
	checkNumberOfVersions();
	setVersions();
	setSources();
	return rp;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public Set<Long> getSources() {
	return sources;
    }

    public void setSources(Set<Long> sources) {
	this.sources = sources;
    }

    public List<RequirementPatternVersionUnmarshaller> getVersions() {
	return versions;
    }

    public void setVersions(List<RequirementPatternVersionUnmarshaller> versions) {
	this.versions = versions;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public boolean isEditable() {
	return editable;
    }

    public void setEditable(boolean editable) {
	this.editable = editable;
    }

}
