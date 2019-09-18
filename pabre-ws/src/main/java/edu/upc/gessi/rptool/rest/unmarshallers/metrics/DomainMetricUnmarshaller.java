package edu.upc.gessi.rptool.rest.unmarshallers.metrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.metrics.DomainMetric;
import edu.upc.gessi.rptool.domain.metrics.DomainMetricValue;
import edu.upc.gessi.rptool.exceptions.PossibleValueException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class DomainMetricUnmarshaller extends UnmarshallerGenericMetric {

    protected String defaultValueString;
    protected DomainMetricValue defaultValue;
    protected List<String> possibleValues;

    @JsonCreator
    public DomainMetricUnmarshaller(@JsonProperty(value = "id", required = false) Long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "defaultValue", required = false) String defaultValueString,
	    @JsonProperty(value = "possibleValues", required = true) List<String> possibleValues,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sourcesByIdentifier) {
	this.id = id == null ? 0 : id;
	this.possibleValues = possibleValues;
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.sources = sources;
	this.sourcesByIdentifier = sourcesByIdentifier;
	this.defaultValueString = defaultValueString;

    }

    public DomainMetricUnmarshaller(String name, String description, String comments, String defaultValueString,
	    List<String> possibleValues, Set<Long> sources, Set<String> sourcesByIdentifier){
	this.possibleValues = possibleValues;
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.sources = sources;
	this.sourcesByIdentifier = sourcesByIdentifier;
	this.defaultValueString = defaultValueString;

    }

    public DomainMetricUnmarshaller(String name, String description, String comments, String defaultValueString,
	    List<String> possibleValues) {
	this.possibleValues = possibleValues;
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.defaultValueString = defaultValueString;
    }

    protected void checkPossibleValues() throws SemanticallyIncorrectException {
	if (possibleValues.isEmpty())
	    throw new SemanticallyIncorrectException("possibleValues is empty");
    }

    private Set<DomainMetricValue> buildPossibleValues() {

	Set<DomainMetricValue> ret = new HashSet<>();
	int counter = 0;
	defaultValue = null;
	if (possibleValues == null)
	    possibleValues = new ArrayList<>();
	for (String pvu : possibleValues) {

	    DomainMetricValue aux = new DomainMetricValue(pvu);
	    aux.setOrder(counter);
	    ret.add(aux);
	    ++counter;
	    if (pvu.equals(defaultValueString)) defaultValue = aux;
	}
	return ret;
    }

    @Override
    public void instantiateInternalMetric() {
	metric = new DomainMetric();
    }

    protected void setDefaultValues() throws SemanticallyIncorrectException {
	if (defaultValueString != null) {
	    if (defaultValue == null) throw new SemanticallyIncorrectException("defaultValue not in possibleValues");
	    try {
		((DomainMetric) metric).safeSetDefaultValue(defaultValue);
	    } catch (PossibleValueException pve) {
		throw new SemanticallyIncorrectException(pve.getMessage());
	    }
	}
    }

    @Override
    public void myBuild() throws SemanticallyIncorrectException {
	checkPossibleValues();
	((DomainMetric) metric).setPossibleValues(buildPossibleValues());
	setDefaultValues();
    }
}
