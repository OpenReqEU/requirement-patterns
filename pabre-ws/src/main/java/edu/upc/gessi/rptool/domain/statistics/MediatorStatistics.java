package edu.upc.gessi.rptool.domain.statistics;

import java.util.List;

import org.hibernate.Session;

import edu.upc.gessi.rptool.data.mediators.MediatorConnection;
import edu.upc.gessi.rptool.data.mediators.MediatorGeneric;

/*
 * This class contains all specific operations 
 * necessaries to get, save, update and delete in 
 * database all type of statistic classes.
 */
@SuppressWarnings("rawtypes")
public final class MediatorStatistics extends MediatorGeneric {

    /**
     * Constructor has to be private so no one be able to create any instance
     */
    private MediatorStatistics() {
    }

    /**
     * Get the list of PatternAssociatedRequirementData associated with the
     * RequirementPattern identified by the id received as parameter.
     * 
     * @return The list of PatternAssociatedRequirementData.
     */
    public static List listPatternAssociatedRequirements(long id) {
	Session session = MediatorConnection.getCurrentSession();
	String query = new StringBuilder("select pard").append(" from PatternAssociatedRequirementData as pard")
		.append(" inner join pard.requirementPattern as rp").append(" where rp.id=").append(id).toString();
	List requirements = session.createQuery(query).list();
	return requirements;
    }

    /**
     * Get the list of FormAssociatedRequirementData associated with the
     * RequirementForm identified by the id received as parameter.
     * 
     * @return The list of FormAssociatedRequirementData.
     */
    public static List listFormAssociatedRequirements(long id) {
	Session session = MediatorConnection.getCurrentSession();
	String query = new StringBuilder("select fard").append(" from FormAssociatedRequirementData as fard")
		.append(" inner join fard.requirementForm as rf").append(" where rf.id=").append(id).toString();
	List requirements = session.createQuery(query).list();
	return requirements;
    }

    /**
     * Get the list of NewRequirementData.
     * 
     * @return The list of NewRequirementData.
     */
    public static List listNewRequirements() {
	Session session = MediatorConnection.getCurrentSession();
	String query = new StringBuilder("select nrd").append(" from NewRequirementData as nrd").toString();
	List requirements = session.createQuery(query).list();
	return requirements;
    }

    /**
     * Get the list of all InstancedRequirementData.
     * 
     * @return The list of all InstancedRequirementData.
     */
    public static List listInstancedRequirements() {
	Session session = MediatorConnection.getCurrentSession();
	String query = new StringBuilder("select ird").append(" from InstancedRequirementData as ird").toString();
	List requirements = session.createQuery(query).list();
	return requirements;
    }

    /**
     * Get the list of all NoInstancedRequirementData.
     * 
     * @return The list of all NoInstancedRequirementData.
     */
    public static List listNoInstancedRequirements() {
	Session session = MediatorConnection.getCurrentSession();
	String query = new StringBuilder("select nird").append(" from NoInstancedRequirementData as nird").toString();
	List requirements = session.createQuery(query).list();
	return requirements;
    }

    /**
     * Get the list with the InstancedRequirementData corresponding with all
     * parameters restrictions
     * 
     * @param projectName
     *            Value of attribute projectName the searched
     *            InstancedRequirementData must satisfy
     * @param schemaId
     *            Id of classification schema the searched InstancedRequirementData
     *            must be associated
     * @param generalClassifierId
     *            Id of general classifier the searched InstancedRequirementData
     *            must be associated
     * @param partId
     *            Id of PatternItem the searched InstancedRequirementData must be
     *            associated
     * @param session
     *            Session to work with in this query
     * @return The InstancedRequirementData corresponding with all parameters
     *         restrictions
     */
    public static List loadInstancedRequirementDataInSession(String projectName, long schemaId,
	    long generalClassifierId, long partId, Session session) {
	String query = new StringBuilder("select ird").append(" from InstancedRequirementData as ird")
		.append(" where ird.projectName = '").append(projectName).append("'")
		.append(" and ird.classificationSchema.id = ").append(schemaId)
		.append(" and ird.generalClassifier.id = ").append(generalClassifierId)
		.append(" and ird.isInstanceOf = ").append(partId).toString();

	return session.createQuery(query).list();
    }

}
