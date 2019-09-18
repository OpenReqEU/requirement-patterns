package edu.upc.gessi.rptool.data.mediators;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.rest.utilities.Util;

/**
 * This class constains all specefic operations necessaries to save, update and
 * delete in database all the requirement patterns
 * 
 * @author Awais Iqbal
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class MediatorPatterns extends MediatorGeneric {

    /**
     * Gets the String attribute name saved in the database with the identifier
     * received as parameter from the class received as parameter. NOTE: It is
     * important that the class received as parameter has the "attribute" name;
     * otherwise, an error will occur.
     * 
     * @param id
     *            The object's identifier that we want to get.
     * @param c
     *            The object's class that we want to get.
     * @return - The String name in the database with identifier received as
     *         parameter from the class received as parameter. - Null if doesn't
     *         exist any object in class with the identifier received as parameter
     */
    public static String getPatternName(long id, Class c) {
	Session session = MediatorConnection.getCurrentSession();
	Criteria aux = session.createCriteria(c).add(Restrictions.idEq(id));
	return (String) aux.setProjection(Projections.property("name")).uniqueResult();
    }

    /**
     * Get the name of the given pattern ID
     * 
     * @param id
     *            ID of the pattern
     * @param c
     *            Class to be returned
     * @return Name of the pattern
     */
    public static String getNamePattern(long id, Class c) {
	Session session = MediatorConnection.getCurrentSession();
	String name = "";
	name = ((RequirementPatternVersion) session.createCriteria(c).add(Restrictions.idEq(id)).uniqueResult())
		.getRequirementPattern().getName();
	return name;
    }

    /**
     * Loads from database the object whose class is received as parameter
     * identified by the parameter id, using the session received as parameter.
     * 
     * @param id
     *            Identifier of the object the method has to retrieve.
     * @param c
     *            Class of the object the method has to retrieve.
     * 
     * @return The object in database identified by parameter id and whose class is
     *         c
     */
    public static Object getPattern(long id, Class c) {
	if (c == null)
	    throw new NullPointerException("Load operation over null objects is not allowed");
	Session session = MediatorConnection.getCurrentSession();
	return session.load(c, id);
    }

    /**
     * Get the list of requirement patterns also with their parents and editable
     * info.
     * 
     * @return The list of requirement patterns and their parents.
     */
    public static List listPatternNamesWithParentsAndEditable() {
	Session session = MediatorConnection.getCurrentSession();
	List names = session
		.createQuery(
			"select p.name, b.id, p.editable from RequirementPattern as p left join p.classifiers as b")
		.list();
	return Util.uniqueList(names);
    }

    /**
     * Get the list of requirement patterns also with their parents and editable
     * info.
     * 
     * @return The list of requirement patterns and their parents.
     */
    public static List listPatternsWithoutClassifiers() {
	Session session = MediatorConnection.getCurrentSession();
	Criteria c = session.createCriteria(RequirementPattern.class);
	c.add(Restrictions.isEmpty("classifiers"));
	return Util.uniqueList(c.list());
    }

    public static List listPatternWithGivenKeyword(String keyword) {
	Session session = MediatorConnection.getCurrentSession();
		return session.createQuery("select p from RequirementPattern as p " + "left join p.versions as v "
			+ "left join v.keywords as k " + "where (lower(k.name) like :keyword or lower(p.name) like :keyword) ")
			.setParameter("keyword", "%" + keyword.toLowerCase() + "%").list();
    }

    /**
     * Get the list of the id, name, editable, and last version available info.
     * 
     * @return The list with id, name, editable, and last version available info.
     */
    public static List listPatternIdWithNameEditableAvailable() {
	Session session = MediatorConnection.getCurrentSession();
		return session.createQuery("select p.id, p.name, p.editable, v.available "
			+ "from RequirementPattern as p left join p.versions as v "
			+ "where v.versionDate = (select max(v2.versionDate) from p.versions v2)").list();
    }

    /**
     * Get the list of the id, name, editable, last version available info and the
     * id of the classifier where the requirement pattern is located
     * 
     * @return The list with id, name, editable, and last version available info and
     *         their parents.
     */
    public static List listPatternIdWithNameEditableAvailableAndParents() {
	Session session = MediatorConnection.getCurrentSession();
	List names = session.createQuery("select p.id, p.name, p.editable, v.available, b.id "
		+ "from RequirementPattern as p left join p.versions as v left join p.classifiers as b "
		+ "where v.versionDate = (select max(v2.versionDate) from p.versions v2)").list();
	names = Util.uniqueList(names);
	return names;
    }

    /**
	 * @deprecated ()
     * Get the Requirement Patterns with a specific internal classifier
     * 
     * @param id
     *            The id of the classifier that we want to get its requirement
     *            patterns.
     * @return The list of Requirement Patterns with a specific internal classifier
     */
    @Deprecated
    public static List listPatternsWithThisClassifier(Long id) {
	Session session = MediatorConnection.getCurrentSession();
	List patterns = session
		.createQuery("select p from RequirementPattern as p left join p.classifiers as b where b.id = " + id)
		.list();
	return Util.uniqueList(patterns);
    }

    /**
     * List all the dependencies of the RequirementPattern whose identifier is
     * received as parameter.
     * 
     * @param l
     *            The identifier of the RequirementPattern whose dependencies will
     *            be returned.
     * @param c
     *            The object's class that we want to get.
     * @return The list of dependencies of the RequirementPattern identified by the
     *         parameter.
     */
    public static List listPatternDependencies(long l, Class c) {
	Session session = MediatorConnection.getCurrentSession();
	List dependencies = session.createCriteria(c).add(Restrictions.eq("idFirst", l)).list();
	return Util.uniqueList(dependencies);
    }

    /**
	 * @deprecated ()
     * List all the versions of the RequirementPattern whose identifier is received
     * as parameter.
     * 
     * @param l
     *            The identifier of the RequirementPattern whose versions will be
     *            returned.
     * @param c
     *            The object's class that we want to get.
     * @return The list of versions of the RequirementPattern identified by the
     *         parameter.
     */
    @Deprecated
    public static List listPatternVersions(RequirementPattern l, Class c) {
	Session session = MediatorConnection.getCurrentSession();
	List versions = session.createCriteria(c).add(Restrictions.eq("requirementPattern", l))
		.addOrder(Order.desc("versionDate")).list();
	return Util.uniqueList(versions);
    }

    /**
	 * @deprecated ()
     * Loads from database the RequirementForm associated with the FixedPart whose
     * id is received as parameter.
     * 
     * @param fixedPartId
     *            Identifier of the FixedPart associated with the RequirementForm
     *            searched
     * @return RequirementForm associated with the FixedPart whose id is received as
     *         parameter
     */
    @Deprecated
    public static Object getRequirementFormFromFixedPart(long fixedPartId) {
	Session session = MediatorConnection.getCurrentSession();
	String query = new StringBuilder("select rf").append(" from RequirementForm as rf")
		.append(" where rf.fixedPart.id = ").append(fixedPartId).toString();
		return session.createQuery(query).uniqueResult();
    }

    /**
	 * @deprecated ()
     * Loads from database the RequirementForm id associated with the ExtendedPart
     * whose id is received as parameter.
     * 
     * @param extendedPartId
     *            Identifier of the ExtendedPart associated with the RequirementForm
     *            searched
     * @return long RequirementForm identifier associated with the ExtendedPart
     *         whose id is received as parameter
     */
    @Deprecated
    public static long getRequirementFormFromExtendedPart(long extendedPartId) {
	Session session = MediatorConnection.getCurrentSession();
	String query = new StringBuilder("select extended_pattern").append(" from extended_pattern")
		.append(" where id = ").append(extendedPartId).toString();
	BigInteger formId = (BigInteger) session.createSQLQuery(query).uniqueResult();
	return formId.longValue();
    }

    /**
     * Loads from database the RequirementPattern id associated with the
     * RequirementForm whose id is received as parameter.
     * 
     * @param reqFormId
     *            Identifier of the RequirementForm associated with the
     *            RequirementPattern searched
     * @return long RequirementPattern identifier associated with the
     *         RequirementForm whose id is received as parameter
     */
    @Deprecated
    public static long getRequirementPatternFromRequirementForm(long reqFormId) {
	Session session = MediatorConnection.getCurrentSession();
	String query = new StringBuilder("select requirement_pattern").append(" from requirement_form")
		.append(" where id = ").append(reqFormId).toString();
	BigInteger patternId = (BigInteger) session.createSQLQuery(query).uniqueResult();
	return patternId.longValue();
    }

}
