package edu.upc.gessi.rptool.data;

import java.util.List;

import org.hibernate.Session;

import edu.upc.gessi.rptool.data.mediators.MediatorConnection;
import edu.upc.gessi.rptool.data.mediators.MediatorGeneric;

/*
 * This class contains the controller for all generic 
 * operations necessaries to create and initialize the 
 * database, to connect with it, and operations to list, 
 * count, save, update and delete a generic object.
 */
@SuppressWarnings("rawtypes")
public class GenericDataController {

    /**
     * Constructor has to be protected to be visible only from its subclasses.
     */
    protected GenericDataController() {
    }

    /**
     * Reinitialize the database and get a session to connect with it. This method
     * is useful to create from zero a new database. NOTE: the execution of this
     * method clear all data saved in the database (if it exists).
     */
    public static void createSchema() {
	MediatorConnection.createSchema();
    }

    /**
     * This method initialize a session to connect with database.
     */
    public static void initializeDataBaseSession() {
	MediatorConnection.initialize();
    }

    /**
     * Return a list that contains all objects saved in database of the class
     * received as parameter.
     * 
     * @param obj
     *            The class of the objects that we want to get
     * @return List that contains all objects saved in database of the class
     *         received as parameter
     */
    public static <T extends Object> List list(T obj) {
	return MediatorGeneric.list(obj.getClass());
    }

    /**
     * Count the number of objects saved in database of the class received as
     * parameter.
     * 
     * @param obj
     *            The class of the objects that we want to count
     * @return The number of objects saved in database of the class received as
     *         parameter
     */
    public static int count(Class obj) {
	return MediatorGeneric.count(obj);
    }

    /**
     * Save in database the object received as parameter.
     * 
     * @param o
     *            The object that we want to save.
     */
    public static void save(Object o) {
	MediatorGeneric.save(o);
    }

    /**
     * Save in database the object received as parameter, in a given Session
     * 
     * @param o
     *            The object that we want to save
     * @param s
     *            The session where we want to save
     */
    public static void save(Object o, Session s) {
	MediatorGeneric.saveInSession(o, s);
    }

    /**
     * Update in database the object received as parameter.
     * 
     * @param o
     *            The object that we want to update.
     */
    public static void update(Object o) {
	MediatorGeneric.update(o);
    }

    /**
     * Update in database the object received as parameter with the session received
     * as parameter.
     * 
     * @param o
     *            The object that we want to update.
     * @param s
     *            The session in which update the object.
     */
    public static void update(Object o, Session s) {
	MediatorGeneric.updateInSession(o, s);
    }

    /**
     * Delete from database the object received as parameter.
     * 
     * @param o
     *            The object that we want to remove.
     */
    public static void delete(Object o) {
	MediatorGeneric.delete(o);
    }

    /**
     * Delete from database the object received as parameter, with the given session
     * 
     * @param o
     *            The object that we want to remove.
     * @param s
     *            The session where we want to save
     */
    public static void delete(Object o, Session s) {
	MediatorGeneric.deleteInSession(o, s);
    }

    /**
     * {@inheritDoc MediatorConnection#flush()}
     */
    public static void flush() {
	MediatorConnection.flush();
    }

}
