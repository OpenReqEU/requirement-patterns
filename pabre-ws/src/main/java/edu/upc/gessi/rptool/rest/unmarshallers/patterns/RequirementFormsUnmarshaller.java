package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class RequirementFormsUnmarshaller {
    private Set<RequirementFormUnmarshaller> forms;

    @JsonCreator
    public RequirementFormsUnmarshaller(@JsonProperty(required = true) Set<RequirementFormUnmarshaller> forms) {
	this.forms = forms;
    }

    public Set<RequirementForm> build() throws SemanticallyIncorrectException, IntegrityException {
	Set<RequirementForm> ret = new HashSet<>();
	if (forms != null) {
	    for (RequirementFormUnmarshaller aux : forms) {
		ret.add(aux.build());
	    }
	}
	return ret;
    }

    public Set<RequirementFormUnmarshaller> getForms() {
	return forms;
    }

    public void setForms(Set<RequirementFormUnmarshaller> forms) {
	this.forms = forms;
    }

}
