package edu.upc.gessi.rptool.data.mediators;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.TransientObjectException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import edu.upc.gessi.rptool.rest.utilities.Util;

/**
 * This class contains all operations to get, list, count, save, update, delete
 * generic object
 * 
 * @author Awais Iqbal
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class MediatorGeneric {

    static final Logger logger = Logger.getLogger(MediatorGeneric.class.getName());

    /**
     * Gets the object saved in the database with the id received as parameter from
     * the class received as parameter.
     * 
     * @param id
     *            The object's id that we want to get.
     * @param c
     *            The object's class that we want to get.
     * @return - The object saved in the database with the id received as parameter
     *         from the class received as parameter. - Null if doesn't exist any
     *         object in class with the id received as parameter
     */
    public static Object get(long id, Class c) {
	Session session = MediatorConnection.getCurrentSession();
	return get(id, c, session);
    }

    /**
     * Gets the object saved in the database with the id received as parameter from
     * the class received as parameter.
     * 
     * @param id
     *            The object's id that we want to get.
     * @param c
     *            The object's class that we want to get.
     * @param session
     *            The session in which we want to retrieve the object.
     * @return The object saved in the database with the id received as parameter
     *         from the class received as parameter. - Null if doesn't exist any
     *         object in class with the id received as parameter
     */
    public static Object get(long id, Class c, Session session) {
	return session.createCriteria(c).add(Restrictions.idEq(id)).uniqueResult();
    }

    /**
     * Gets the object saved in the database with the name received as parameter
     * from the class received as parameter. NOTE: It is important that the class
     * received as parameter has the "attribute" name; otherwise, an error will
     * occur.
     * 
     * @param name
     *            The object's name that we want to get.
     * @param c
     *            The object's class that we want to get.
     * @return - The object saved in the database with the name received as
     *         parameter from the class received as parameter. - Null if doesn't
     *         exist any object in class with the name received as parameter
     */
    public static Object get(String name, Class c) {
	Session session = MediatorConnection.getCurrentSession();
	return get(name, c, session);
    }

    /**
     * Gets the object saved in the database with the name received as parameter
     * from the class received as parameter. NOTE: It is important that the class
     * received as parameter has the "attribute" name; otherwise, an error will
     * occur.
     * 
     * @param name
     *            The object's name that we want to get.
     * @param c
     *            The object's class that we want to get.
     * @param session
     *            The session in which we want to retrieve the object.
     * @return - The object saved in the database with the name received as
     *         parameter from the class received as parameter. - Null if doesn't
     *         exist any object in class with the name received as parameter
     */
    public static Object get(String name, Class c, Session session) {
	return get("name", name, c, session);
    }

    /**
     * Gets the object saved in the database with the name of the attribute as
     * parameter from the class received as parameter and get only with the value
     * given as parameter. NOTE: It is important that the class received as
     * parameter has the "attribute"; otherwise, an error will occur
     * 
     * @param attribute
     *            The name of the attribute to filter
     * @param value
     *            The object's value that we want to get.
     * @param c
     *            The object's class that we want to get.
     * @return The object saved in the database with the name received as parameter
     *         from the class received as parameter. - Null if doesn't exist any
     *         object in class with the name received as parameter
     */
    public static Object get(String attribute, String value, Class c) {
	Session session = MediatorConnection.getCurrentSession();
	return get(attribute, value, c, session);
    }

    /**
     * Gets the object saved in the database with the name of the attribute as
     * parameter from the class received as parameter and get only with the value
     * given as parameter. NOTE: It is important that the class received as
     * parameter has the "attribute"; otherwise, an error will occur
     * 
     * @param attribute
     *            The name of the attribute to filter
     * @param value
     *            The object's value that we want to get.
     * @param c
     *            The object's class that we want to get.
     * @param session
     *            The session in which we want to retrieve the object.
     * @return The object saved in the database with the name received as parameter
     *         from the class received as parameter. - Null if doesn't exist any
     *         object in class with the name received as parameter
     */
    public static Object get(String attribute, String value, Class c, Session session) {
	return session.createCriteria(c).add(Restrictions.eq(attribute, value)).uniqueResult();
    }

    /**
     * Return a list that contains all objects saved in database of the class
     * received as parameter. The method launch an exception if any Hibernate error
     * has occurred.
     * 
     * @param obj
     *            The class of the objects that we want to get
     * @return List that contains all objects saved in database of the class
     *         received as parameter
     */
    public static List list(Class obj) {
	Session session = MediatorConnection.getCurrentSession();
	List result = session.createCriteria(obj).list();
	result = Util.uniqueList(result);
	session.flush();
	return result;
    }

    /**
     * List names of the all objects saved in database of the class received as
     * parameter. NOTE: It is important that the class received as parameter has the
     * "attribute" name; otherwise, an error will occur.
     * 
     * @param c
     *            The class that we want to get all names.
     * @return The list of the attribute "name" of the all objects saved in database
     *         of the class received as parameter.
     */
    public static List listNames(Class c) {
	Session session = MediatorConnection.getCurrentSession();
	return session.createCriteria(c).setProjection(Projections.property("name")).list();
    }

    /**
     * Count the number of objects saved in database of the class received as
     * parameter. The method launch an exception if any Hibernate error has
     * occurred.
     * 
     * @param obj
     *            The class of the objects that we want to count
     * @return The number of objects saved in database of the class received as
     *         parameter
     */
    public static int count(Class obj) {
	Session session = MediatorConnection.getCurrentSession();
	int result = ((Long) session.createCriteria(obj).setProjection(Projections.rowCount()).uniqueResult())
		.intValue();
	session.flush();
	return result;
    }

    /**
     * Update in database the object received as parameter. The method launch an
     * exception if any Hibernate error has occurred.
     * 
     * @param obj
     *            The object that we want to update.
     */
    public static void update(Object obj) {
	updateInSession(obj, MediatorConnection.getCurrentSession());
    }

    /**
     * Update in session received as parameter the object received as parameter.
     * 
     * @param obj
     *            The object that we want to update.
     * @param session
     *            The session in which we want to update the object.
     */
    public static void updateInSession(Object obj, Session session) {
	try {
	    session.update(obj);
	} catch (HibernateException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Delete from database the object received as parameter. The method launch an
     * exception if any Hibernate error has occurred.
     * 
     * @param obj
     *            The object that we want to remove.
     */
    public static void delete(Object obj) {
	Session s = MediatorConnection.getCurrentSession();
	deleteInSession(obj, s);
    }

    /**
     * Delete in Session received as parameter the object received as parameter.
     * 
     * @param obj
     *            The object that we want to update.
     * @param session
     *            The Session in which we want to delete the object.
     */
    public static void deleteInSession(Object obj, Session session) {
	try {
	    session.delete(obj);
	    // session.flush();//Makes many time row deleted or updated
	} catch (HibernateException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Save in database the object received as parameter. The method launch an
     * exception if any Hibernate error has occurred.
     * 
     * @param obj
     *            The object that we want to save.
     */
    public static void save(Object obj) {
	saveInSession(obj, MediatorConnection.getCurrentSession());
    }

    /**
     * Save in session received as parameter the object received as parameter.
     * 
     * @param obj
     *            The object that we want to update.
     * @param session
     *            The session in which we want to update the object.
     */
    public static void saveInSession(Object obj, Session session) {
	try {
	    session.save(obj);
	    session.flush();
	} catch (HibernateException e) {
	    if (!(e instanceof TransientObjectException)) { // Avoid throwing Transient object exception to the user
		if (e.getMessage().contains(IdGenerator.IDEXCEPTIONSTRING)) {// Rollback when is ID generation fail
		    MediatorConnection.checkSessionAndRollback(e, session);
		}
		throw e;
	    }
	}
    }

    /**
     * {@inheritDoc Session#saveOrUpdate(Object)}
     */
    public static void saveOrUpdate(Object obj) {
	saveOrUpdateSession(obj, MediatorConnection.getCurrentSession());
    }

    /**
     * {@inheritDoc Session#saveOrUpdate(Object)}
     */
    public static void saveOrUpdateSession(Object obj, Session session) {
	session.saveOrUpdate(obj);
    }

    /**
     * Make a transient instance persistent. This operation cascades to associated
     * instances if the association is mapped with <tt>cascade="persist"</tt>.<br>
     * <br>
     * The semantics of this method are defined by JSR-220.
     *
     * @param object
     *            a transient instance to be made persistent
     */
    public static void persist(Object obj) {
	MediatorConnection.getCurrentSession().persist(obj);
    }

    /**
     * {@inheritDoc Session#merge(Object)}
     */
    public static void merge(Object obj) {
	obj = MediatorConnection.getCurrentSession().merge(obj);
    }

    /**
     * Given a id, check if exists any {@link Object} in the database with the given
     * ID
     * 
     * @param id
     *            The id to search in database
     * @return true if exists any object otherwise returns false
     */
    public static boolean checkIfExists(long id) {
	Object o = MediatorGeneric.get(id, Object.class);
	return o != null;
    }
}
