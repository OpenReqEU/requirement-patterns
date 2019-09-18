package edu.upc.gessi.rptool.rest.unmarshallers.patterns.updaters;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.RequirementFormPutUnmarshaller;

public class RequirementFormUpdater {
    private RequirementForm f;
    private RequirementForm newFields;
    private RequirementFormPutUnmarshaller unmarshaller;
    private boolean availableIsPresent;
    private boolean numInstancesIsPresent;
    private boolean statsNumInstancesIsPresent;
    private boolean statsNumAssociatesIsPresent;

    public RequirementFormUpdater(RequirementForm f, RequirementFormPutUnmarshaller unmarshaller) {
	this.f = f;
	this.unmarshaller = unmarshaller;
    }

    public void update() throws Exception {
	build();
	updateFields();
	save();
    }

    private void save() {
	GenericDataController.update(f);
    }

    private void updateFields() throws IntegrityException {
	if (numInstancesIsPresent)
	    f.setNumInstances(newFields.getNumInstances());
	if (availableIsPresent)
	    f.setAvailable(newFields.getAvailable());
	if (statsNumInstancesIsPresent)
	    f.setStatsNumInstances(newFields.getStatsNumInstances());
	if (statsNumAssociatesIsPresent)
	    f.setStatsNumAssociates(newFields.getStatsNumAssociates());
	if (newFields.getName() != null)
	    f.setName(newFields.getName());
	if (newFields.getDescription() != null)
	    f.setDescription(newFields.getDescription());
	if (newFields.getAuthor() != null)
	    f.setAuthor(newFields.getAuthor());
	if (newFields.getVersion() != null)
	    f.setVersion(newFields.getVersion());
	if (newFields.getSources() != null)
	    f.setSources(newFields.getSources());
	if (newFields.getComments() != null)
	    f.setComments(newFields.getComments());
    }

    private void build() throws IntegrityException, SemanticallyIncorrectException {
	newFields = unmarshaller.build();
	availableIsPresent = unmarshaller.availableIsPresent();
	numInstancesIsPresent = unmarshaller.numInstancesIsPresent();
	statsNumInstancesIsPresent = unmarshaller.statsNumInstancesIsPresent();
	statsNumAssociatesIsPresent = unmarshaller.statsNumAssociatesIsPresent();
    }
}
