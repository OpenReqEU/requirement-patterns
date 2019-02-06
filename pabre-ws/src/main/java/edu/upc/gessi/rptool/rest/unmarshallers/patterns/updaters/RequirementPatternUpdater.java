package edu.upc.gessi.rptool.rest.unmarshallers.patterns.updaters;

import org.apache.uima.UIMAException;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.data.NLPController;
import edu.upc.gessi.rptool.data.PatternDataController;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.RequirementPatternPutUnmarshaller;

@Deprecated
public class RequirementPatternUpdater {
    private RequirementPattern p;
    private RequirementPattern newFields;
    private RequirementPatternPutUnmarshaller unmarshaller;
    private boolean editableIsPresent;
    private boolean updateLemmatizer;

    public RequirementPatternUpdater(RequirementPattern p, RequirementPatternPutUnmarshaller unmarshaller) {
	this.p = p;
	this.unmarshaller = unmarshaller;
    }

    public void update() throws Exception {
	build();
	updateFileds();
	updateInternalControlFields();
	save();
    }

    private void updateInternalControlFields() throws IntegrityException, UIMAException {
	if (updateLemmatizer) {
	    for (RequirementPatternVersion rpv : p.getVersions()) {
		rpv.setLemmatizedVersion(NLPController.lemmatizeVersion(rpv));
		PatternDataController.update(rpv);
	    }
	}
    }

    private void save() {
	GenericDataController.update(p);
    }

    private void updateFileds() throws IntegrityException {

	if (newFields.getName() != null) {
	    p.setName(newFields.getName());
	    updateLemmatizer = true;
	}
	if (newFields.getDescription() != null) {
	    p.setDescription(newFields.getDescription());
	    updateLemmatizer = true;
	}
	if (newFields.getComments() != null)
	    p.setComments(newFields.getComments());
	if (newFields.getSources() != null)
	    p.setSources(newFields.getSources());
	if (editableIsPresent)
	    p.setEditable(newFields.getEditable());
    }

    private void build() throws Exception {
	newFields = unmarshaller.build();
	editableIsPresent = unmarshaller.getEditableIsPresent();
    }
}
