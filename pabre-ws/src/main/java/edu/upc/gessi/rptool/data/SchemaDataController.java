package edu.upc.gessi.rptool.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import edu.upc.gessi.rptool.data.mediators.MediatorGeneric;
import edu.upc.gessi.rptool.data.mediators.MediatorSchemas;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

@SuppressWarnings({ "unchecked" })
public final class SchemaDataController extends GenericDataController {

    private static final Logger logger = Logger.getLogger(SchemaDataController.class.getName());

    /**
     * Constructor has to be private so no one be able to create any instance
     */
    private SchemaDataController() {
    }

    /**
     * List names of the all Classification Schemas saved in database.
     * 
     * @return The list with the names of the all Classification Schemas saved in
     *         database.
     */
    public static List<String> listSchemaNames() {
	return MediatorSchemas.listNames(ClassificationSchema.class);
    }

    /**
     * List all Classification Schemas saved in database.
     * 
     * @author fernando.mora
     * @return The list of the all Classification Schemas saved in database.
     */
    public static List<ClassificationSchema> listSchemas() {
	return MediatorSchemas.list(ClassificationSchema.class);
    }

    /**
     * Given a list with names following with this order:
     * Schema,RootClassifier,InternalClassifier0,InternalCLassifier1,...,InternalclassifierN
     * return the last InternalCLassifierN in the list. The list must have at least
     * 3 strings (Schema,RootCLassifier,InternalCLassifier)
     * 
     * @param namesList
     *            List with the names of the path of classifiers to follow
     * @return {@link Classifier} who is the last one in the list
     * @throws ValueException
     *             When the parameters are less then 3
     */
    public static Classifier getInternalClassifierByNames(List<String> namesList) throws ValueException {
	if (namesList.size() <= 2) {
	    throw new ValueException(
		    "Query names list must have at least 3 names (schema, rootCLassifier and internalClassifier)");
	}
	Classifier icReturn = null;
	Classifier rc = null;
	ClassificationSchema cs = SchemaDataController.getSchema(namesList.get(0));// get the schema
	if (cs != null) {
	    for (Iterator<Classifier> it = cs.getRootClassifiers().iterator(); it.hasNext();) {
		Classifier f = it.next();
		if (f.getName().equals(namesList.get(1))) {
		    rc = f; // Get the rootClassifier
		    break;
		}
	    }
	}
	if (rc != null) { // if
	    icReturn = rc.getTheLastInternalsClassifiersIncludedInNames(namesList.subList(2, namesList.size()));
	}
	return icReturn;
    }

    /**
     * Gets the Classification Schema saved in the database with the name received
     * as parameter.
     * 
     * @param name
     *            The object's name that we want to get.
     * @return - The Classification Schema saved in the database with the name
     *         received as parameter. - Null if doesn't exist any Classification
     *         Schema with the name received as parameter
     */
    public static ClassificationSchema getSchema(String name) {
	return (ClassificationSchema) MediatorSchemas.get(name, ClassificationSchema.class);
    }

    /**
     * @author fernando.mora Gets the Classification Schema saved in the database
     *         with the i received as parameter.
     * @param id
     *            The object's id that we want to get.
     * @return - The Classification Schema saved in the database with the name
     *         received as parameter. - Null if doesn't exist any Classification
     *         Schema with the name received as parameter
     */
    public static ClassificationSchema getSchema(long id) {
	return (ClassificationSchema) MediatorSchemas.get(id, ClassificationSchema.class);
    }

    /**
     * Gets the Classification Schema saved in the database with the id received as
     * parameter.
     * 
     * @param id
     *            The object's id that we want to get.
     * @return - The Classification Schema saved in the database with the id
     *         received as parameter. - Null if doesn't exist any Classification
     *         Schema with the id received as parameter
     */
    public static Classifier getClassifier(Long id) {
	return (Classifier) MediatorSchemas.get(id, Classifier.class);
    }

    /**
     * Get a classifier with the given name, in case that there are more then one
     * classifier with the same name throws a Exception, in case that there are no
     * classifier with the same name throw also a exception.
     * 
     * @param name
     *            Name to search in classifiers
     * @return Classifier with that name
     * @throws ValueException
     */
    public static Classifier getClassifierByName(String name) throws ValueException {
	List<Classifier> l = MediatorSchemas.listClassifiersByName(name);
	if (l.isEmpty()) {
	    throw new NotFoundException("Classifier with name [" + name + "] not found");
	} else if (l.size() > 1) {
	    throw new ValueException("More than one classifier with the same name: " + name);
	} else {
	    return l.get(0);
	}
    }

    /**
     * Save the Classification Schema received as parameter and all of its root
     * classifiers.
     * 
     * @param cs
     *            The Classification Schema that we want to save
     */
    public static void saveSchema(ClassificationSchema cs) {
	// MediatorGeneric.persist(cs);
	MediatorSchemas.save(cs);
    }

    /**
     * This method all the given classifier as Root classifier and update the schema
     * 
     * @param parent
     *            Schema where the classifier should be added
     * @param child
     *            Classifer to add
     * @throws IntegrityException
     */
    public static void addAndSaveRoot(ClassificationSchema parent, Classifier child) throws IntegrityException {
	parent.addRootClassifier(child);
	MediatorSchemas.update(parent);
    }

    /**
     * Given a {@link Classifier} and a {@link Collection} of
     * {@link RequirementPattern}, search all the patterns related to the given
     * {@link Classifier} and delete them as basicClassifer of that patterns. After
     * the delete for each pattern given as parameter add the internal as
     * basicClassifier. NOTE: only functions when the collection is != the null.
     * 
     * @param classifier
     *            {@link Classifier} to update
     * @param patterns
     *            Collection of {@link RequirementPattern} who has to be assigned
     * @throws SemanticallyIncorrectException
     *             Throw semanticallyIncorrectException when the classifer type is
     *             incorrect
     */
    public static void substituteRequirementPatternsFromInternalClassifier(Classifier classifier,
	    Collection<RequirementPattern> patterns) throws SemanticallyIncorrectException {
	logger.debug("Substituting pattern in classifier");
	classifier.clearPatterns();
	MediatorSchemas.update(classifier);
	classifier.addPatterns(patterns);
	MediatorSchemas.update(classifier);
    }

    public static void deleteInternalClassifiersFromClassifier(Classifier classifier)
	    throws SemanticallyIncorrectException {
	classifier.clearInternalsClassifiers();
	MediatorSchemas.update(classifier);
    }

    public static void delete(ClassificationSchema s)
	    throws HibernateException, IntegrityException, SemanticallyIncorrectException {
	MediatorSchemas.delete(s);
    }

    public static void updatePatternsInRecursive(Classifier ic) {
	for (Classifier internalClassifier : ic.getInternalClassifiers()) {
	    for (RequirementPattern pat : internalClassifier.getPatterns()) {
		MediatorSchemas.update(pat);
	    }
	    for (Classifier internals : internalClassifier.getInternalClassifiers()) {
		updatePatternsInRecursive(internals);
	    }
	}
    }

    /**
     * Given two classifiers A and B, copies all the not null values of the
     * classifiers B to A
     * 
     * @param old
     *            classifier A where to be copied
     * @param newInternal
     *            classifiers B from where the values should be copied
     * @throws IntegrityException
     * @throws SemanticallyIncorrectException
     */
    public static void updateInternalClassifier(Classifier old, Classifier newInternal)
	    throws IntegrityException, SemanticallyIncorrectException {
	// Check name to update
	if (newInternal.getName() != null)
	    old.setName(newInternal.getName());
	// Check description to update
	if (newInternal.getDescription() != null)
	    old.setDescription(newInternal.getDescription());
	// Check comments to update
	if (newInternal.getComments() != null)
	    old.setComments(newInternal.getComments());
	// Check sources to update
	if (newInternal.getSources() != null)
	    old.clearAndSetSources(newInternal.getSources());
	// Check classifiers to update
	if (newInternal.getInternalClassifiers() != null) {
	    old.clearInternalsClassifiers();
	    for (Classifier c : newInternal.getInternalClassifiers()) {
		MediatorGeneric.save(c);
		old.addClassifier(c);
	    }
	    GenericDataController.flush();
	    // MediatorGeneric.save(old.getInternalClassifiers());
	    // old.clearAndSetClassifiers(newInternal.getInternalClassifiers());
	}
	// Check patterns to update
	if (newInternal.getPatterns() != null)
	    old.clearAndSetPatterns(newInternal.getPatterns());
	MediatorGeneric.update(old);// Update the Internal
    }

    public static void deleteReferencedPatternOfTheSchema(ClassificationSchema cs)
	    throws HibernateException, IntegrityException, SemanticallyIncorrectException {
	MediatorSchemas.deleteReferencedPatternOfTheSchema(cs);
    }

    /**
     * Obtains from database the RequirementPattern whose name is received as
     * parameter, removes its basic classifier oldC and adds newC also as basic
     * classifier.
     * 
     * @param name
     *            The name of the RequirementPattern where the method has to add
     *            InternalClassifier newC and remove oldC.
     * @param newC
     *            The InternalClassifier that the method has to add to the
     *            RequirementPattern identified by name.
     * @param oldC
     *            The InternalClassifier that the method has to remove from the
     *            RequirementPattern identified by name.
     * @throws Exception
     *             if parameter name or newC is null or any problem occurs during
     *             database update process. Parameter oldC can be null to cover the
     *             case of pattern being classified in non-binded patterns.
     */
    public static void movePatternFromClassifiers(String name, Classifier newC, Classifier oldC) throws Exception {
	if (name == null || newC == null)
	    throw new NullPointerException("Move operation over null objects is not allowed");

	RequirementPattern p = (RequirementPattern) MediatorGeneric.get(name, RequirementPattern.class);
	oldC.removePattern(p);
	newC.addPattern(p);
	MediatorGeneric.update(p);

    }

}
