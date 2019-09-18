package edu.upc.gessi.rptool.domain.statistics;

import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

/**
 * 
 * This class represents a requirement data with some requirement pattern and
 * requirement form associated
 */

public class FormAssociatedRequirementData extends NoInstancedRequirementData {

    /*
     * ATTRIBUTES
     */

    private RequirementPattern requirementPattern;
    private RequirementForm requirementForm;
    private String partName;

    /*
     * CREATORS
     */

    public FormAssociatedRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier, String name, String formText, String questionText, String state,
	    RequirementPattern requirementPattern, RequirementForm requirementForm, String partName) {

	super(projectName, classificationSchema, generalClassifier, name, formText, questionText, state);
	setRequirementPattern(requirementPattern);
	setRequirementForm(requirementForm);
	setPartName(partName);
    }

    public FormAssociatedRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier, String name, String formText, String questionText, String state,
	    RequirementPattern requirementPattern, RequirementForm requirementForm) {
	this(projectName, classificationSchema, generalClassifier, name, formText, questionText, state,
		requirementPattern, requirementForm, "");
    }

    public FormAssociatedRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier, String name, String formText, String questionText,
	    RequirementPattern requirementPattern, RequirementForm requirementForm) {
	this(projectName, classificationSchema, generalClassifier, name, formText, questionText, "",
		requirementPattern, requirementForm);
    }

    public FormAssociatedRequirementData() throws SemanticallyIncorrectException {

	this("", new ClassificationSchema(), new Classifier(), "", "",
            "", new RequirementPattern(), new RequirementForm());
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public RequirementPattern getRequirementPattern() {
	return requirementPattern;
    }

    public void setRequirementPattern(RequirementPattern requirementPattern) {
	this.requirementPattern = requirementPattern;
    }

    public RequirementForm getRequirementForm() {
	return requirementForm;
    }

    public void setRequirementForm(RequirementForm requirementForm) {
	this.requirementForm = requirementForm;
    }

    public String getPartName() {
	return partName;
    }

    public void setPartName(String partName) {
	this.partName = partName;
    }

}
