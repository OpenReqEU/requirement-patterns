package edu.upc.gessi.rptool.domain.schema.states;

import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class ClassifierRootState extends AbstractClassifierState {

    public ClassifierRootState(Classifier classifier) {
	super(classifier);
    }

    @Override
    public void addClassifier(Classifier child) throws SemanticallyIncorrectException, IntegrityException {
	classifier.getInternalClassifiers().add(child);
	child.setParentClassifier(classifier);
    }

    @Override
    public void removeClassifier(Classifier child) throws SemanticallyIncorrectException {
	classifier.getInternalClassifiers().remove(child);
	child.setParentClassifier(null);
    }

    @Override
    public void clearClassifiers() throws SemanticallyIncorrectException {
	classifier.getInternalClassifiers().clear();
    }

    @Override
    public String getStateTypeName() {
	return "Root";
    }

    @Override
    public void deleteClassifier() throws SemanticallyIncorrectException {
	classifier.clearInternalsClassifiers();
    }

}
