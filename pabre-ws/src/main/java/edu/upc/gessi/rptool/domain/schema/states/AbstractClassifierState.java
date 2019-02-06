package edu.upc.gessi.rptool.domain.schema.states;

import java.util.HashSet;

import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public abstract class AbstractClassifierState implements IClassifierState {

    protected Classifier classifier;

    public AbstractClassifierState(Classifier classifier) {
	this.classifier = classifier;
    }

    @Override
    public Classifier copy() throws IntegrityException, SemanticallyIncorrectException {
	Classifier copy = new Classifier();

	// Copy generic fields
	copy.setId(classifier.getId());
	copy.setName(classifier.getName());
	copy.setComments(classifier.getComments());
	copy.setDescription(classifier.getDescription());
	copy.setPos(classifier.getPos());
	copy.setType(classifier.getType());
	copy.setSources(classifier.getSources());

	// Copy classifiers
	copy.setInternalClassifiers(new HashSet<>(classifier.getInternalClassifiers()));

	// Copy patterns
	copy.setPatterns(new HashSet<>(classifier.getPatterns()));

	// Copy parent classifier
	copy.setParentClassifier(classifier.getParentClassifier());

	// Copy parent schema
	copy.setParentSchema(classifier.getParentSchema());

	return copy;
    }

    public Classifier getClassifier() {
	return classifier;
    }

    public void setClassifier(Classifier classifier) {
	this.classifier = classifier;
    }

}
