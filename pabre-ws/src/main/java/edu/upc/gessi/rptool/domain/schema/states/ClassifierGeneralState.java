package edu.upc.gessi.rptool.domain.schema.states;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class ClassifierGeneralState extends AbstractClassifierState {

    public ClassifierGeneralState(Classifier classifier) {
	super(classifier);
    }

    @Override
    public void addPattern(RequirementPattern rp) throws SemanticallyIncorrectException {
	classifier.getPatterns().add(rp);
	classifier.readjustType();
    }

    @Override
    public void addClassifier(Classifier child) throws SemanticallyIncorrectException, IntegrityException {
	classifier.getInternalClassifiers().add(child);
	child.setParentClassifier(classifier);
	classifier.readjustType();
    }

    @Override
    public void removePattern(RequirementPattern rp) throws SemanticallyIncorrectException {
        //not implemented WHY?
    }

    @Override
    public void clearClassifiers() throws SemanticallyIncorrectException {
        //not implemented WHY?
    }

    @Override
    public String getStateTypeName() {
	return "General";
    }

    @Override
    public void deleteClassifier() {
        //not implemented WHY?
    }

}
