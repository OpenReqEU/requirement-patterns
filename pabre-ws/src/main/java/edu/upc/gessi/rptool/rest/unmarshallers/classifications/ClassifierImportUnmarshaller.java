package edu.upc.gessi.rptool.rest.unmarshallers.classifications;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Transient;
import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.IdToDomainObject;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.RedundancyException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class ClassifierImportUnmarshaller extends ClassifierUnmarshaller {

    protected Set<String> sourcesAux;
    protected Set<String> reqPatterns;

    protected Set<ClassifierImportUnmarshaller> internalClassifiersAux;

    @JsonCreator
    public ClassifierImportUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "pos", required = true) int pos,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sources,
	    @JsonProperty(value = "requirementPatterns", required = false) Set<String> reqPatterns,
	    @JsonProperty(value = "internalClassifiers", required = false) Set<ClassifierImportUnmarshaller> internalClassifiers)
	    throws IntegrityException, SemanticallyIncorrectException {
	super(id, name, description, comments, pos);

	setBasicValues(sources, reqPatterns, internalClassifiers);

    }

    protected void setBasicValues(Set<String> sources, Set<String> reqPatterns,
	    Set<ClassifierImportUnmarshaller> internalClassifiers) {
	this.sourcesAux = sources;
	this.reqPatterns = reqPatterns;
	this.internalClassifiersAux = internalClassifiers;

	if (this.sourcesAux == null)
	    this.sourcesAux = new HashSet<>();
	if (this.reqPatterns == null)
	    this.reqPatterns = new HashSet<>();
	if (this.internalClassifiersAux == null)
	    this.internalClassifiersAux = new HashSet<>();
    }

    @Override
    protected void checkClassifierAndAdd()
	    throws SemanticallyIncorrectException, IntegrityException, RedundancyException {
	Set<ClassifierUnmarshaller> s = new HashSet<>();
	for (ClassifierUnmarshaller internalClassifiers : internalClassifiersAux) {
	    s.add(internalClassifiers);
	}
	checkPosAndAddClassifier(s);
    }

    @Override
    protected void setSources() throws SemanticallyIncorrectException {
	try {
	    classifierInstance.clearAndSetSources(IdToDomainObject.getSourcesByIdentifiers(sourcesAux));
	} catch (NotFoundException e) {
	    throw new SemanticallyIncorrectException("invalid source id");
	}
    }

    @Override
    protected void getPatternAndUpdate() throws SemanticallyIncorrectException, RedundancyException {
	Set<RequirementPattern> auxReqPatterns;
	try {
	    auxReqPatterns = IdToDomainObject.getReqPatternsByNames(this.reqPatterns);
	} catch (NotFoundException e) {
	    throw new SemanticallyIncorrectException("invalid req pattern id");
	}
	for (RequirementPattern rp : auxReqPatterns) {
	    rp.addClassifier(classifierInstance);
	}
    }

    @Transient
    public Set<String> getSourceNames() {
	return sourcesAux;
    }

    @Transient
    public Set<String> getReqPatternsNames() {
	return reqPatterns;
    }

    @Transient
    public Set<ClassifierImportUnmarshaller> getUnmImportInternalClassifiers() {
	return internalClassifiersAux;
    }

    @Override
    public int hashCode() {
	return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
	return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int getPos() {
	return classifierInstance.getPos();
    }

}
