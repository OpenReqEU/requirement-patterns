package edu.upc.gessi.rptool.rest.unmarshallers.classifications;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.IdToDomainObject;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.RedundancyException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class ClassifierUnmarshaller {

    protected Set<Long> sources;
    protected Set<Long> requirementPatterns;
    protected long id;
    protected Set<ClassifierUnmarshaller> internalClassifiers;
    protected Classifier classifierInstance;
    private int pos;
    private static final Logger logger = Logger.getLogger(ClassifierUnmarshaller.class.getName());

    @JsonCreator
    public ClassifierUnmarshaller(@JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "pos", required = true) int pos,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "requirementPatterns", required = false) Set<Long> reqPatterns,
	    @JsonProperty(value = "internalClassifiers", required = false) Set<ClassifierUnmarshaller> internalClassifiers)
	    throws IntegrityException, SemanticallyIncorrectException {
	setBasicValues(name, description, comments, pos);
	setSourcesPatternsClassifiersAndInit(sources, reqPatterns, internalClassifiers);
    }

    public ClassifierUnmarshaller(long id, String name, String description, String comments, int pos)
	    throws IntegrityException, SemanticallyIncorrectException {
	this.id = id;
	setBasicValues(name, description, comments, pos);
    }

    /**
     * Set the basic values of the classifier instance
     * 
     * @param name
     *            Name to set
     * @param description
     *            Description to set
     * @param comments
     *            comments to set
     * @param pos
     *            pos to set
     * @throws IntegrityException
     * @throws SemanticallyIncorrectException
     */
    protected void setBasicValues(String name, String description, String comments, int pos)
	    throws IntegrityException, SemanticallyIncorrectException {
	this.pos = pos;// variable used to check positions

	classifierInstance = new Classifier();
	classifierInstance.setName(name);
	classifierInstance.setDescription(description);
	classifierInstance.setComments(comments);
	classifierInstance.setPos(pos);
    }

    /**
     * method used to initialize the internal collection
     * 
     * @param sources
     *            Sources to set
     * @param reqPatterns
     *            Patterns to set
     * @param internalClassifiers
     *            Classifiers to set
     */
    protected void setSourcesPatternsClassifiersAndInit(Set<Long> sources, Set<Long> reqPatterns,
	    Set<ClassifierUnmarshaller> internalClassifiers) {
	this.sources = sources;
	this.requirementPatterns = reqPatterns;
	this.internalClassifiers = internalClassifiers;

	if (this.sources == null)
	    this.sources = new HashSet<>();
	if (this.requirementPatterns == null)
	    this.requirementPatterns = new HashSet<>();
	if (this.internalClassifiers == null)
	    this.internalClassifiers = new HashSet<>();
    }

    /**
     * Method created to build a {@link Classifier} from a unmarshaller.
     * 
     * @param isRoot
     *            This variable indicate if the given classifier must be a Root
     *            classifier or not
     * @return
     * @throws SemanticallyIncorrectException
     * @throws RedundancyException
     * @throws IntegrityException
     */
    public Classifier build(boolean isRoot)
	    throws SemanticallyIncorrectException, RedundancyException, IntegrityException {
	logger.info("Building classifier: " + classifierInstance.getName());

	if (isRoot)
	    classifierInstance.setTypeToRoot();

	checkClassifierAndAdd();

	setSources();

	getPatternAndUpdate();

	return classifierInstance;
    }

    /**
     * Method generated so the subclasses could add they own internalClassifiers
     * fields
     * 
     * @throws SemanticallyIncorrectException
     * @throws IntegrityException
     * @throws RedundancyException
     */
    protected void checkClassifierAndAdd()
	    throws SemanticallyIncorrectException, IntegrityException, RedundancyException {
	Collection<ClassifierUnmarshaller> s = this.internalClassifiers;
	checkPosAndAddClassifier(s);
    }

    /**
     * Method to set the sources of the classifier
     * 
     * @throws SemanticallyIncorrectException
     */
    protected void setSources() throws SemanticallyIncorrectException {
	try {
	    classifierInstance.clearAndSetSources(IdToDomainObject.getSources((sources)));
	} catch (NotFoundException e) {
	    throw new SemanticallyIncorrectException("Invalid source id");
	}
    }

    /**
     * Given a Set of {@link ClassifierUnmarshaller} check if the field "pos" is
     * correct (the value is not repeated, and goes all throw 0...N, where N is the
     * number of classifiers).
     * 
     * @param s
     *            Collection of classifiers unmarshallers to add
     * @throws SemanticallyIncorrectException
     * @throws IntegrityException
     * @throws RedundancyException
     */
    protected void checkPosAndAddClassifier(Collection<ClassifierUnmarshaller> s)
	    throws SemanticallyIncorrectException, IntegrityException, RedundancyException {
	int internalsSize = s.size();
	boolean[] positions = new boolean[internalsSize]; // initialized a false
	for (ClassifierUnmarshaller tmp : s) {
	    int posAux = tmp.getPos();
	    if (posAux < 0 || posAux >= internalsSize) {
		throw new SemanticallyIncorrectException("Incorrect pos value");
	    } else {
		positions[posAux] = true;
	    }
	    Classifier c = tmp.build(false);
	    c.setParentClassifier(classifierInstance);
	    classifierInstance.addClassifier(c);
	}
	for (boolean b : positions) {
	    if (!b) throw new SemanticallyIncorrectException("Incorrect pos value");
	}
    }

    /**
     * Method used to set the patterns of the current classifier
     * 
     * @throws SemanticallyIncorrectException
     * @throws RedundancyException
     */
    protected void getPatternAndUpdate() throws SemanticallyIncorrectException, RedundancyException {

	Set<RequirementPattern> auxReqPatterns;
	try {
	    auxReqPatterns = IdToDomainObject.getReqPatterns(requirementPatterns);
	} catch (NotFoundException e) {
	    throw new SemanticallyIncorrectException("Invalid req pattern id");
	}
	classifierInstance.addPatterns(auxReqPatterns);
    }

    /**
     * Method used to check if all the innerclassifiers have the field ID assigned
     * and is not equals to 0
     * 
     * @return True if all the classifiers unmarshallers has the field id != 0
     */
    public boolean checkAllItemsContainsID() {
	boolean b = id != 0;
	for (ClassifierUnmarshaller classifierUnmarshaller : internalClassifiers) {
	    if (!classifierUnmarshaller.checkAllItemsContainsID()) {
		b = false;
	    }
	}
	return b;
    }

    @Override
    public int hashCode() {
	return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
	return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    public Set<Long> getSources() {
	return sources;
    }

    public void setSources(Set<Long> sources) {
	this.sources = sources;
    }

    public Set<Long> getRequirementPatterns() {
	return requirementPatterns;
    }

    public void setRequirementPatterns(Set<Long> requirementPatterns) {
	this.requirementPatterns = requirementPatterns;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public Set<ClassifierUnmarshaller> getInternalClassifiers() {
	return internalClassifiers;
    }

    public void setInternalClassifiers(Set<ClassifierUnmarshaller> internalClassifiers) {
	this.internalClassifiers = internalClassifiers;
    }

    public int getPos() {
	return pos;
    }

    public void setPos(int pos) {
	this.pos = pos;
    }

}
