package edu.upc.gessi.rptool.data.mediators;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.utilities.Util;

/*
 * This class contains all specific operations 
 * necessaries to save, update and delete in 
 * database schemas.
 */
@SuppressWarnings({ "rawtypes" })
public final class MediatorSchemas extends MediatorGeneric {

    /**
     * Constructor has to be private so no one be able to create any instance
     */
    private MediatorSchemas() {
    }

    /**
     * Save the objects in the set received as parameter and after save the object
     * received as parameter.
     * 
     * @param m
     *            The object that we want to save
     * @param roots
     *            The set of objects to save (in this case, this set corresponds to
     *            the root classifiers of the classification schema that m
     *            represents)
     */
    public static void save(Set<Classifier> roots) {
	for (Classifier classifier : roots) {
	    save(classifier);
	}
    }

    @SuppressWarnings("unchecked")
    public static List listClassifiersByName(String name) {
	Session session = MediatorConnection.getCurrentSession();
	List result = session.createCriteria(Classifier.class).add(Restrictions.eq("name", name)).list();
	return Util.uniqueList(result);
    }

    /**
     * @deprecated ()
     * Get the list of requirement patterns that there are in the classifier with
     * the id received as parameter.
     * 
     * <strong>DEPRECATED</strong>: Use the set of pattern inside the classifier
     * 
     * @param id
     *            The id of the classifier that we want to get its requirement
     *            pattern.
     * @param session
     *            The session in which we want to do the query.
     * @return The list of requirement patterns that there are in the classifier
     *         with the id received as parameter.
     */
    @Deprecated
    public static List getPatternsFromBasicClassifier(Long id) {
	Session session = MediatorConnection.getCurrentSession();
	return session
		.createQuery("select p from RequirementPattern as p left join p.classifiers as b where b.id = " + id)
		.list();
    }

    /**
     * Delete the given Classification schma
     * 
     * @param s
     *            Schema to be deleted
     * @throws HibernateException
     * @throws IntegrityException
     * @throws SemanticallyIncorrectException
     */
    public static void delete(ClassificationSchema s)
	    throws HibernateException, IntegrityException, SemanticallyIncorrectException {
	MediatorSchemas.deleteReferencedPatternOfTheSchema(s);
	MediatorGeneric.delete(s);
    }

    /**
     * Delete all the classifiers inside the schema
     * 
     * @param schema
     *            Schema to be deleted
     * @throws HibernateException
     * @throws IntegrityException
     * @throws SemanticallyIncorrectException
     */
    public static void deleteReferencedPatternOfTheSchema(ClassificationSchema schema)
	    throws HibernateException, SemanticallyIncorrectException {
	for (Classifier root : schema.getRootClassifiers()) {
	    deletePatternsRecursive(root);
	}
	MediatorConnection.flush();
    }

    /**
     * Delete classifier
     * 
     * @param c
     *            {@link Classifier} to be deleted
     * @throws SemanticallyIncorrectException
     */
    public static void deleteClassifierPatterns(Classifier c) throws SemanticallyIncorrectException {
	c.clearPatterns();
	update(c);
    }

    /**
     * Delete classifier
     * 
     * @param c
     *            Classifier where to delete the patterns
     * @throws SemanticallyIncorrectException
     */
    public static void deletePatternsRecursive(Classifier c) throws SemanticallyIncorrectException {
	for (Classifier internal : c.getInternalClassifiers()) {
	    deleteClassifierPatterns(internal);
	    deletePatternsRecursive(internal);
	}
    }

}
