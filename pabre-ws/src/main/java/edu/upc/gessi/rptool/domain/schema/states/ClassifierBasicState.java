package edu.upc.gessi.rptool.domain.schema.states;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class ClassifierBasicState extends AbstractClassifierState {

    public ClassifierBasicState(Classifier classifier) {
	super(classifier);

    }

    @Override
    public void addPattern(RequirementPattern rp) throws SemanticallyIncorrectException {
	classifier.getPatterns().add(rp);
    }

    @Override
    public void removePattern(RequirementPattern rp) throws SemanticallyIncorrectException {
	classifier.getPatterns().remove(rp);
	classifier.readjustType();
    }

    @Override
    public void clearClassifiers() throws SemanticallyIncorrectException {
    }

    @Override
    public String getStateTypeName() {
	return "Basic";
    }

    @Override
    public void deleteClassifier() throws SemanticallyIncorrectException {
	classifier.clearPatterns();
    }

}
