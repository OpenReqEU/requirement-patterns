package edu.upc.gessi.rptool.domain.statistics;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.data.mediators.MediatorConnection;
import edu.upc.gessi.rptool.data.mediators.MediatorGeneric;
import edu.upc.gessi.rptool.data.mediators.MediatorPatterns;
import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;
import edu.upc.gessi.rptool.domain.patternelements.FixedPart;
import edu.upc.gessi.rptool.domain.patternelements.PatternItem;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;

/**
 * PENDING TO BE REVIEWED FOR THE NEW PATTERNOBJECT VERSION This class contains
 * the controller for specific operations necessaries for the all type of
 * statistic classes.
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public final class StatisticsDataController extends GenericDataController {

    /**
     * Constructor has to be private so no one be able to create any instance
     */
    private StatisticsDataController() {
    }

    /**
     * List all the PatternAssociatedRequirementData saved in the database having as
     * RequirementPattern the one received as parameter
     * 
     * @param The
     *            RequirementPattern whose PatternAssociatedRequirementData the
     *            method has to return.
     * @return The list with all PatternAssociatedRequirementData saved in the
     *         database having as RequirementPattern the one received as parameter
     */
    public static List<PatternAssociatedRequirementData> listPatternAssociatedRequirements(RequirementPattern p) {
	if (p == null)
	    return null;

	return MediatorStatistics.listPatternAssociatedRequirements(p.getId());
    }

    /**
     * List all the FormAssociatedRequirementData saved in the database having as
     * RequirementForm the one received as parameter
     * 
     * @param The
     *            RequirementForm whose FormAssociatedRequirementData the method has
     *            to return.
     * @return The list with all FormAssociatedRequirementData saved in the database
     *         having as RequirementForm the one received as parameter
     */
    public static List<FormAssociatedRequirementData> listFormAssociatedRequirements(RequirementForm f) {
	if (f == null)
	    return null;

	return MediatorStatistics.listFormAssociatedRequirements(f.getId());
    }

    /**
     * List all the NewRequirementData saved in the database
     * 
     * @return The list with all NewRequirementData saved in the database
     */
    public static List<NewRequirementData> listNewRequirements() {
	return MediatorStatistics.listNewRequirements();
    }

    /**
     * Update in database all PatternAssociatedRequirements in the list received as
     * parameter
     * 
     * @param requirements
     *            List with all PatternAssociatedRequirements has to be updated
     */
    public static void updatePatternAssociatedRequirements(List<PatternAssociatedRequirementData> requirements) {

	if (requirements != null && requirements.size() > 0) {
	    Session session = null;

	    try {
		session = MediatorConnection.beginTransaction();

		for (PatternAssociatedRequirementData req : requirements) {
		    MediatorGeneric.updateInSession(req, session);
		}

		MediatorConnection.endTransaction(session);
	    } catch (HibernateException e) {
		e.printStackTrace();
		if (session != null && session.isOpen() && session.getTransaction() != null)
		    session.getTransaction().rollback();
		throw e;
	    }
	}
    }

    /**
     * Update in database all FormAssociatedRequirements in the list received as
     * parameter
     * 
     * @param requirements
     *            List with all FormAssociatedRequirements has to be updated
     */
    public static void updateFormAssociatedRequirements(List<FormAssociatedRequirementData> requirements) {

	if (requirements != null && requirements.size() > 0) {
	    Session session = null;

	    try {
		session = MediatorConnection.beginTransaction();

		for (FormAssociatedRequirementData req : requirements) {
		    MediatorGeneric.updateInSession(req, session);
		}

		MediatorConnection.endTransaction(session);
	    } catch (HibernateException e) {
		e.printStackTrace();
		if (session != null && session.isOpen() && session.getTransaction() != null)
		    session.getTransaction().rollback();
		throw e;
	    }
	}
    }

    /**
     * Update in database all NewRequirements in the list received as parameter
     * 
     * @param requirements
     *            List with all NewRequirements has to be updated
     */
    public static void updateNewRequirements(List<NewRequirementData> requirements) {

	if (requirements != null && requirements.size() > 0) {
	    Session session = null;

	    try {
		session = MediatorConnection.beginTransaction();

		for (NewRequirementData req : requirements) {
		    MediatorGeneric.updateInSession(req, session);
		}

		MediatorConnection.endTransaction(session);
	    } catch (HibernateException e) {
		e.printStackTrace();
		if (session != null && session.isOpen() && session.getTransaction() != null)
		    session.getTransaction().rollback();
		throw e;
	    }
	}
    }

    /**
     * List all the InstancedRequirementData saved in the database
     * 
     * @return The list with all InstancedRequirementData saved in the database.
     */
    public static List<InstancedRequirementData> listInstancedRequirements() {
	return MediatorStatistics.listInstancedRequirements();
    }

    /**
     * List all the NoInstancedRequirementData saved in the database, including all
     * PatternAssociatedRequirementData, FormAssociatedRequirementData and
     * NewRequirementData
     * 
     * @return The list with all NoInstancedRequirementData saved in the database
     */
    public static List<NoInstancedRequirementData> listNoInstancedRequirements() {
	return MediatorStatistics.listNoInstancedRequirements();
    }

    /**
     * Updates in database statistic instances attributes from objects received as
     * parameter. Also checks if the parameter requirementData already exists in
     * database. If exists, updates the existing one adding its number of uses, if
     * not, saves in database the one received.
     * 
     * @param pattern
     *            RequirementPattern whose statistic instances attribute has to be
     *            updated
     * @param version
     *            RequirementPatternVersion whose statistic instances attribute has
     *            to be updated
     * @param form
     *            RequirementForm whose statistic instances attribute has to be
     *            updated
     * @param part
     *            PatternItem whose statistic instances attribute has to be updated
     * @param requirementData
     *            InstancedRequirementData to update or save
     */
    public static void updateInstancedStats(RequirementPattern pattern, RequirementPatternVersion version,
	    RequirementForm form, PatternItem part, InstancedRequirementData instancedReq) {
	if (pattern == null || version == null || form == null || part == null || instancedReq == null)
	    throw new NullPointerException("Update operation over null objects is not allowed");

	Session session = null;
	try {
	    session = MediatorConnection.beginTransaction();

	    MediatorGeneric.updateInSession(pattern, session);
	    MediatorGeneric.updateInSession(version, session);
	    MediatorGeneric.updateInSession(form, session);
	    MediatorGeneric.updateInSession(part, session);

	    List<InstancedRequirementData> requirementDataListFromDB = MediatorStatistics
		    .loadInstancedRequirementDataInSession(instancedReq.getProjectName(),
			    instancedReq.getClassificationSchema().getId(), instancedReq.getGeneralClassifier().getId(),
			    part.getId(), session);

	    if (requirementDataListFromDB == null || requirementDataListFromDB.isEmpty()) {

		// If instanced requirement data don't exists, save it
		MediatorGeneric.saveInSession(instancedReq, session);
	    } else {

		// If instanced requirement data already exists, update it
		InstancedRequirementData requirementDataFromDB = requirementDataListFromDB.get(0);
		requirementDataFromDB.setNumInstancesInProject(
			requirementDataFromDB.getNumInstancesInProject() + instancedReq.getNumInstancesInProject());
		MediatorGeneric.updateInSession(requirementDataFromDB, session);
	    }

	    MediatorConnection.endTransaction(session);
	} catch (HibernateException e) {
	    e.printStackTrace();
	    if (session != null && session.isOpen() && session.getTransaction() != null)
		session.getTransaction().rollback();
	    throw e;
	}
    }

    /**
     * Updates in database statistic associates attribute from object received as
     * parameter. Also saves new requirement data associated to this object.
     * 
     * @param o
     *            Object whose statistic associates attribute has to be updated.
     *            Will be instances of RequirementPattern or RequirementForm
     *            classes.
     * @param requirementData
     *            Requirement data associated to object o.
     */
    public static void updateAssociatedStats(GenericObject o, NoInstancedRequirementData requirementData) {
	if (o == null || requirementData == null)
	    throw new NullPointerException("Update operation over null objects is not allowed");

	Session session = null;
	try {
	    session = MediatorConnection.beginTransaction();

	    MediatorGeneric.updateInSession(o, session);
	    MediatorGeneric.saveInSession(requirementData, session);

	    MediatorConnection.endTransaction(session);
	} catch (HibernateException e) {
	    e.printStackTrace();
	    if (session != null && session.isOpen() && session.getTransaction() != null)
		session.getTransaction().rollback();
	    throw e;
	}
    }

    /**
     * Loads from database the RequirementForm associated to the instanced
     * requirement received as parameter.
     * 
     * @param instancedReq
     *            InstancedRequirementData whose RequirementForm will be returned
     * @return RequirementForm associated with InstancedRequirementData received as
     *         parameter.
     */
    public static RequirementForm loadFormFromInstancedRequirement(InstancedRequirementData instancedReq) {
	RequirementForm form = null;
	PatternItem part = instancedReq.getIsInstanceOf();

	if (part == null) {
	    return null;

	} else if (part instanceof FixedPart) {
	    form = (RequirementForm) MediatorPatterns.getRequirementFormFromFixedPart(part.getId());

	} else if (part instanceof ExtendedPart) {
	    long formId = MediatorPatterns.getRequirementFormFromExtendedPart(part.getId());
	    form = (RequirementForm) MediatorGeneric.get(formId, RequirementForm.class);
	}

	return form;
    }

    /**
     * Loads from database the name of RequirementPattern associated to the
     * RequirementForm received as parameter.
     * 
     * @param form
     *            RequirementForm whose RequirementPattern name will be returned
     * @return String The name of the RequirementPattern associated with
     *         RequirementForm received as parameter.
     */
    public static String loadRequirementPatternNameFromForm(RequirementForm form) {

	long patternId = MediatorPatterns.getRequirementPatternFromRequirementForm(form.getId());

	String patternName = MediatorPatterns.getPatternName(patternId, RequirementPattern.class);
	return patternName;
    }

}
