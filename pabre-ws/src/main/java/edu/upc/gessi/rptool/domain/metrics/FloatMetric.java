package edu.upc.gessi.rptool.domain.metrics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;

@Entity
@Table(name = "FLOAT_METRIC")
public class FloatMetric extends SimpleMetric {

    /*
     * ATTRIBUTES
     */

    @Column(name = "MIN_VALUE", nullable = false)
    protected Float minValue;

    @Column(name = "MAX_VALUE", nullable = true)
    protected Float maxValue;

    @Column(name = "DEFAULT_VALUE", nullable = true)
    protected Float defaultValue;

    @Column(name = "HAS_DEFAULT", nullable = false)
    protected Boolean hasDefault = false;

    /*
     * CREATORS
     */

    /**
     * This method instantiates a new float metric. Only is useful to Hibernate,
     * DON'T USE!!!!
     */
    public FloatMetric() {
	minValue = Float.MIN_VALUE;
	maxValue = Float.MAX_VALUE;
	defaultValue = Float.MAX_VALUE;
    }

    /**
     * This method instantiates a new float metric with the minimum and maximum
     * values received as parameters. If minimum isn't <= maximum we raise a
     * ValueException.
     * 
     * @param min
     *            The minimum value of this float metric.
     * @param max
     *            The maximum value of this float metric.
     * @throws ValueException
     *             This exception is raised when the minimum > maximum.
     */
    public void setMinMax(float min, float max) throws ValueException {
	if (min <= max) {
	    minValue = min;
	    maxValue = max;
	    defaultValue = maxValue;
	} else
	    throw new ValueException("Minimum must be smaller than maximum.");

    }

    public FloatMetric(float min, float max) throws ValueException {
	setMinMax(min, max);
    }

    @JsonCreator
    public FloatMetric(@JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "minValue", required = true) float minValue,
	    @JsonProperty(value = "maxValue", required = true) float maxValue,
	    @JsonProperty(value = "defaultValue", required = false) Float defaultValue)
	    throws ValueException, IntegrityException {
	super(name, description, comments);
	setMinMax(minValue, maxValue);

	if (defaultValue != null) {
	    setDefaultValue(defaultValue);
	}

    }

    /**
     * This method instantiates a new float metric with the minimum, maximum and
     * default values received as parameters. If minimum isn't <= default or default
     * isn't <= maximum we raise a ValueException.
     * 
     * @param min
     *            The minimum value of this float metric.
     * @param max
     *            The maximum value of this float metric.
     * @param def
     *            The default value of this float metric.
     * @throws ValueException
     *             This exception is raised when minimum isn't <= default or default
     *             isn't <= maximum
     */
    public FloatMetric(float min, float max, float def) throws ValueException {
	if (min <= def && def <= max) {
	    minValue = min;
	    maxValue = max;
	    defaultValue = def;
	    hasDefault = true;
	} else
	    throw new ValueException(
		    "Minimum must be smaller than default value and default value must be smaller than maximum.");
    }

    /*
     * GET'S AND SET'S METHODS
     */

    /**
     * Sets the minimum value. A float metric always has to satisfy that minimum <=
     * maximum, and, if it has a default value, minimum <= default value <= maximum.
     * Because of this, if this metric has default value and the new minimum value
     * is > than it or if this metric don't has a default value and the new minimum
     * value is > than its maximum value, we raise a value exception.
     * 
     * @param min
     *            The new minimum value
     * @throws ValueException
     *             We raise a value exception when this metric has default value and
     *             the new minimum value is > than it or when this metric don't has
     *             a default value and the new minimum value is > than its maximum
     *             value.
     */
    public void setMinValue(float min) throws ValueException {
	if ((hasDefault && min > defaultValue) || min > maxValue)
	    throw new ValueException(
		    "If default value is defined, minimum must be smaller than default value and default value must be smaller than maximum. Otherwise, minimum must be smaller than maximum.");
	minValue = min;
    }

    public Float getMinValue() {
	return minValue;
    }

    /**
     * Sets the maximum value. A float metric always has to satisfy that minimum <=
     * maximum, and, if it has a default value, minimum <= default value <= maximum.
     * Because of this, if this metric has default value and the new maximum value
     * is < than it or if this metric don't has a default value and the new maximum
     * value is < than its minimum value, we raise a value exception.
     * 
     * @param max
     *            The new maximum value
     * @throws ValueException
     *             We raise a value exception when this metric has default value and
     *             the new maximum value is < than it or when this metric don't has
     *             a default value and the new maximum value is < than its minimum
     *             value.
     */
    public void setMaxValue(float max) throws ValueException {
	if ((hasDefault && max < defaultValue) || max < minValue)
	    throw new ValueException(
		    "If default value is defined, minimum must be smaller than default value and default value must be smaller than maximum. Otherwise, minimum must be smaller than  maximum.");
	maxValue = max;
    }

    public Float getMaxValue() {
	return maxValue;
    }

    /**
     * Sets the default value. A float metric always has to satisfy that minimum <=
     * maximum, and, if it has a default value, minimum <= default value <= maximum.
     * Because of this, if new default value is < than minimum or if new default
     * value > than maximum, we raise a value exception. If new default value is a
     * correct value, we also set hasDefault attribute to true.
     * 
     * @param def
     *            The new default value
     * @throws ValueException
     *             We raise a value exception when new default value is < than
     *             minimum or when new default value > than maximum.
     */
    public void setDefaultValue(Float def) throws ValueException {
	hasDefault = true;
	if (def == null) {
	    hasDefault = false;
	} else if ((def < minValue || def > maxValue)) {
	    throw new ValueException(
		    "If default value is defined, minimum must be smaller than default value and default value must be smaller than maximum. Otherwise, minimum must be smaller than maximum.");
	} else {
	    defaultValue = def;

	}

    }

    public Float getDefaultValue() {
	return defaultValue;
    }

    public void setHasDefault(boolean hasDefault) {
	this.hasDefault = hasDefault;

    }

    public boolean getHasDefault() {
	return hasDefault;
    }

    /*
     * OTHER METHODS
     */

    /**
     * This method is useful to see if this float metric has a default value
     * assigned.
     * 
     * @return A boolean that show if this float metric has a default value assigned
     *         or not.
     */
    @Override
    public boolean hasDefaultValue() {
	return hasDefault;
    }

    /**
     * This method is useful to unset ("desassign") the default value assigned to
     * this float metric. This method set the hasDefault attribute to false.
     */
    @Override
    public void unsetDefaultValue() {
	hasDefault = false;
    }

    public void setDefaultValue() throws ValueException {
	hasDefault = true;
	setDefaultValue(defaultValue);

    }

    /**
     * Method used to evaluate the correctness of all new values from user interface
     * at the same time.
     * 
     * @param minValue
     *            The new min value
     * @param maxValue
     *            The new max value
     * @param hasDefValue
     *            true if default value has to be considered, false if not
     * @param defValue
     *            The new default value
     * @throws ValueException
     *             Raises a value exception when new default value is < than minimum
     *             or when new default value > than maximum or when the new maximum
     *             value is < than the new minimum value.
     */
    public void setValues(float minValue, float maxValue, boolean hasDefValue, float defValue) throws ValueException {

	if ((hasDefValue && (minValue > defValue || maxValue < defValue)) || (maxValue < minValue)) {
	    throw new ValueException(
		    "If default value is defined, minimum must be smaller than default value and default value must be smaller than maximum. Otherwise, minimum must be smaller than  maximum.");
	}
	// all values are correct
	this.minValue = minValue;
	this.maxValue = maxValue;
	this.defaultValue = defValue;
	this.hasDefault = hasDefValue;
    }

    @Override
    public Type getType() {
	return Type.FLOAT;
    }

    public void setMinNoCheck(Float min) {
	minValue = min;
    }

    public void setMaxNoCheck(Float max) {
	maxValue = max;
    }

    public void setDefaultValueNoCheck(Float defaultV) {
	defaultValue = defaultV;
    }

}