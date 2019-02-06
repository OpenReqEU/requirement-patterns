package edu.upc.gessi.rptool.domain.metrics;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.exceptions.IntegrityException;

@Entity
@Table(name = "SET_METRIC")
public class SetMetric extends Metric {

    /*
     * ATTRIBUTES
     */

    @ManyToOne
    private SimpleMetric simple;

    /*
     * CREATORS
     */

    public SetMetric() {
    }

    public SetMetric(SimpleMetric type) {
	simple = type;
    }

    @JsonCreator
    public SetMetric(@JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments) throws IntegrityException {
	super(name, description, comments);

    }
    /*
     * GET'S AND SET'S METHODS
     */

    public SimpleMetric getSimple() {
	return simple;
    }

    public void setSimple(SimpleMetric type) {
	simple = type;
    }

    @Override
    public Type getType() {
	return Type.SET;
    }

}