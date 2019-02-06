package edu.upc.gessi.rptool.domain.metrics;

import javax.persistence.Entity;
import javax.persistence.Table;

import edu.upc.gessi.rptool.exceptions.IntegrityException;

/*
 * This abstract class represents a SimpleMetric. It is a 
 * subclass of Metric ("extends Metric") and it has a default
 * value ("implements DefaultValueMetric").
 * 
 * All subclass of SimpleMetric extends this class.
 */
@Entity
@Table(name = "SIMPLE_METRIC")
public abstract class SimpleMetric extends Metric implements DefaultValueMetric {
    public SimpleMetric() {
    };

    public SimpleMetric(String name, String description, String comments) throws IntegrityException {
	super(name, description, comments);
    }

}