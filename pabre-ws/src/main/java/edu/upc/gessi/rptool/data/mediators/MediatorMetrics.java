package edu.upc.gessi.rptool.data.mediators;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import edu.upc.gessi.rptool.domain.metrics.DomainMetric;
import edu.upc.gessi.rptool.domain.metrics.DomainMetricValue;

/**
 * This class contains all specific operatiosn necessaries to operate over all
 * type of Metrics
 *
 */
@SuppressWarnings("rawtypes")
public final class MediatorMetrics extends MediatorGeneric {

    /**
     * Constructor has to be private so no one be able to create any instance
     */
    private MediatorMetrics() {
    }

    /**
     * Save the objects in the set received as parameter and after save the object
     * received as parameter.
     * 
     * @param m
     *            The object that we want to save
     * @param possibleValues
     *            The set of objects to save (in this case, this set corresponds to
     *            the possibles values of the domain metric that m represents)
     */
    public static void saveDomainMetric(Object m, Set possibleValues) {
	Session session = MediatorConnection.getCurrentSession();
	Iterator i = possibleValues.iterator();
	while (i.hasNext()) {
	    Object v = i.next();
	    session.save(v);
	}
	session.save(m);
	session.flush();
    }

    public static void updateDomainMetric(Object m, Set possibleValues) {
	Session session = MediatorConnection.getCurrentSession();
	Iterator i = possibleValues.iterator();
	while (i.hasNext()) {
	    Object v = i.next();
	    session.save(v);
	}
	session.update(m);
	session.flush();
    }

    /**
     * Remove the objects in the "oldValues" set received as parameter, save the
     * objects in the "newValues" set received as parameter, and after save the
     * object received as parameter.
     * 
     * @param m
     *            The object that we want to update
     * @param oldValues
     *            The set of objects to remove (in this case, this set corresponds
     *            to the old possible values before the update of the domain metric
     *            that m represents)
     * @param newValues
     *            The set of objects to save (in this case, this set corresponds to
     *            the possibles values of the domain metric that m represents)
     */
    public static void updateDomainMetric(Object m, List oldValues, Set newValues) {
	Session session = MediatorConnection.getCurrentSession();
	Iterator i = oldValues.iterator();
	while (i.hasNext()) {
	    Object v = i.next();
	    session.delete(v);
	}
	i = newValues.iterator();
	while (i.hasNext()) {
	    Object v = i.next();
	    session.save(v);
	}
	session.update(m);
	session.flush();
    }

    /**
     * Delete the objects in the set received as parameter and after delete the
     * object received as parameter.
     * 
     * @param m
     *            The object that we want to remove
     * @param possibleValues
     *            The set of objects to remove (in this case, this set corresponds
     *            to the possibles values of the domain metric that m represents)
     */
    public static void deleteDomainMetric(DomainMetric m, Set<DomainMetricValue> possibleValues) {
	m.setDefaultValue((DomainMetricValue) null);
	deleteDomainMetricValues(possibleValues);

	MediatorGeneric.delete(m);
	MediatorConnection.flush();
    }

    public static void deleteDomainMetricValues(Set<DomainMetricValue> oldValues) {
	Iterator i = oldValues.iterator();
	while (i.hasNext())
	    MediatorGeneric.delete(i.next());
	MediatorConnection.flush();

    }

}
