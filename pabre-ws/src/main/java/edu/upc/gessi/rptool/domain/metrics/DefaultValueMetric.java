package edu.upc.gessi.rptool.domain.metrics;

/*
 * This interface represent a metric that 
 * has a default value
 */

public interface DefaultValueMetric {

    /*
     * METHODS TO IMPLEMENT
     */

    /*
     * This method will be useful to know if this metric has a default value
     * assigned
     */
    public boolean hasDefaultValue();

    /*
     * This method will be useful to unset (to "desassign") the default value of
     * this metric
     */
    public void unsetDefaultValue();

}
