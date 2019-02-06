package edu.upc.gessi.rptool.domain.metrics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;

@Entity
@Table(name = "STRING_METRIC")
public class StringMetric extends SimpleMetric {

    /*
     * ATTRIBUTES
     */

    @Column(name = "DEFAULT_VALUE", nullable = true)
    protected String defaultValue = null;

    /*
     * CREATORS
     */

    public StringMetric() {
    }

    public StringMetric(String def) {
	defaultValue = def;
    }

    @JsonCreator
    public StringMetric(@JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "defaultValue", required = false) String defaultValue) throws IntegrityException {
	super(name, description, comments);
	this.defaultValue = defaultValue;
    }

    /*
     * GET'S AND SET'S METHODS
     */

    /**
     * Sets the default value. The string metric default value can't be an empty
     * value (it can't be null or it's length can't be 0). Because of this, if the
     * new default value is an empty value, we raise a value exception.
     * 
     * @param def
     *            The new default value
     * @throws ValueException
     *             We raise a value exception when new default value is an empty
     *             value (null or when new default value length is 0).
     */
    public void setDefaultValue(String def) throws ValueException {
	if (def != null && def.trim().length() == 0)
	    throw new ValueException("You have to introduce a name for the default value.");
	defaultValue = def;
    }

    public String getDefaultValue() {
	return defaultValue;
    }

    /*
     * OTHER METHODS
     */

    /**
     * This method is useful to see if this string metric has a default value
     * assigned.
     * 
     * @return A boolean that show if this string metric has a default value
     *         assigned or not.
     */
    @Override
    public boolean hasDefaultValue() {
	return (defaultValue != null);
    }

    /**
     * This method is useful to unset ("desassign") the default value assigned to
     * this string metric. This method set the hasDefault attribute to false.
     */
    @Override
    public void unsetDefaultValue() {
	defaultValue = null;
    }

    public void setDefaultValueNoCheck(String d) {
	defaultValue = d;
    }

    @Override
    public Type getType() {
	return Type.STRING;
    }

}