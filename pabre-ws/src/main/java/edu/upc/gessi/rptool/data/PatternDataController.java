package edu.upc.gessi.rptool.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAException;

import edu.upc.gessi.rptool.data.mediators.MediatorConnection;
import edu.upc.gessi.rptool.data.mediators.MediatorGeneric;
import edu.upc.gessi.rptool.data.mediators.MediatorPatterns;
import edu.upc.gessi.rptool.domain.ExternalObject;
import edu.upc.gessi.rptool.domain.patternelements.Dependency;
import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;
import edu.upc.gessi.rptool.domain.patternelements.FixedPart;
import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementPatternDTO;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.utilities.Util;

/**
 * This class contains the controller for specific operations necessaries for
 * the all type of RequirementPatterns.
 */
@SuppressWarnings({ "unchecked", "unused" })
public final class PatternDataController extends GenericDataController {

    private static final Logger logger = Logger.getLogger(PatternDataController.class.getName());

    /**
     * Constructor has to be private so no one be able to create any instance
     */
    private PatternDataController() {
    }

    /**
     * Gets the RequirementPattern saved in the database with the id received as
     * parameter.
     * 
     * @param name
     *            The object's name that we want to get.
     * @return - The RequirementPattern saved in the database with the id received
     *         as parameter. - Null if doesn't exist any RequirementPattern with the
     *         id received as parameter
     */
    public static RequirementPattern getPattern(long id) {
	logger.debug("Getting pattern: " + id);
	return (RequirementPattern) MediatorGeneric.get(id, RequirementPattern.class);
    }

    /**
     * Gets the RequirementPattern String name saved in the database with the
     * identifier received as parameter.
     * 
     * @param id
     *            The object's identifier that we want to get.
     * @return - The String name saved in the database with the identifier received
     *         as parameter. - Null if doesn't exist any RequirementPattern with the
     *         identifier received as parameter
     */
    public static String getPatternName(long id) {
	return MediatorPatterns.getPatternName(id, RequirementPattern.class);
    }

    /**
     * Gets the RequirementPattern saved in the database with the name received as
     * parameter.
     * 
     * @param name
     *            The object's name that we want to get.
     * @return - The RequirementPattern saved in the database with the name received
     *         as parameter. - Null if doesn't exist any RequirementPattern with the
     *         name received as parameter
     */
    public static RequirementPattern getPattern(String name) {
	if (name == null)
	    return null;
	return (RequirementPattern) MediatorGeneric.get(name, RequirementPattern.class);
    }

    /**
     * Gets the {@link RequirementPattern} saved in the database with the given set
     * of ids as parameter
     * 
     * @param patterns
     *            Set of ids of the patterns to retreive
     * @return List of {@link RequirementPattern}
     */
    public static List<RequirementPattern> getPatterns(Collection<Long> patterns) {
	logger.debug("Getting patterns from a list with size: " + patterns.size());
	List<RequirementPattern> lrp = new ArrayList<>();
	for (Long long1 : patterns) {
	    lrp.add(getPattern(long1));
	}
	return lrp;
    }

    /**
     * Gets the RequirementPatternVersion saved in the database with the id received
     * as parameter.
     * 
     * @param name
     *            The object's name that we want to get.
     * @return - The RequirementPatternVersion saved in the database with the id
     *         received as parameter. - Null if doesn't exist any
     *         RequirementPatternVersion with the id received as parameter
     */
    public static RequirementPatternVersion getPatternVersion(long id) {
	return (RequirementPatternVersion) MediatorGeneric.get(id, RequirementPatternVersion.class);
    }

    /**
     * List all the RequirementPattern saved in the database
     * 
     * @return The list with all RequirementPattern saved in the database
     */
    public static List<RequirementPattern> listPatterns() {
	return MediatorGeneric.list(RequirementPattern.class);
    }

    /**
     * List names of the all RequirementPatterns saved in database.
     * 
     * @return The list with the names of the all RequirementPatterns saved in
     *         database.
     */
    public static List<String> listPatternNames() {
	return MediatorGeneric.listNames(RequirementPattern.class);
    }

    /**
     * List all the RequirementPattern saved in the database in which any of its
     * versions has the specified keyword
     * 
     * @return The list with all RequirementPatterns saved in the database
     */
    public static List<RequirementPattern> listPatternWithGivenKeyword(String keyword) {
	return MediatorPatterns.listPatternWithGivenKeyword(keyword);
    }

    /**
     * List all the dependencies of the RequirementPattern whose identifier is
     * received as parameter.
     * 
     * @param l
     *            The identifier of the RequirementPattern whose dependencies will
     *            be returned.
     * @return The list of dependencies of the RequirementPattern identified by the
     *         parameter.
     */
    public static List<Dependency> listPatternDependencies(long l) {
	return MediatorPatterns.listPatternDependencies(l, Dependency.class);
    }

    /**
     * List all the {@link RequirementPattern} saved in the catalogue having as
     * classifier the received classifier, also gets recursively the patterns of all
     * the inner classifiers. returns a list with unique patterns
     * 
     * @param ic
     *            Classifier to search
     * @return {@link List} of patterns obtained
     */
    public static List<RequirementPattern> listPatternsClassifierAndAllInnerClassifiers(Classifier ic) {

	// get patterns recursively
	List<RequirementPattern> listPatterns = PatternDataController.getPatternRecursiveClassifier(ic);

	// generate unique results
	List<RequirementPattern> l = new ArrayList<>(new TreeSet<>(listPatterns));

	return l;
    }

    /**
     * Given a {@link Classifier} obtains all the {@link RequirementPattern} inside
     * any of the inner classifier
     * 
     * @param ic
     *            classifier to search
     * @return List of patterns
     */
    private static List<RequirementPattern> getPatternRecursiveClassifier(Classifier ic) {
	List<RequirementPattern> lrp = new ArrayList<>();
	lrp.addAll(ic.getPatterns()); // Get all the patterns of the current classifier
	for (Classifier internalClassifiers : ic.getInternalClassifiers()) {
	    // for each classifier get they patterns
	    lrp.addAll(PatternDataController.getPatternRecursiveClassifier(internalClassifiers));
	}
	return lrp;

    }

    // PATTERN SAVING METHODS

    /**
     * Saves the given pattern into the Catalogue
     * 
     * @param p
     *            Pattern to be saved
     * @throws IntegrityException
     * @throws UIMAException
     *             Exception related to lemmatization
     */
    public static void saveNewPattern(RequirementPattern p) throws IntegrityException, UIMAException {
	logger.debug("Entrying in saveNewPattern");

	Set<RequirementPatternVersion> versions = new HashSet<>(p.getVersions()); // Create a copy of the version
	p.getVersions().clear(); // clear all the version, to avoid the row updated by multiple places
	MediatorGeneric.save(p); // save the pattern without the version

	saveNewVersionsIntoPattern(p, versions); // call the method to save the version into the pattern
	MediatorConnection.flush();
    }

    /**
     * Given a pattern and a collection of versions, saves all the version inside
     * the collection into the database and add that version to the given pattern
     * 
     * @param p
     *            Pattern where the version would be saved
     * @param versions
     *            {@link Collection} with all the versions to save in the database
     * @throws IntegrityException
     * @throws UIMAException
     *             Exception related to the lemmatization
     */
    public static void saveNewVersionsIntoPattern(RequirementPattern p, Collection<RequirementPatternVersion> versions)
	    throws IntegrityException, UIMAException {
	logger.debug("Entrying in addNewVersionIntoPattern");
	for (RequirementPatternVersion rpv : versions) {
	    logger.debug("Clearing the forms to save the version");
	    Collection<RequirementForm> forms = new HashSet<>(rpv.getForms());
	    rpv.getForms().clear();

	    logger.debug("saving PatternVersion");
	    rpv.setRequirementPattern(null);

	    MediatorGeneric.save(rpv);

	    logger.debug("Saving the external Objects");
	    saveExternalObjects(rpv.getExternalObjects());

	    logger.debug("Adding version to the pattern");
	    p.addVersion(rpv);
	    rpv.setRequirementPattern(p);

	    logger.debug("Save and add all the forms");
	    saveNewFormsIntoVersion(rpv, forms);

	    logger.debug("Updating Pattern");
	    MediatorGeneric.saveOrUpdate(p);
	}
	MediatorGeneric.update(p);
    }

    /**
     * Given a {@link Collection} of external objects, saves them into the database
     * 
     * @param externalObjects
     *            {@link ExternalObject} to be saved
     */
    private static void saveExternalObjects(Collection<ExternalObject> externalObjects) {
	for (ExternalObject externalObject : externalObjects) {
	    MediatorGeneric.save(externalObject);
	}
    }

    /**
     * Given a Pattern version and a collection of forms, save all the forms into
     * the database and add form into the given version
     * 
     * @param rpv
     *            Version where the forms would be saved
     * @param forms
     *            Collection with all the forms to be saved
     * @throws IntegrityException
     * @throws UIMAException
     */
    private static void saveNewFormsIntoVersion(RequirementPatternVersion rpv, Collection<RequirementForm> forms)
	    throws IntegrityException, UIMAException {
	logger.debug("Entrying in saveNewForms");
	for (RequirementForm rf : forms) {
	    FixedPart fp = rf.getFixedPart();
	    // Save Fixed part parameters
	    saveNewParameters(fp.getParameters());
	    // Save fixed part
	    MediatorGeneric.save(fp);
	    // Save Extended part parameters
	    saveNewExtendedParts(rf.getExtendedParts());
	    // Save extended part
	    MediatorGeneric.save(rf);
	    // Add form to the version
	    rpv.addForm(rf);

	}
	long startTime = System.nanoTime();
	rpv.setLemmatizedVersion(NLPController.lemmatizeVersion(rpv));
	long endTime = System.nanoTime();
	long duration = (endTime - startTime) / 1000000; // divide by 1000000 to get milliseconds.
	logger.info("Version lemmatized in : " + duration);
    }

    /**
     * Persist all the parameters given by parameter
     * 
     * @param parameters
     *            Parameters to be persisted
     */
    private static void saveNewParameters(Collection<Parameter> parameters) {
	logger.debug("Entrying in saveNewParameters");
	if (parameters != null) {
	    for (Parameter p : parameters) {
		MediatorGeneric.save(p);
	    }
	}
    }

    /**
     * Persist all the extended parts passed by parameter
     * 
     * @param extendedParts
     *            Parts to be persisted
     */
    private static void saveNewExtendedParts(Collection<ExtendedPart> extendedParts) {
	for (ExtendedPart ep : extendedParts) {
	    saveNewParameters(ep.getParameters());
	    MediatorGeneric.save(ep);
	}
    }

    // SUBSTITUTE METHODS

    public static void substituteNewPartsIntoForm(Collection<ExtendedPart> extendedParts, RequirementForm rf) {
	rf.getExtendedParts().clear();
	saveNewExtendedParts(extendedParts);
	MediatorConnection.flush();
	rf.getExtendedParts().addAll(extendedParts);
	MediatorGeneric.update(rf);

    }

    public static void substituteNewFormsIntoVersion(Collection<RequirementForm> forms, RequirementPatternVersion rpv)
	    throws IntegrityException, UIMAException {
	rpv.getForms().clear();
	saveNewFormsIntoVersion(rpv, forms);
	MediatorGeneric.update(rpv);
    }

    // UNBINDED

    /**
     * Get all the unbinded (dosen't belong to any classifier) patterns in the
     * catalogue
     * 
     * @param unbindedPatterns
     *            Collection where the pattern DTO will be added
     */
    public static void getUnbindedPatternsDTO(Collection<RequirementPatternDTO> unbindedPatterns) {
	List<RequirementPattern> lrp = MediatorPatterns.listPatternsWithoutClassifiers();
	lrp = Util.uniqueList(lrp);
	for (RequirementPattern rp : lrp) {
	    RequirementPatternDTO rpdto = new RequirementPatternDTO(rp);
	    rpdto.reduceFields();
	    unbindedPatterns.add(rpdto);
	}

    }

    // DELETE METHODS

    /**
     * Given a Pattern, this method delete it from the database
     * 
     * @param p
     *            Pattern to be deleted
     * @throws NullPointerException
     * @throws SemanticallyIncorrectException
     */
    public static void deletePattern(RequirementPattern p) throws NullPointerException, SemanticallyIncorrectException {
	deletePatternClassifiers(p);

	// deletePatternVersions(p);
	MediatorGeneric.delete(p);
    }

    private static void deletePatternClassifiers(RequirementPattern p) throws SemanticallyIncorrectException {

	for (Classifier c : p.getClassifiers()) {
	    c.removePattern(p);
	    // p.removeClassifier(c);
	}
	p.getClassifiers().clear();
	PatternDataController.update(p);

    }

    /**
     * Delete all the versions of the given pattern
     * 
     * @param p
     *            Pattern
     * @throws NullPointerException
     */
    private static void deletePatternVersions(RequirementPattern p) throws NullPointerException {
	if (p == null)
	    throw new NullPointerException("Delete operation over null objects is not allowed");
	for (RequirementPatternVersion rpv : p.getVersions()) {
	    deletePatternVersion(rpv);
	}
	p.getVersions().clear();
    }

    /**
     * Delete a version of a pattern and remove if fro mthe pattern version list
     * 
     * @param rp
     *            Pattern from where we have to delete
     * @param v
     *            version to delete
     */
    public static void deletePatternVersion(RequirementPatternVersion v) {
	deleteVersionForms(v);
	MediatorGeneric.delete(v);
    }

    /**
     * Delete all the forms of the given version
     * 
     * @param ver
     *            Version where to clean the forms
     */
    public static void deleteVersionForms(RequirementPatternVersion ver) {
	for (RequirementForm rf : ver.getForms()) {
	    deleteForm(rf);
	}
    }

    /**
     * Delete a Form
     * 
     * @param f
     *            Form to be deleted
     */
    public static void deleteForm(RequirementForm f) {
	MediatorGeneric.delete(f);
    }

    /**
     * Delete a collection of extended parts.
     * 
     * @param eParts
     *            Extended parts to be deleted
     */
    private static void deleteExtendedParts(Collection<ExtendedPart> extendedParts) {
	for (ExtendedPart k : extendedParts) {
	    for (Parameter param : k.getParameters()) {
		MediatorGeneric.delete(param);
	    }
	    MediatorGeneric.delete(k);
	}
    }

}
