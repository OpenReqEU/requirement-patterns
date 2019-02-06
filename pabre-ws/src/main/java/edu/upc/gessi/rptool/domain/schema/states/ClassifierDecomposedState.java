package edu.upc.gessi.rptool.domain.schema.states;

import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class ClassifierDecomposedState extends AbstractClassifierState {

    public ClassifierDecomposedState(Classifier classifier) {
	super(classifier);

    }

    @Override
    public boolean alreadyHas(long id) {
	boolean found = false;
	if (classifier.getId() == id) {
	    return true;
	}
	for (Classifier internalClassifier : classifier.getInternalClassifiers()) {
	    found = internalClassifier.alreadyHas(id);
	    if (found) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public void addClassifier(Classifier child) throws SemanticallyIncorrectException, IntegrityException {
	if (classifier.containsClassifierSameName(child.getName())) {
	    throw new IntegrityException(
		    "You cannot add this internal classifier: the parent already contains a internal classifier with this name.");
	} else {
	    child.setParentClassifier(classifier);
	    classifier.getInternalClassifiers().add(child);

	}
    }

    @Override
    public void removeClassifier(Classifier child) throws SemanticallyIncorrectException {
	child.setParentClassifier(null);
	//classifier.getInternalClassifiers().remove(child);
    }

    @Override
    public void clearClassifiers() throws SemanticallyIncorrectException {
	classifier.clearInternalsClassifiers();
    }

    @Override
    public String getStateTypeName() {
	return "Decomposed";
    }

    @Override
    public void deleteClassifier() throws SemanticallyIncorrectException {
	classifier.clearInternalsClassifiers();
    }

}
