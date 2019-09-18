package edu.upc.gessi.rptool.domain.metrics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;

@Entity
@Table(name = "INTEGER_METRIC")
public class IntegerMetric extends SimpleMetric {

    /*
     * ATTRIBUTES
     */

    @Column(name = "MIN_VALUE", nullable = false)
    protected Integer minValue;

    @Column(name = "MAX_VALUE", nullable = true)
    protected Integer maxValue;

    @Column(name = "DEFAULT_VALUE", nullable = true)
    protected Integer defaultValue;

    @Column(name = "HAS_DEFAULT", nullable = false)
    protected Boolean hasDefault = false;

    /*
     * CREATORS
     */

    /**
     * This method instantiates a new integer metric. Only is useful to Hibernate,
     * DON'T USE!!!!
     */
    public IntegerMetric() {
	minValue = Integer.MIN_VALUE;
	maxValue = Integer.MAX_VALUE;
    }

    /**
     * This method instantiates a new integer metric with the minimum and maximum
     * values received as parameters. If minimum isn't <= maximum we raise a
     * ValueException.
     * 
     * @param min
     *            The minimum value of this integer metric.
     * @param max
     *            The maximum value of this integer metric.
     * @throws ValueException
     *             This exception is raised when the minimum > maximum.
     */

    public void setMinMax(int min, int max) throws ValueException {
	if (min <= max) {
	    minValue = min;
	    maxValue = max;
	} else {
	    throw new ValueException("Minimum must be smaller than maximum.");
	}
    }

    public IntegerMetric(int min, int max) throws ValueException {
	setMinMax(min, max);
    }

    /**
     * This method instantiates a new integer metric with the minimum, maximum and
     * default values received as parameters. If minimum isn't <= default or default
     * isn't <= maximum we raise a ValueException.
     * 
     * @param min
     *            The minimum value of this integer metric.
     * @param max
     *            The maximum value of this integer metric.
     * @param def
     *            The default value of this integer metric.
     * @throws ValueException
     *             This exception is raised when minimum isn't <= default or default
     *             isn't <= maximum
     */
    public IntegerMetric(int min, int max, int def) throws ValueException {
	if (min <= def && def <= max) {
	    minValue = min;
	    maxValue = max;
	    defaultValue = def;
	    hasDefault = true;
	} else
	    throw new ValueException("Minimum must be smaller than default value and \n "
		    + "default value must be smaller than maximum.");
    }

    @JsonCreator
    public IntegerMetric(@JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "minValue", required = true) int minValue,
	    @JsonProperty(value = "maxValue", required = true) int maxValue,
	    @JsonProperty(value = "defaultValue", required = false) Integer defaultValue)
	    throws ValueException, IntegrityException {
	super(name, description, comments);
	setMinMax(minValue, maxValue);

	if (defaultValue != null) {
	    setDefaultValue(defaultValue);
	}

    }

    private final String defaultValueErrorMessage = "If default value is defined, minimum must be smaller than default value and default value must be smaller than maximum. Otherwise, minimum must be smaller than maximum.";

    /*
     * GET'S AND SET'S METHODS
     */

    /**
     * Sets the minimum value. A integer metric always has to satisfy that minimum
     * <= maximum, and, if it has a default value, minimum <= default value <=
     * maximum. Because of this, if this metric has default value and the new
     * minimum value is > than it or if this metric don't has a default value and
     * the new minimum value is > than its maximum value, we raise a value
     * exception.
     * 
     * @param min
     *            The new minimum value
     * @throws ValueException
     *             We raise a value exception when this metric has default value and
     *             the new minimum value is > than it or when this metric don't has
     *             a default value and the new minimum value is > than its maximum
     *             value.
     */
    public void setMinValue(int min) throws ValueException {
	if ((hasDefault && min > defaultValue) || min > maxValue)
	    throw new ValueException(defaultValueErrorMessage);
	minValue = min;
    }

    public Integer getMinValue() {
	return minValue;
    }

    /**
     * Sets the maximum value. A integer metric always has to satisfy that minimum
     * <= maximum, and, if it has a default value, minimum <= default value <=
     * maximum. Because of this, if this metric has default value and the new
     * maximum value is < than it or if this metric don't has a default value and
     * the new maximum value is < than its minimum value, we raise a value
     * exception.
     * 
     * @param max
     *            The new maximum value
     * @throws ValueException
     *             We raise a value exception when this metric has default value and
     *             the new maximum value is < than it or when this metric don't has
     *             a default value and the new maximum value is < than its minimum
     *             value.
     */
    public void setMaxValue(int max) throws ValueException {
	if ((hasDefault && max < defaultValue) || max < minValue)
	    throw new ValueException(defaultValueErrorMessage);
	maxValue = max;
    }

    public Integer getMaxValue() {
	return maxValue;
    }

    /**
     * Sets the default value. A integer metric always has to satisfy that minimum
     * <= maximum, and, if it has a default value, minimum <= default value <=
     * maximum. Because of this, if new default value is < than minimum or if new
     * default value > than maximum, we raise a value exception. If new default
     * value is a correct value, we also set hasDefault attribute to true.
     * 
     * @param def
     *            The new default value
     * @throws ValueException
     *             We raise a value exception when new default value is < than
     *             minimum or when new default value > than maximum.
     */
    public void setDefaultValue(Integer def) throws ValueException {
	setMinMax(minValue, maxValue);
	if (def == null) {
	    hasDefault = false;
	} else if ((def < minValue || def > maxValue)) {
	    throw new ValueException(defaultValueErrorMessage);
	} else {
	    defaultValue = def;
	    hasDefault = true;
	}

    }

    public Integer getDefaultValue() {
	return defaultValue;
    }

    public boolean getHasDefault() {
	return hasDefault;
    }

    public void setHasDefault(boolean hasDefault) {
	this.hasDefault = hasDefault;
    }

    /*
     * OTHER METHODS
     */

    /**
     * This method is useful to see if this integer metric has a default value
     * assigned.
     * 
     * @return A boolean that show if this integer metric has a default value
     *         assigned or not.
     */
    @Override
    public boolean hasDefaultValue() {
	return hasDefault;
    }

    /**
     * This method is useful to unset ("desassign") the default value assigned to
     * this integer metric. This method set the hasDefault attribute to false.
     */
    @Override
    public void unsetDefaultValue() {
	hasDefault = false;
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
    public void setValues(int minValue, int maxValue, boolean hasDefValue, int defValue) throws ValueException {

	if ((hasDefValue && (minValue > defValue || maxValue < defValue)) || (maxValue < minValue)) {
	    throw new ValueException(defaultValueErrorMessage);
	}
	// all values are correct
	this.minValue = minValue;
	this.maxValue = maxValue;
	this.defaultValue = defValue;
	this.hasDefault = hasDefValue;
    }

    public void setMinNoCheck(Integer minValue2) {
	minValue = minValue2;

    }

    public void setMaxNoCheck(Integer maxValue2) {
	maxValue = maxValue2;

    }

    public void setDefaultValueNoCheck(Integer defaultValue2) {
	defaultValue = defaultValue2;

    }

    @Override
    public Type getType() {
	return Type.INTEGER;
    }

}