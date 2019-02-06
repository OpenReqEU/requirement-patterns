package edu.upc.gessi.rptool.domain.statistics;

import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public abstract class NoInstancedRequirementData extends RequirementData {

    private final static String NOT_PROCESSED_STATE = "Not processed";
    private final static String REJECTED_STATE = "Rejected";
    private final static String ADDED_STATE = "Added";

    /*
     * ATTRIBUTES
     */

    private String name;
    private String formText;
    private String questionText;
    private String state;

    /*
     * CREATORS
     */

    public NoInstancedRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier, String name, String formText, String questionText, String state) {
	super(projectName, classificationSchema, generalClassifier);
	setName(name);
	setFormText(formText);
	setQuestionText(questionText);
	setProjectName(projectName);
	setState(state);
    }

    public NoInstancedRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier, String name, String formText, String questionText) {
	this(projectName, classificationSchema, generalClassifier, name, formText, questionText, NOT_PROCESSED_STATE);
    }

    public NoInstancedRequirementData() throws SemanticallyIncorrectException {
	this(new String(), new ClassificationSchema(), new Classifier(), new String(), new String(),
		new String());
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getFormText() {
	return formText;
    }

    public void setFormText(String formText) {
	this.formText = formText;
    }

    public String getQuestionText() {
	return questionText;
    }

    public void setQuestionText(String questionText) {
	this.questionText = questionText;
    }

    public String getState() {
	return state;
    }

    public void setState(String state) {
	if (state == null || (!state.equals(NOT_PROCESSED_STATE) && !state.equals(REJECTED_STATE)
		&& !state.equals(ADDED_STATE))) {
	    this.state = NOT_PROCESSED_STATE;
	} else {
	    this.state = state;
	}
    }

    public static String getNotProcessedState() {
	return NOT_PROCESSED_STATE;
    }

    public static String getRejectedState() {
	return REJECTED_STATE;
    }

    public static String getAddedState() {
	return ADDED_STATE;
    }

}