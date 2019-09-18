package edu.upc.gessi.rptool.rest.unmarshallers.patterns.updaters;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;
import edu.upc.gessi.rptool.domain.patternelements.FixedPart;
import edu.upc.gessi.rptool.domain.patternelements.PatternItem;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.PatternItemPutUnmarshaller;

public class RequirementPatternItemUpdater {
    private PatternItem item;
    private PatternItem aux;
    private PatternItemPutUnmarshaller unmarshaller;
    private boolean availableIsPresent;
    private boolean numInstancesIsPresent;
    private boolean statsNumInstancesIsPresent;

    public RequirementPatternItemUpdater(PatternItem item, PatternItemPutUnmarshaller metricJson) {
	this.item = item;
	this.unmarshaller = metricJson;
    }

    public void update() throws SemanticallyIncorrectException, UnrecognizedPropertyException {
	unmarshall();
	updateFileds();
	save();
    }

    private void save() {
	GenericDataController.update(item);
    }

    private void updateFileds() {
	if (aux.getPatternText() != null)
	    item.setPatternText(aux.getPatternText());
	if (aux.getQuestionText() != null)
	    item.setQuestionText(aux.getQuestionText());
	if (numInstancesIsPresent)
	    item.setNumInstances(aux.getNumInstances());
	if (availableIsPresent)
	    item.setAvailable(aux.getAvailable());
	if (statsNumInstancesIsPresent)
	    item.setStatsNumInstances(aux.getStatsNumInstances());
	if (aux.getParameters() != null) {
	    item.getParameters().clear();
	    item.getParameters().addAll(aux.getParameters());
	}
	if (aux.getArtifactRelation() != null)
	    item.setArtifactRelation(aux.getArtifactRelation());
	if (aux instanceof ExtendedPart && item instanceof ExtendedPart) {
	    ExtendedPart ep2 = (ExtendedPart) aux;
	    if (ep2.getName() != null && !ep2.getName().equals("")) {
		((ExtendedPart) item).setName(ep2.getName());
	    }
	}

    }

    private void unmarshall() throws SemanticallyIncorrectException, UnrecognizedPropertyException {
	aux = unmarshaller.build(item instanceof FixedPart);
	unmarshaller.checkFields();
	availableIsPresent = unmarshaller.availableIsPresent();
	numInstancesIsPresent = unmarshaller.numInstancesIsPresent();
	statsNumInstancesIsPresent = unmarshaller.statsNumInstancesIsPresent();

    }
}
