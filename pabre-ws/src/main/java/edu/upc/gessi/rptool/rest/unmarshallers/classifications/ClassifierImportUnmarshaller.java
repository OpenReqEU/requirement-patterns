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

    protected Set<String> sources, reqPatterns;

    protected Set<ClassifierImportUnmarshaller> internalClassifiers;

    @JsonCreator
    public ClassifierImportUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "pos", required = true) int pos,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sources,
	    @JsonProperty(value = "requirementPatterns", required = false) Set<String> reqPatterns,
	    @JsonProperty(value = "internalClassifiers", required = false) Set<ClassifierImportUnmarshaller> internalClassifiers)
	    throws IntegrityException, IOException, SemanticallyIncorrectException {
	super(id, name, description, comments, pos);

	setBasicValues(sources, reqPatterns, internalClassifiers);

    }

    protected void setBasicValues(Set<String> sources, Set<String> reqPatterns,
	    Set<ClassifierImportUnmarshaller> internalClassifiers) {
	this.sources = sources;
	this.reqPatterns = reqPatterns;
	this.internalClassifiers = internalClassifiers;

	if (this.sources == null)
	    this.sources = new HashSet<>();
	if (this.reqPatterns == null)
	    this.reqPatterns = new HashSet<>();
	if (this.internalClassifiers == null)
	    this.internalClassifiers = new HashSet<>();
    }

    @Override
    protected void checkClassifierAndAdd()
	    throws SemanticallyIncorrectException, IntegrityException, RedundancyException {
	Set<ClassifierUnmarshaller> s = new HashSet<>();
	for (ClassifierUnmarshaller internalClassifiers : internalClassifiers) {
	    s.add(internalClassifiers);
	}
	checkPosAndAddClassifier(s);
    }

    @Override
    protected void setSources() throws SemanticallyIncorrectException {
	try {
	    classifierInstance.clearAndSetSources(IdToDomainObject.getSourcesByIdentifiers(sources));
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
	return sources;
    }

    @Transient
    public Set<String> getReqPatternsNames() {
	return reqPatterns;
    }

    @Transient
    public Set<ClassifierImportUnmarshaller> getUnmImportInternalClassifiers() {
	return internalClassifiers;
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
