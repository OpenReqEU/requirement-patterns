package edu.upc.gessi.rptool.domain.statistics;

import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public abstract class RequirementData {

    /*
     * ATTRIBUTES
     */

    private long id;
    private String projectName;
    private ClassificationSchema classificationSchema;
    private Classifier generalClassifier;

    /*
     * CREATORS
     */

    public RequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier) {
	setProjectName(projectName);
	setClassificationSchema(classificationSchema);
	setGeneralClassifier(generalClassifier);
    }

    public RequirementData() throws SemanticallyIncorrectException {
	this(new String(), new ClassificationSchema(), new Classifier());
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    public ClassificationSchema getClassificationSchema() {
	return classificationSchema;
    }

    public void setClassificationSchema(ClassificationSchema classificationSchema) {
	this.classificationSchema = classificationSchema;
    }

    public Classifier getGeneralClassifier() {
	return generalClassifier;
    }

    public void setGeneralClassifier(Classifier generalClassifier) {
	this.generalClassifier = generalClassifier;
    }

}
