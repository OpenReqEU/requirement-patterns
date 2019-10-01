package edu.upc.gessi.rptool.rest.unmarshallers.patterns.updaters;

import org.apache.uima.UIMAException;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.data.NLPController;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.RequirementPatternVersionPutUnmarshaller;

public class RequirementPatternVersionUpdater {

    private RequirementPatternVersion v;
    private RequirementPatternVersion newFields;
    private RequirementPatternVersionPutUnmarshaller unmarshaller;

    public RequirementPatternVersionUpdater(RequirementPatternVersion v,
	    RequirementPatternVersionPutUnmarshaller unmarshaller) {
	this.v = v;
	this.unmarshaller = unmarshaller;
    }

    public void update() throws Exception {
	build();
	updateFileds();
	save();
    }

    private void save() {
	GenericDataController.update(v);
    }

    private void updateFileds() throws IntegrityException, UIMAException {

	if (newFields.getVersionDate() != null)
	    v.setVersionDate(newFields.getVersionDate());
	if (newFields.getAuthor() != null)
	    v.setAuthor(newFields.getAuthor());
	if (newFields.getGoal() != null)
	    v.setGoal(newFields.getGoal());
	if (newFields.getReason() != null)
	    v.setReason(newFields.getReason());
	if (unmarshaller.numInstancesIsPresent())
	    v.setNumInstances(newFields.getNumInstances());
	if (unmarshaller.availableIsPresent())
	    v.setAvailable(newFields.getAvailable());
	if (unmarshaller.statsNumInstancesIsPresent())
	    v.setStatsNumInstances(newFields.getStatsNumInstances());
	if (unmarshaller.statsNumAssociatesIsPresent())
	    v.setStatsNumAssociates(newFields.getStatsNumAssociates());
	if (newFields.getArtifactRelation() != null)
	    v.setArtifactRelation(newFields.getArtifactRelation());
	if (newFields.getKeywords() != null) {
	    v.setKeywords(newFields.getKeywords());
	    v.setLemmatizedVersion(NLPController.lemmatizeVersion(v));// should be the last one to ensure the
								      // lemmatization is correct
	}
    }

    private void build() throws Exception {
	newFields = unmarshaller.build();
    }
}
