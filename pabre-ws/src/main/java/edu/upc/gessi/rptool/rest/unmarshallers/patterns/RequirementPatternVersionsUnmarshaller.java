package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;

public class RequirementPatternVersionsUnmarshaller {
    private Set<RequirementPatternVersionUnmarshaller> versions;

    @JsonCreator
    public RequirementPatternVersionsUnmarshaller(
	    @JsonProperty(required = true) Set<RequirementPatternVersionUnmarshaller> versions) {
	this.versions = versions;

    }

    public Set<RequirementPatternVersion> build() throws Exception {
	Set<RequirementPatternVersion> ret = new HashSet<>();
	if (versions != null) {
	    for (RequirementPatternVersionUnmarshaller aux : versions) {
		ret.add(aux.build());
	    }
	}

	return ret;
    }

    public Set<RequirementPatternVersionUnmarshaller> getVersions() {
	return versions;
    }

    public void setVersions(Set<RequirementPatternVersionUnmarshaller> versions) {
	this.versions = versions;
    }

}
