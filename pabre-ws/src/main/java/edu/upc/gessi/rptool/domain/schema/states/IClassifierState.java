package edu.upc.gessi.rptool.domain.schema.states;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public interface IClassifierState {

    /**
     * This method should return the name of the current state
     * 
     * @return String with the name of the type (Root, Basic, General, Decomposed)
     */
    String getStateTypeName();

    default void addClassifier(Classifier child) throws SemanticallyIncorrectException, IntegrityException {
	throw new SemanticallyIncorrectException(getStateTypeName() + " could not add child classifier");
    }

    default void removeClassifier(Classifier child) throws SemanticallyIncorrectException {
	throw new SemanticallyIncorrectException(getStateTypeName() + " could not remove child classifier");
    }

    default void clearClassifiers() throws SemanticallyIncorrectException {
	throw new SemanticallyIncorrectException(getStateTypeName() + " could not have classifiers to clear");
    }

    default void addPattern(RequirementPattern rp) throws SemanticallyIncorrectException {
	throw new SemanticallyIncorrectException(getStateTypeName() + " could not have patterns");
    }

    default void removePattern(RequirementPattern rp) throws SemanticallyIncorrectException {
	throw new SemanticallyIncorrectException(getStateTypeName() + " could not have patterns");
    }

    default boolean alreadyHas(long id) {
	return false;
    }

    /**
     * Method used to generate a copy of the current classifier
     * 
     * @return New instance of classifier with all the values of the current
     *         classifier
     * @throws IntegrityException
     * @throws SemanticallyIncorrectException
     */
    Classifier copy() throws IntegrityException, SemanticallyIncorrectException;

    /**
     * Method used to delete all the internal information depending on the
     * classifier type
     * 
     * @throws SemanticallyIncorrectException
     */
    void deleteClassifier() throws SemanticallyIncorrectException;

}
