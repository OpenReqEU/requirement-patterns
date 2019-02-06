package edu.upc.gessi.rptool.domain.statistics;

import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

/**
 * 
 * This class represents a requirement data with no requirement pattern
 * associated. It's only associated to a general classifier
 */

public class NewRequirementData extends NoInstancedRequirementData {

    /*
     * ATTRIBUTES
     */

    private String patternName;

    /*
     * CREATORS
     */

    public NewRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier, String name, String formText, String questionText, String state,
	    String patternName) {
	super(projectName, classificationSchema, generalClassifier, name, formText, questionText, state);
	setPatternName(patternName);
    }

    public NewRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier, String name, String formText, String questionText) {
	this(projectName, classificationSchema, generalClassifier, name, formText, questionText, new String(),
		new String());
    }

    public NewRequirementData() throws SemanticallyIncorrectException {
	this(new String(), new ClassificationSchema(), new Classifier(), new String(), new String(),
		new String());
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public String getPatternName() {
	return patternName;
    }

    public void setPatternName(String patternName) {
	this.patternName = patternName;
    }

}
