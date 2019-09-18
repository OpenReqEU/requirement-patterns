package edu.upc.gessi.rptool.domain.metrics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.PossibleValueException;
import edu.upc.gessi.rptool.exceptions.ValueException;

@Entity
@Table(name = "DOMAIN_METRIC")
public class DomainMetric extends SimpleMetric {

    /*
     * ATTRIBUTES
     */

    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinColumn(name = "DOMAIN_ID")
    private Set<DomainMetricValue> possibleValues;

    @ManyToOne(cascade = {CascadeType.ALL})
    private DomainMetricValue defaultValue;

    /*
     * CREATORS
     */

    public DomainMetric() {
	possibleValues = new TreeSet<>(new DomainMetricValueComparator());
	defaultValue = null;
    }

    public DomainMetric(@JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments) throws IntegrityException {
	super(name, description, comments);

    }
    /*
     * GET'S AND SET'S METHODS
     */

    public Set<DomainMetricValue> getPossibleValues() {
	return possibleValues;
    }

    public List<DomainMetricValue> getPossibleValuesAsList() {
	return new ArrayList<>(this.possibleValues);
    }

    public List<String> getPossibleValuesAsString() {
	List<DomainMetricValue> l = getPossibleValuesAsList();
	List<String> ls = new ArrayList<>();
	for (int i = 0; i < l.size(); i++) {
	    ls.add(l.get(i).getValue());
	}
	return ls;
    }

    public void setPossibleValues(Set<DomainMetricValue> v) {
	if (v == null)
	    possibleValues = new TreeSet<>(new DomainMetricValueComparator());
	else {
	    possibleValues = new TreeSet<>(new DomainMetricValueComparator());
	    for (DomainMetricValue dmv : v) possibleValues.add(dmv);
	}
    }

    public DomainMetricValue getDefaultValue() {
	return defaultValue;
    }

    public void setDefaultValue(DomainMetricValue v) {
	defaultValue = v;
    }

    /**
     * This method is useful to set the default value in safe mode. With this
     * method, we check that the new Default Value is one of the possible values of
     * this metric. In another case, we raise a possible value exception.
     * 
     * @param v
     *            The new default value
     * @throws PossibleValueException
     *             We raise a possible value exception when the new Default Value
     *             isn't one of the possible values of this metric.
     */
    public void safeSetDefaultValue(DomainMetricValue v) throws PossibleValueException {
	// If the new default value is null, we assign null to
	// default value and return
	if (v == null) {
	    defaultValue = v;
	    return;
	}
	// If new default value isn't null and there are possible
	// values, we check that the new default value is one of the
	// possible values of this metric and save the result in ok
	// variable
	boolean ok = false;
	if (!possibleValues.isEmpty()) {
	    Iterator<DomainMetricValue> iter = possibleValues.iterator();
	    while (iter.hasNext()) {
		DomainMetricValue m = iter.next();
		if (m.sameValue(v)) {
		    ok = true;
		    break;
		}
	    }
	}
	// If new default value is one of the possible values of this
	// metric, we assign the new default value
	if (ok)
	    defaultValue = v;
	// In another case, we raise a PossibleValueException
	else
	    throw new PossibleValueException("The default value must be one of the possible values");
    }

    /**
     * This method is useful to set the default value in safe mode. With this
     * method, we check that the new Default Value is one of the possible values of
     * this metric. In another case, we raise a possible value exception.
     * 
     * @param v
     *            The string value of the new default value
     * @throws PossibleValueException
     *             We raise a possible value exception when the new Default Value
     *             isn't one of the possible values of this metric.
     */
    public void setDefaultValue(String v) throws PossibleValueException {
	// If the new default value is null, we assign null to
	// default value and return
	if (v == null) {
	    defaultValue = null;
	    return;
	}
	// If new default value isn't null and there are possible
	// values, we check that the new default value is one of the
	// possible values of this metric and save the result in ok
	// variable.
	// If new default value is a possible value of this metric, we
	// also assign the DomainMetricValue that correspond to the new
	// default value as default value.
	boolean ok = false;
	if (!possibleValues.isEmpty()) {
	    Iterator<DomainMetricValue> iter = possibleValues.iterator();
	    while (iter.hasNext()) {
		DomainMetricValue m = iter.next();
		if (m.getValue().compareTo(v) == 0) {
		    ok = true;
		    defaultValue = m;
		    break;
		}
	    }
	}
	// If new default value isn't one of the possible values of this
	// metric, we raise a possible value exception
	if (!ok)
	    throw new PossibleValueException("The default value must be one of the possible values");
    }

    /*
     * OTHER METHODS
     */

    /**
     * This method is useful to see if this domain metric has a default value
     * assigned. If default value is null, the domain metric don't has default value
     * assigned
     * 
     * @return A boolean that show if this domain metric has a default value
     *         assigned or not.
     */
    @Override
    public boolean hasDefaultValue() {
	return (defaultValue != null);
    }

    /**
     * This method is useful to unset ("desassign") the default value assigned to
     * this float metric. We do this setting the default value to null.
     */
    @Override
    public void unsetDefaultValue() {
	defaultValue = null;
    }

    /**
     * This method is useful to add a DomainMetricValue as possible value of this
     * metric. We only add the DomainMetricValue received as parameter if it's value
     * is different to all value of other possible values; in another case, we raise
     * a value exception and do nothing.
     * 
     * @param v
     *            The DomainMetricValue that has to be added to the possible values
     *            of this metric.
     * @throws ValueException
     *             We raise a ValueException when the value of the DomainMetricValue
     *             received as parameter is equal than other value of one of the
     *             possible values of this metric.
     */
    public void addValue(DomainMetricValue v) throws ValueException {
	Iterator<DomainMetricValue> i = possibleValues.iterator();
	while (i.hasNext()) {
	    DomainMetricValue mv = i.next();
	    if (mv.sameValue(v)) throw new ValueException("Domain metric can't have two or more equals possible values.");
	}
	possibleValues.add(v);
    }

    /**
     * This method is useful to remove a DomainMetricValue from possible values of
     * this metric.
     * 
     * @param v
     *            The DomainMetricValue that has to be removed to the possible
     *            values of this metric.
     */
    public void removePossibleValue(DomainMetricValue v) {
	// Search in possible values set the metric domain
	// value received as parameter
	Iterator<DomainMetricValue> iter = possibleValues.iterator();
	while (iter.hasNext()) {
	    DomainMetricValue m = iter.next();
	    // When we find it, we remove it from possible values set
	    if (m.sameValue(v)) {
		iter.remove();
		return;
	    }
	}
    }

    /**
     * This method is useful to remove a value from possible values of this metric.
     * 
     * @param v
     *            The string that corresponds to the value of the DomainMetricValue
     *            that has to be removed to the possible values of this metric.
     */
    public void removePossibleValue(String value) {
	// Search in possible values set the metric domain
	// value that contains the value received as parameter
	Iterator<DomainMetricValue> iter = possibleValues.iterator();
	while (iter.hasNext()) {
	    DomainMetricValue m = iter.next();
	    // When we find it, we remove it from possible values set
	    if (value.compareTo(m.getValue()) == 0) {
		iter.remove();
		return;
	    }
	}
    }

    /**
     * This method remove all possible values and default value of this domain
     * metric.
     */
    public void removeAllPossibleValues() {
	possibleValues.clear();
	defaultValue = null;
    }

    /**
     * This method is useful to get the DomainMetricValue that contains the string
     * value received as parameter in this possible values set.
     * 
     * @param value
     *            The value that w want to search in the possible values set of this
     *            metric.
     * @return The DomainMetricValue that contains the string value received as
     *         parameter in this possible values set.
     */
    public DomainMetricValue getPossibleValueByValue(String value) {
	if (!possibleValues.isEmpty()) {
	    Iterator<DomainMetricValue> iter = possibleValues.iterator();
	    while (iter.hasNext()) {
		DomainMetricValue m = iter.next();
		if (value.compareTo(m.getValue()) == 0) {
		    return m;
		}
	    }
	}
	return null;
    }

    /**
     * This class is useful to compare 2 DomainMetricValue.
     */
    public static class DomainMetricValueComparator implements Comparator<DomainMetricValue> {
	@Override
	public int compare(DomainMetricValue obj1, DomainMetricValue obj2) {
	    if (obj1 == null || obj2 == null) throw new NullPointerException("Comparison between null objects is not allowed");

	    if (obj1.getOrder() < obj2.getOrder()) return -1;
	    else if (obj1.getOrder() > obj2.getOrder()) return 1;
	    return 0;
	}
    }

    @Override
    public Type getType() {
	return Type.DOMAIN;
    }

    public void clearPossibleValues() {
	this.defaultValue = null;
	this.possibleValues.clear();
    }

    public void clearPossibleValuesAndAddPossible(Set<DomainMetricValue> values) {
	clearPossibleValues();
	possibleValues.addAll(values);
	
    }

}
