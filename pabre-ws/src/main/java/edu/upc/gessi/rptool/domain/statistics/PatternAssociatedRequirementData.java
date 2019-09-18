package edu.upc.gessi.rptool.domain.statistics;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

/**
 * 
 * This class represents a requirement data with some requirement pattern
 * associated
 */

public class PatternAssociatedRequirementData extends NoInstancedRequirementData {

    /*
     * ATTRIBUTES
     */

    private RequirementPattern requirementPattern;
    private String formName;

    /*
     * CREATORS
     */

    public PatternAssociatedRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier, String name, String formText, String questionText, String state,
	    RequirementPattern requirementPattern, String formName) {
	super(projectName, classificationSchema, generalClassifier, name, formText, questionText, state);

	setRequirementPattern(requirementPattern);
	setFormName(formName);
    }

    public PatternAssociatedRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier, String name, String formText, String questionText, String state,
	    RequirementPattern requirementPattern) {
	this(projectName, classificationSchema, generalClassifier, name, formText, questionText, state,
		requirementPattern, "");
    }

    public PatternAssociatedRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier, String name, String formText, String questionText,
	    RequirementPattern requirementPattern) {
	this(projectName, classificationSchema, generalClassifier, name, formText, questionText, "",
		requirementPattern, "");
    }

    public PatternAssociatedRequirementData() throws SemanticallyIncorrectException {
	this("", new ClassificationSchema(), new Classifier(), "", "",
			"", new RequirementPattern());
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

    public String getFormName() {
	return formName;
    }

    public void setFormName(String formName) {
	this.formName = formName;
    }

}
