package edu.upc.gessi.rptool.domain.metrics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.upc.gessi.rptool.domain.Identificable;

/*
 * This class represents a Domain Metric Value, i.e.,
 * a value that belongs to one DomainMetric. 
 */

@Entity
@Table(name = "METRICS_DOMAIN_VALUE")
public class DomainMetricValue implements Comparable<DomainMetricValue>, Identificable {

    /*
     * ATTRIBUTES
     */

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "customGenerator")
    private long id;

    @Column(name = "VALUE", nullable = false)
    private String value;

    @Column(name = "VALUE_ORDER", nullable = false)
    private int order;

    /*
     * CREATORS
     */

    public DomainMetricValue() {
    }

    public DomainMetricValue(String value) {
	this.value = value;
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public long getId() {
	return id;
    }

    public void setId(long nid) {
	id = nid;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String v) {
	value = v;
    }

    public int getOrder() {
	return order;
    }

    public void setOrder(int o) {
	order = o;
    }

    /*
     * OTHER METHODS
     */

    /**
     * This method is useful to know if two DomainMetricValue have the same value.
     * It returns a boolean that show if this DomainMetricValue and the
     * DomainMetricValue received as parameter have the same value.
     * 
     * @param v
     *            The DomainMetricValue which compare this.
     * @return True, if value of this DomainMetricValue and value of the
     *         DomainMetricValue received as parameter are equals.
     */
    public boolean sameValue(DomainMetricValue v) {
	return (this.getValue().compareTo(v.getValue()) == 0);
    }

    /**
     * Method used to compare 2 DomainMetricValue: this and the DomainMetricValue
     * received as parameter
     * 
     * @param obj
     *            MDomainMetricValue that we want to compare
     * @return - The comparison result, ignoring case, between the values of the
     *         DomainMetricValue
     */
    @Override
    public int compareTo(DomainMetricValue o) {
	if (o == null)
	    throw new NullPointerException("Comparison between null objects is not allowed");

	return this.getValue().compareToIgnoreCase(o.getValue());
    }

}