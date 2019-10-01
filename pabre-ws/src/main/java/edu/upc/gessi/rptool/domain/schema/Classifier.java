package edu.upc.gessi.rptool.domain.schema;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.states.ClassifierBasicState;
import edu.upc.gessi.rptool.domain.schema.states.ClassifierDecomposedState;
import edu.upc.gessi.rptool.domain.schema.states.ClassifierGeneralState;
import edu.upc.gessi.rptool.domain.schema.states.ClassifierRootState;
import edu.upc.gessi.rptool.domain.schema.states.IClassifierState;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

/**
 * This class represents a classifier.
 * <p>
 * It can be 4 types of the classifier
 * <ul>
 * <li>BASIC: Contains {@link RequirementPattern} pattern</li>
 * <li>DECOMPOSED: Contains more {@link Classifier} inside</li>
 * <li>GENERAL: Doesn't contains anything, it's the last classifier in the
 * tree</li>
 * <li>ROOT: This type of classifier represent the parents of the classifiers,
 * contains only classifiers and has a reference to the schema where they
 * belong</li>
 * </ul>
 * </p>
 * 
 * @author Awais Iqbal
 *
 */
@Entity
@Table(name = "CLASSIFIERS")
public class Classifier extends ClassificationObject {

    /*
     * ATTRIBUTES
     */

    static final Logger logger = Logger.getLogger(Classifier.class);

    // Constants of every type of classifier
    public static final int ROOT = 0;
    public static final int BASIC = 1;
    public static final int DECOMPOSED = 2;
    public static final int GENERAL = 3;

    @Column(name = "NAME", nullable = false)
    private String name;

    /**
     * This attribute indicates the type of the current state using the constants
     * indicated in this class ({@link Classifier#ROOT}, {@link Classifier#BASIC},
     * {@link Classifier#DECOMPOSED}, {@link Classifier#GENERAL})
     */
    @Column(name = "TYPE", nullable = false)
    private int type;

    /**
     * This attribute indicates the position of this classifier inside the parent
     * classifier
     */
    @Column(name = "POS", nullable = false)
    protected int pos;

    /**
     * This attribute constains all the classifiers inside the current classifier.
     */
    @OneToMany(mappedBy = "parentClassifier", orphanRemoval = true, fetch = FetchType.EAGER, cascade = {
	    CascadeType.ALL })
    private Set<Classifier> internalClassifiers;

    /**
     * Attribute referencing to the parent classifier, null if is a RootCLassifier
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "PARENT_CLASSIFIER")
    private Classifier parentClassifier;

    /**
     * This attribute reference to the patterns contained inside this classifier.
     */
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
	    CascadeType.REFRESH }, fetch = FetchType.EAGER, targetEntity = RequirementPattern.class)
    @JoinTable(name = "CLASSIFIER_PATTERN", joinColumns = {
	    @JoinColumn(name = "CLASSIFIER_ID") }, inverseJoinColumns = { @JoinColumn(name = "PATTERN_ID") })
    private Set<RequirementPattern> patterns;

    /**
     * Attribute referencing to the parent schema, it will be null if this
     * classifier is not a RootClassifier
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARENT_SCHEMA")
    private ClassificationSchema parentSchema;

    /**
     * This attributes represents the instance of the current state, depending on
     * the type of the classifier
     */
    @Transient
    private IClassifierState currentState;

    /*
     * CONTRUCTORS
     */

    /**
     * Instantiates a new internal classifier of type general.
     * 
     * @throws SemanticallyIncorrectException
     */
    public Classifier() throws SemanticallyIncorrectException {
	this.internalClassifiers = new HashSet<>();
	this.patterns = new HashSet<>();
	setType(GENERAL);// Default type is General
    }

    /*
     * OTHER METHODS
     */

    /**
     * Check if a classifier already contains a classifier with the given ID
     * 
     * @param id
     *            ID to search
     * @return True if the classifier already contains that ID, false otherwise
     */
    public boolean alreadyHas(long id) {
	return currentState.alreadyHas(id);
    }

    /**
     * This method will add the given classifier to the this classifier, In case
     * that the classifier already a pattern throws
     * {@link SemanticallyIncorrectException} and if this classifier already a
     * classifier with the same name throws {@link IntegrityException}
     * 
     * @param child
     *            The internal classifier that we will add to this classifier
     * @throws IntegrityException
     *             We launch an integrity exception when this decomposed classifier
     *             already contains another internal classifier with the same name
     * @throws SemanticallyIncorrectException
     *             Exception happen if you try to add a pattern to the root
     *             classifier or a basic classifier
     */
    public void addClassifier(Classifier child) throws IntegrityException, SemanticallyIncorrectException {
	currentState.addClassifier(child);
    }

    /**
     * This method will remove the classifier from this classifier, after removing
     * the classifier it will readjust the type of the classifier
     * 
     * @param child
     *            The internal classifier that we will remove to this classifier
     * @throws SemanticallyIncorrectException
     */
    public void removeClassifier(Classifier child) throws SemanticallyIncorrectException {
	currentState.removeClassifier(child);
    }

    /**
     * This method adds a pattern to this classifier, after adding the pattern it
     * will readjust it type
     * 
     * @param pattern
     *            Pattern to add to this classifier
     * @throws SemanticallyIncorrectException
     *             Exception throwen when you try to remove a pattern from a
     *             classifier that cannot have a pattern
     */
    public void addPattern(RequirementPattern pattern) throws SemanticallyIncorrectException {
	currentState.addPattern(pattern);
    }

    /**
     * This method removesa pattern from this classifier
     * 
     * @param pattern
     *            Pattern to remove
     * @throws SemanticallyIncorrectException
     */
    public void removePattern(RequirementPattern pattern) throws SemanticallyIncorrectException {
	currentState.removePattern(pattern);
    }

    /**
     * Add a collection of patterns to this classifier
     * 
     * @param patterns
     *            Pattern to add
     * @throws SemanticallyIncorrectException
     */
    public void addPatterns(Collection<RequirementPattern> patterns) throws SemanticallyIncorrectException {
	for (RequirementPattern requirementPattern : patterns) {
	    addPattern(requirementPattern);
	}
    }

    /**
     * Method used to know if this Internal Classifier already contains a Internal
     * Classifier with the name n.
     * 
     * @param name
     *            The name that we want to test
     * @return - True, if any other internal classifier of this classifier have the
     *         given name - False, in another case
     */
    public boolean containsClassifierSameName(String name) {
	for (Classifier internalClassifier : internalClassifiers) {
	    if (internalClassifier.getName().equals(name)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * This method is used to check and readjust the type of the classifier, should
     * be called after any action
     * 
     * @throws SemanticallyIncorrectException
     */
    public void readjustType() throws SemanticallyIncorrectException {
	// if the type is root, don't need to readjust because is always ROOT
	if (type == ROOT) {
	    return;
	}
	boolean containsPatterns = !patterns.isEmpty();
	boolean containsClassifiers = !internalClassifiers.isEmpty();
	if (containsPatterns && !containsClassifiers && type != BASIC) {
	    setTypeToBasic();
	} else if (!containsPatterns && containsClassifiers && type != DECOMPOSED) {
	    setTypeToDescomposed();
	} else if (!containsClassifiers && !containsPatterns && type != GENERAL) {
	    setTypeToGeneral();
	}
    }

    /**
     * This method check if the classifier type is in correct range
     * 
     * @param t
     *            type to check
     * @return
     */
    public boolean checkCorrectType(int t) {
	return t == GENERAL || t == BASIC || t == DECOMPOSED || t == ROOT;
    }

    /**
     * This method clears all the existing classifiers (deleting the reference),
     * readjust the type and add the collection of classifiers given in the
     * parameter
     * 
     * @param s
     *            Classifiers to add
     * @throws SemanticallyIncorrectException
     * @throws IntegrityException
     */
    public void clearAndSetClassifiers(Collection<Classifier> s)
	    throws SemanticallyIncorrectException, IntegrityException {
	currentState.clearClassifiers();
	readjustType();
	for (Classifier classifier : s) {
	    currentState.addClassifier(classifier);
	}
    }

    /**
     * This method is used to clear all the inners classifiers of the this
     * classifier
     * 
     * @throws SemanticallyIncorrectException
     *             Exception thrown when this method is called over a classifier
     *             that cannot have inner classifiers
     */
    public void clearInternalsClassifiers() throws SemanticallyIncorrectException {
	this.internalClassifiers.clear();
	readjustType();
    }

    /**
     * This method clears all the pattern inside this classifier and add the given
     * collection in the parameter
     * 
     * @param s
     *            Patterns to add
     * @throws SemanticallyIncorrectException
     */
    public void clearAndSetPatterns(Set<RequirementPattern> s) throws SemanticallyIncorrectException {
	clearPatterns();
	addPatterns(s);
    }

    /**
     * This method clears the patterns and readjust the type of the pattern
     * 
     * @throws SemanticallyIncorrectException
     *             Exception thrown when this method is called on a classifier that
     *             cannot have patterns
     */
    public void clearPatterns() throws SemanticallyIncorrectException {
	this.patterns.clear();
	readjustType();
    }

    /**
     * Set the type of this classifier to {@link Classifier#ROOT}
     * 
     * @throws SemanticallyIncorrectException
     */
    public void setTypeToRoot() throws SemanticallyIncorrectException {
	setType(ROOT);
    }

    /**
     * Set the type of this classifier to {@link Classifier#BASIC}
     * 
     * @throws SemanticallyIncorrectException
     */
    public void setTypeToBasic() throws SemanticallyIncorrectException {
	setType(BASIC);
    }

    /**
     * Set the type of this classifier to {@link Classifier#GENERAL}
     * 
     * @throws SemanticallyIncorrectException
     */
    public void setTypeToGeneral() throws SemanticallyIncorrectException {
	setType(GENERAL);
    }

    /**
     * Set the type of this classifier to {@link Classifier#DECOMPOSED}
     * 
     * @throws SemanticallyIncorrectException
     */
    public void setTypeToDescomposed() throws SemanticallyIncorrectException {
	setType(DECOMPOSED);
    }

    /**
     * Method used to create a new Classifier with the same state (same value for
     * his attributes) than original.
     * 
     * @return New {@link Classifier} with the same value for his attributes then
     *         the this classifier
     * @throws Exception
     */
    public Classifier copy() throws IntegrityException, SemanticallyIncorrectException {
	return currentState.copy();
    }

    /**
     * Get the last classifier in given list of names
     * 
     * @param namesList
     *            List of names of the classifiers to go throw
     * @return Classifier of the last name in the list
     */
    public Classifier getTheLastInternalsClassifiersIncludedInNames(List<String> namesList) {
	Classifier ic = null; // when the nameList as 1 element is the current InternalCLassifier
	for (Classifier f : this.getInternalClassifiers()) {
	    if (f.getName().equals(namesList.get(0))) {
		ic = f;
		break;
	    }
	}
	if (namesList.size() >= 2 && ic != null) { // if we need to go deep call recursively
	    ic = ic.getTheLastInternalsClassifiersIncludedInNames(namesList.subList(1, namesList.size()));
	}
	return ic;
    }

    /*
     * GET'S AND SET'S METHODS
     */

    /**
     * Set the type of this classifier, first check if the value is correct, after
     * that set the type and creates a instace of the corresponding state
     * 
     * @param t
     * @throws SemanticallyIncorrectException
     */
    public void setType(int t) throws SemanticallyIncorrectException {
	if (!checkCorrectType(t)) {
	    throw new SemanticallyIncorrectException("Incorrect type value");
	}
	this.type = t;
	switch (t) {
	case Classifier.ROOT:
	    this.currentState = new ClassifierRootState(this);
	    break;
	case Classifier.BASIC:
	    this.currentState = new ClassifierBasicState(this);
	    break;
	case Classifier.GENERAL:
	    this.currentState = new ClassifierGeneralState(this);
	    break;
	case Classifier.DECOMPOSED:
	    this.currentState = new ClassifierDecomposedState(this);
	    break;
	default:
	    logger.error("Incorrect type");
	    break;
	}
    }

    public String getName() {
	return name;
    }

    public void setName(String newname) throws IntegrityException {
	if (newname == null || newname.trim().length() == 0)
	    throw new IntegrityException("You have to introduce a name");
	name = newname;
    }

    public int getType() throws SemanticallyIncorrectException {
	readjustType();
	return type;
    }

    public Set<Classifier> getInternalClassifiers() {
	return internalClassifiers;
    }

    public void setInternalClassifiers(Set<Classifier> s) {
	this.internalClassifiers = s;
    }

    public void setPos(int n) {
	pos = n;
    }

    public Integer getPos() {
	return pos;
    }

    public Set<RequirementPattern> getPatterns() {
	return patterns;
    }

    public void setPatterns(Set<RequirementPattern> patterns) {
	this.patterns = patterns;
    }

    public Classifier getParentClassifier() {
	return parentClassifier;
    }

    public void setParentClassifier(Classifier parentClassifier) {
	this.parentClassifier = parentClassifier;
    }

    public ClassificationSchema getParentSchema() {
	return parentSchema;
    }

    public void setParentSchema(ClassificationSchema parentSchema) {
	this.parentSchema = parentSchema;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,parentClassifier,type);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Classifier other = (Classifier) obj;
	if (name == null) {
	    if (other.name != null) return false;
	} else if (!name.equals(other.name))
	    return false;
	if (parentClassifier == null) {
	    if (other.parentClassifier != null) return false;
	} else if (!parentClassifier.equals(other.parentClassifier))
	    return false;
        return type == other.type;
    }

}