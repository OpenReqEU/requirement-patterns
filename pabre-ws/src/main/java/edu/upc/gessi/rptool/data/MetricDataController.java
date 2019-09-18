package edu.upc.gessi.rptool.data;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import edu.upc.gessi.rptool.data.mediators.MediatorGeneric;
import edu.upc.gessi.rptool.data.mediators.MediatorMetrics;
import edu.upc.gessi.rptool.domain.metrics.DomainMetric;
import edu.upc.gessi.rptool.domain.metrics.DomainMetricValue;
import edu.upc.gessi.rptool.domain.metrics.FloatMetric;
import edu.upc.gessi.rptool.domain.metrics.IntegerMetric;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.SetMetric;
import edu.upc.gessi.rptool.domain.metrics.SimpleMetric;
import edu.upc.gessi.rptool.domain.metrics.StringMetric;
import edu.upc.gessi.rptool.domain.metrics.TimePointMetric;
import edu.upc.gessi.rptool.domain.metrics.Type;

/**
 * This class contains the controller for specific operations necessaries for
 * the all type of Metrics.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class MetricDataController extends GenericDataController {

    /* There are the lists that contains all metrics
     when a metric is created, modified or removed
     the correspond list has to be updated properly*/
    private static List<StringMetric> stringMetrics = null;
    private static List<IntegerMetric> integerMetrics = null;
    private static List<FloatMetric> floatMetrics = null;
    private static List<TimePointMetric> timePointMetrics = null;
    private static List<DomainMetric> domainMetrics = null;
    private static List<SetMetric> setMetrics = null;
    private static List<Metric> metrics = null;

    /**
     * Constructor has to be private so no one be able to create any instance
     */
    private MetricDataController() {
    }

    /**
     * This class is useful to order a list of metrics by their name.
     */
    public static class MetricComparator implements Comparator<Metric> {
	private static Collator collator = Collator.getInstance();

	@Override
	public int compare(Metric a, Metric b) {
	    if (a == null || b == null) throw new NullPointerException("Comparison between null objects is not allowed");

	    return collator.compare(a.getName(), b.getName());
	}
    }

    /**
     * List all the String Metrics saved in the database
     * 
     * @return The list with all String Metrics saved in the database
     */
    public static List<StringMetric> listStringMetrics() {
	// Get the list with all metric of this type
	if (MetricDataController.getStringMetrics() == null) {
	    // Set the correspond list to this type of metrics
	    MetricDataController.setStringMetrics(MediatorGeneric.list(StringMetric.class));
	    Collections.sort(MetricDataController.getStringMetrics(), new MetricComparator());
	}

	return MetricDataController.getStringMetrics();
    }

    /**
     * List all the Metrics saved in the database
     * 
     * @return The list with all Metrics saved in the database
     */
    public static List<Metric> listMetrics() {
	// Get the list with all metric of this type
	if (MetricDataController.getIntegerMetrics() == null) {
	    // Set the correspond list to this type of metrics
	    MetricDataController.setMetrics(MediatorGeneric.list(Metric.class));
	    Collections.sort(MediatorGeneric.list(Metric.class), new MetricComparator());
	}

	return MetricDataController.getMetrics();
    }

    /**
     * List all the Integer Metrics saved in the database
     * 
     * @return The list with all Integer Metrics saved in the database
     */
    public static List<IntegerMetric> listIntegerMetrics() {
	// Get the list with all metric of this type
	if (MetricDataController.getIntegerMetrics() == null) {
	    // Set the correspond list to this type of metrics
	    MetricDataController.setIntegerMetrics(MediatorGeneric.list(IntegerMetric.class));
	    Collections.sort(MetricDataController.getIntegerMetrics(), new MetricComparator());
	}
	return MetricDataController.getIntegerMetrics();
    }

    /**
     * List all the Float Metrics saved in the database
     * 
     * @return The list with all Float Metrics saved in the database
     */
    public static List<FloatMetric> listFloatMetrics() {
	// Get the list with all metric of this type
	if (MetricDataController.getFloatMetrics() == null) {
	    // Set the correspond list to this type of metrics
	    MetricDataController.setFloatMetrics(MediatorGeneric.list(FloatMetric.class));
	    Collections.sort(MetricDataController.getFloatMetrics(), new MetricComparator());
	}
	return MetricDataController.getFloatMetrics();
    }

    /**
     * List all the TimePoint Metrics saved in the database
     * 
     * @return The list with all TimePoint Metrics saved in the database
     */
    public static List<TimePointMetric> listTimePointMetrics() {
	// Get the list with all metric of this type
	if (MetricDataController.getTimePointMetrics() == null) {
	    // Set the correspond list to this type of metrics
	    MetricDataController.setTimePointMetrics(MediatorGeneric.list(TimePointMetric.class));
	    Collections.sort(MetricDataController.getTimePointMetrics(), new MetricComparator());
	}
	return MetricDataController.getTimePointMetrics();
    }

    /**
     * List all the Domain Metrics saved in the database
     * 
     * @return The list with all Domain Metrics saved in the database
     */
    public static List<DomainMetric> listDomainMetrics() {
	// Get the list with all metric of this type
	if (MetricDataController.getDomainMetrics() == null) {
	    // Set the correspond list to this type of metrics
	    MetricDataController.setDomainMetrics(MediatorGeneric.list(DomainMetric.class));
	    Collections.sort(MetricDataController.getDomainMetrics(), new MetricComparator());
	}
	return MetricDataController.getDomainMetrics();
    }

    /**
     * List all the Set Metrics saved in the database
     * 
     * @return The list with all Set Metrics saved in the database
     */
    public static List<SetMetric> listSetMetrics() {
	// Get the list with all metric of this type
	if (MetricDataController.getSetMetrics() == null) {
	    // Set the correspond list to this type of metrics
	    MetricDataController.setSetMetrics(MediatorGeneric.list(SetMetric.class));
	    Collections.sort(MetricDataController.getSetMetrics(), new MetricComparator());
	}
	return MetricDataController.getSetMetrics();
    }

    /**
     * Count the number of metrics saved in database.
     * 
     * @return The number of metrics saved in database.
     */
    public static int countMetrics() {
	return MediatorGeneric.count(Metric.class);
    }

    /**
     * Save in database the String Metric received as parameter and insert it in the
     * list of stringMetrics
     * 
     * @param m
     *            The String metric that we want to save.
     */
    public static void save(StringMetric m) {
	MediatorGeneric.save(m);
	replaceObjects(m, MetricDataController.getStringMetrics());
    }

    /**
     * Save in database the Float Metric received as parameter and insert it in the
     * list of FloatMetrics
     * 
     * @param m
     *            The Float metric that we want to save.
     */
    public static void save(FloatMetric m) {
	MediatorGeneric.save(m);
	replaceObjects(m, MetricDataController.getFloatMetrics());
    }

    /**
     * Save in database the Integer Metric received as parameter and insert it in
     * the list of IntegerMetrics
     * 
     * @param m
     *            The Integer metric that we want to save.
     */
    public static void save(IntegerMetric m) {
	MediatorGeneric.save(m);
	replaceObjects(m, MetricDataController.getIntegerMetrics());
    }

    /**
     * Save in database the TimePoint Metric received as parameter and insert it in
     * the list of TimePointMetrics
     * 
     * @param m
     *            The TimePoint metric that we want to save.
     */
    public static void save(TimePointMetric m) {
	MediatorGeneric.save(m);
	replaceObjects(m, MetricDataController.getTimePointMetrics());
    }

    /**
     * Save in database the Domain Metric received as parameter and insert it in the
     * list of DomainMetrics
     * 
     * @param m
     *            The Domain metric that we want to save.
     */
    public static void save(DomainMetric m) {
	MediatorMetrics.saveDomainMetric(m, m.getPossibleValues());
	replaceObjects(m, MetricDataController.getDomainMetrics());
    }

    /**
     * Save in database the Set Metric received as parameter and insert it in the
     * list of SetMetrics
     * 
     * @param m
     *            The Set metric that we want to save.
     */
    public static void save(SetMetric m) {
	MediatorGeneric.save(m);
	replaceObjects(m, MetricDataController.getSetMetrics());
    }

    /**
     * Update in database the String Metric received as parameter and replace it in
     * the list of stringMetrics
     * 
     * @param m
     *            The string metric that we want to update.
     */
    public static void update(StringMetric m) {
	MediatorGeneric.update(m);
	replaceObjects(m, MetricDataController.getStringMetrics());
    }

    /**
     * Update in database the Float Metric received as parameter and replace it in
     * the list of floatMetrics
     * 
     * @param m
     *            The Float metric that we want to update.
     */
    public static void update(FloatMetric m) {
	MediatorGeneric.update(m);
	replaceObjects(m, MetricDataController.getFloatMetrics());
    }

    /**
     * Update in database the Integer Metric received as parameter and replace it in
     * the list of integerMetrics
     * 
     * @param m
     *            The Integer metric that we want to update.
     */
    public static void update(IntegerMetric m) {
	MediatorGeneric.update(m);
	replaceObjects(m, MetricDataController.getIntegerMetrics());
    }

    /**
     * Update in database the TimePoint Metric received as parameter and replace it
     * in the list of timePointMetrics
     * 
     * @param m
     *            The TimePoint metric that we want to update.
     */
    public static void update(TimePointMetric m) {
	MediatorGeneric.update(m);
	replaceObjects(m, MetricDataController.getTimePointMetrics());
    }

    /**
     * Update in database the Domain Metric received as parameter and replace it in
     * the list of domainMetrics.
     * 
     * @param m
     *            The Domain metric that we want to update.
     * @param oldValues
     *            the old values
     * @param oldValues
     *            This set corresponds to the old possible values before the update
     *            of the domain metric received as parameter
     */
    public static void update(DomainMetric m, List oldValues) {
	MediatorMetrics.updateDomainMetric(m, oldValues, m.getPossibleValues());
	replaceObjects(m, MetricDataController.getDomainMetrics());
    }

    /**
     * Update in database the Set Metric received as parameter and replace it in the
     * list of setMetrics
     * 
     * @param m
     *            The Set metric that we want to update.
     */
    public static void update(SetMetric m) {
	MediatorGeneric.update(m);
	replaceObjects(m, MetricDataController.getSetMetrics());
    }

    /**
     * Delete the metric received as parameter from database and remove it from the
     * list of domainMetrics. The method call to the corresponding method to delete
     * the metric depending on the type of metric (integer, string, float...)
     * 
     * @param m
     *            The metric that we want to remove.
     */
    public static void delete(Metric m) {
	if (m.getType() == Type.STRING) {
	    removeObjects(m, MetricDataController.getStringMetrics());
	} else if (m.getType() == Type.INTEGER) {
	    removeObjects(m, MetricDataController.getIntegerMetrics());
	} else if (m.getType() == Type.FLOAT) {
	    removeObjects(m, MetricDataController.getFloatMetrics());
	} else if (m.getType() == Type.TIME) {
	    removeObjects(m, MetricDataController.getTimePointMetrics());
	} else if (m.getType() == Type.SET) {
	    removeObjects(m, MetricDataController.getSetMetrics());
	} else if (m.getType() == Type.DOMAIN) {
	    removeObjects(m, MetricDataController.getDomainMetrics());
	}
	MediatorGeneric.delete(m);

    }

    /**
     * Replace the object received as parameter in the list received as parameter.
     * 
     * @param m
     *            The Metric to be replaced
     * @param cache
     *            The list in which we want to replace the Metric.
     */
    private static void replaceObjects(Object m, List cache) {
	if (m == null || cache == null) {
	    return;
	}
	cache.remove(m);
	cache.add(m);
    }

    /**
     * Remove the object received as parameter in the list received as parameter.
     * 
     * @param m
     *            The object to be removed
     * @param cache
     *            The list in which we want to remove the object.
     */
    private static void removeObjects(Object m, List cache) {
	if (m != null && cache != null)
	    cache.remove(m);
    }

    public static SimpleMetric getSimpleMetric(String name) {
	if (name == null)
	    return null;
	return (SimpleMetric) MediatorGeneric.get(name, SimpleMetric.class);
    }

    public static SetMetric getSetMetric(String name) {
	if (name == null)
	    return null;
	return (SetMetric) MediatorGeneric.get(name, SetMetric.class);
    }

    public static Metric getMetric(String name) {
	return (Metric) MediatorGeneric.get(name, Metric.class);
    }

    public static Metric getMetric(long id) {
	return (Metric) MediatorGeneric.get(id, Metric.class);
    }

    public static Metric getMetric(String name, Session session) {
	return (Metric) MediatorGeneric.get(name, Metric.class, session);

    }

    public static Metric getMetric(long id, Session session) {
	return (Metric) MediatorGeneric.get(id, Metric.class, session);

    }

    public static void deleteDomainMetricValues(Set<DomainMetricValue> oldValues) {
	MediatorMetrics.deleteDomainMetricValues(oldValues);
    }

    public static void updateDomainMetric(Metric m, Set<DomainMetricValue> values) {
	MediatorMetrics.updateDomainMetric(m, values);

    }

    /*
     * Getters and setters for lists of metrics
     */

    private static List<StringMetric> getStringMetrics() {
	return stringMetrics;
    }

    private static void setStringMetrics(List<StringMetric> stringMetrics) {
	MetricDataController.stringMetrics = stringMetrics;
    }

    private static List<IntegerMetric> getIntegerMetrics() {
	return integerMetrics;
    }

    private static List<Metric> getMetrics() {
	return metrics;
    }

    private static void setIntegerMetrics(List<IntegerMetric> integerMetrics) {
	MetricDataController.integerMetrics = integerMetrics;
    }

    private static void setMetrics(List<Metric> metrics) {
	MetricDataController.metrics = metrics;
    }

    private static List<FloatMetric> getFloatMetrics() {
	return floatMetrics;
    }

    private static void setFloatMetrics(List<FloatMetric> floatMetrics) {
	MetricDataController.floatMetrics = floatMetrics;
    }

    private static List<TimePointMetric> getTimePointMetrics() {
	return timePointMetrics;
    }

    private static void setTimePointMetrics(List<TimePointMetric> timePointMetrics) {
	MetricDataController.timePointMetrics = timePointMetrics;
    }

    private static List<DomainMetric> getDomainMetrics() {
	return domainMetrics;
    }

    private static void setDomainMetrics(List<DomainMetric> domainMetrics) {
	MetricDataController.domainMetrics = domainMetrics;
    }

    private static List<SetMetric> getSetMetrics() {
	return setMetrics;
    }

    private static void setSetMetrics(List<SetMetric> setMetrics) {
	MetricDataController.setMetrics = setMetrics;
    }

}
