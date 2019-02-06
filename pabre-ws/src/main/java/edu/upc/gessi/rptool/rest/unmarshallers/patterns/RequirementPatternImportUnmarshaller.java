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
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class RequirementPatternImportUnmarshaller extends RequirementPatternUnmarshaller {
    private Set<String> sources;
    private List<RequirementPatternVersionImportUnmarshaller> versions;

    @JsonCreator
    public RequirementPatternImportUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sources,
	    @JsonProperty(value = "versions", required = true) ArrayList<RequirementPatternVersionImportUnmarshaller> versions,
	    @JsonProperty(value = "editable", required = true) boolean editable) throws IOException {

	super(id, name, comments, description, editable);
	this.versions = versions;
	this.sources = sources;

    }

    @Override
    protected void setVersions() throws IntegrityException, SemanticallyIncorrectException {
	for (RequirementPatternVersionImportUnmarshaller rpvu : versions) {
	    RequirementPatternVersion rpv = rpvu.build();
	    rpv.setRequirementPattern(rp);
	    rp.addVersion(rpv);
	}
    }

    @Override
    protected void setSources() throws SemanticallyIncorrectException {
	if (sources != null) {
	    try {
		for (Source s : IdToDomainObject.getSourcesByIdentifiers(sources))
		    rp.addSource(s);
	    } catch (NotFoundException e) {
		throw new SemanticallyIncorrectException("invalid source id in pattern");
	    }
	}

    }

    @Override
    protected void checkNumberOfVersions() throws SemanticallyIncorrectException {
	if (versions.size() == 0)
	    throw new SemanticallyIncorrectException("no requirement pattern versions provided in pattern");
    }

    /**
     * Return true when all the elements inside contains a ID different then 0 (0
     * mean that the ID is not indicated)
     * 
     * @return True when all the elements has ID, otherwise returns false
     */
    public boolean checkAllItemsContainsID() {
	boolean b = id != 0;
	for (RequirementPatternVersionImportUnmarshaller rpvi : versions) {
	    if(!rpvi.checkAllItemsContainsID()) {
		b = false;
	    }
	}
	return b;
    }

}
