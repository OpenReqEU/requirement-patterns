package edu.upc.gessi.rptool.domain.statistics;

import edu.upc.gessi.rptool.domain.patternelements.FixedPart;
import edu.upc.gessi.rptool.domain.patternelements.PatternItem;
import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class InstancedRequirementData extends RequirementData {

    /*
     * ATTRIBUTES
     */

    private Integer numInstancesInProject;
    private PatternItem isInstanceOf;

    /*
     * CREATORS
     */

    public InstancedRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier, Integer numInstancesInProject, PatternItem patternItem) {
	super(projectName, classificationSchema, generalClassifier);
	setNumInstancesInProject(numInstancesInProject);
	setIsInstanceOf(patternItem);
    }

    public InstancedRequirementData(String projectName, ClassificationSchema classificationSchema,
	    Classifier generalClassifier) {
	this(projectName, classificationSchema, generalClassifier, 0, new FixedPart());
    }

    public InstancedRequirementData() throws SemanticallyIncorrectException {
	this(new String(), new ClassificationSchema(), new Classifier());
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public Integer getNumInstancesInProject() {
	return numInstancesInProject;
    }

    public void setNumInstancesInProject(Integer numInstancesInProject) {
	this.numInstancesInProject = numInstancesInProject;
    }

    public PatternItem getIsInstanceOf() {
	return isInstanceOf;
    }

    public void setIsInstanceOf(PatternItem isInstanceOf) {
	this.isInstanceOf = isInstanceOf;
    }

}