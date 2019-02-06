package edu.upc.gessi.rptool.data;

import java.util.List;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.upc.gessi.rptool.data.mediators.MediatorConnection;
import edu.upc.gessi.rptool.data.mediators.MediatorGeneric;
import edu.upc.gessi.rptool.domain.CostFunction;
import edu.upc.gessi.rptool.domain.PatternObjectDependency;
import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.patternelements.Keyword;
import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.domain.patternelements.PatternItem;
import edu.upc.gessi.rptool.domain.patternelements.PatternObject;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.exceptions.IntegrityException;

/**
 * This class contains the controller for specific operations necessaries for
 * the {@link Keyword}, {@link Source}, {@link Parameter}, {@link PatternItem}
 * and {@link CostFunction}.
 */
@SuppressWarnings("unchecked")
public final class ObjectDataController extends GenericDataController {

    /**
     * Constructor has to be private so no one be able to create any instance
     */
    private ObjectDataController() {
    }

    public static Source getSource(String identifier) {
	return (Source) MediatorGeneric.get("identifier", identifier, Source.class);
    }

    public static Source getSource(String identifier, Session session) {
	return (Source) MediatorGeneric.get("identifier", identifier, Source.class, session);
    }

    public static Source getSource(long id) {
	try {
	    return (Source) MediatorGeneric.get(id, Source.class);
	} catch (HibernateException e) {
	    return null;
	}
    }

    public static CostFunction getCostFunction(long id) {
	try {
	    return (CostFunction) MediatorGeneric.get(id, CostFunction.class);
	} catch (HibernateException e) {
	    return null;
	}
    }

    public static Keyword getKeyword(String name) {
	return (Keyword) MediatorGeneric.get("name", name, Keyword.class);
    }

    public static Keyword getKeyword(String name, Session session) {
	return (Keyword) MediatorGeneric.get("name", name, Keyword.class, session);
    }

    public static Keyword getKeyword(Long id) {
	try {
	    if (id == null)
		return null;
	    return (Keyword) MediatorGeneric.get(id, Keyword.class);
	} catch (HibernateException e) {
	    return null;
	}
    }

    public static Parameter getParameter(Long id) {
	try {
	    if (id == null)
		return null;
	    return (Parameter) MediatorGeneric.get(id, Parameter.class);
	} catch (HibernateException e) {
	    return null;
	}
    }

    public static PatternItem getPatternItem(long id) {
	return (PatternItem) MediatorGeneric.get(id, PatternItem.class);
    }

    public static PatternObject getPatternObject(long id) {
	PatternObject po = (PatternObject) MediatorGeneric.get(id, PatternObject.class);
	return po;
    }

    public static PatternObject getPatternObject(long id, Session session) {
	PatternObject po = (PatternObject) MediatorGeneric.get(id, PatternObject.class, session);
	return po;
    }

    /**
     * Method useful to get a list with all sources that are saved in database.
     * 
     * @return The list with all sources that are saved in database.
     */
    public static List<Source> listSources() {
	return MediatorGeneric.list(Source.class);
    }

    /**
     * Method useful to get a list with all keywords that are saved in database.
     * 
     * @return The list with all keywords that are saved in database.
     */
    public static List<Keyword> listKeywords() {
	return MediatorGeneric.list(Keyword.class);
    }

    public static List<Parameter> listParameters() {
	return MediatorGeneric.list(Parameter.class);
    }

    public static List<CostFunction> listCostFunctions() {
	return MediatorGeneric.list(CostFunction.class);
    }

    public static void addCostFunctions(RequirementPatternVersion rpv, Set<CostFunction> scf)
	    throws IntegrityException {
	rpv.getExternalObjects().clear();
	Session session = MediatorConnection.getCurrentSession();
	for (CostFunction costFunction : scf) {
	    costFunction.setPatternVersion(rpv);
	    session.save(costFunction);
	}
	session.flush();
    }

    public static void updateCostFunction(CostFunction cfOld, CostFunction cfNew) {
	Session session = MediatorConnection.getCurrentSession();

	if (cfNew.getName() != null) {
	    cfOld.setName(cfNew.getName());
	}

	if (cfNew.getFunction() != null) {
	    cfOld.setFunction(cfNew.getFunction());
	}
	session.update(cfOld);
	session.flush();
    }

    public static void updateAllCostFunctions(RequirementPatternVersion rpv, Set<CostFunction> scf) {
	rpv.getExternalObjects().clear();
	for (CostFunction costFunction : scf) {
	    costFunction.setPatternVersion(rpv);
	    MediatorGeneric.save(costFunction);
	    rpv.getExternalObjects().add(costFunction);
	}
	MediatorConnection.flush();
    }

    /**
     * This method deletes all the dependencies of the given patternObject and
     * create all new dependencies
     * 
     * @param po
     *            PatternObject which we want to change the dependencies
     * @param pod
     *            Set of dependencies to put as dependencies
     */
    public static void saveNewPatternObjectDependency(PatternObject po, Set<PatternObjectDependency> pod) {
	Session session = MediatorConnection.getCurrentSession();
	saveNewPatternObjectDependency(po, pod, session);
	session.flush();
    }

    /**
     * This method deletes all the dependencies of the given patternObject and
     * create all new dependencies, In a given session.
     * 
     * @param po
     *            PatternObject which we want to change the dependencies
     * @param pod
     *            Set of dependencies to put as dependencies
     * @param session
     *            Session where we want to save
     */
    public static void saveNewPatternObjectDependency(PatternObject po, Set<PatternObjectDependency> pod,
	    Session session) {
	Set<PatternObjectDependency> dep = po.getDependencies();
	dep.clear();
	for (PatternObjectDependency aux : pod) {
	    GenericDataController.save(aux, session);
	}
	dep.addAll(pod);
	GenericDataController.update(po, session);
    }

    /**
     * Add new set of dependencies to the given {@link PatternObject}
     * 
     * @param po
     *            PatternObject where to add all the dependencies
     * @param pod
     *            Set of dependencies to add to the PatternObject
     */
    public static void addNewPatternObjectDependency(PatternObject po, Set<PatternObjectDependency> pod) {
	Session session = MediatorConnection.getCurrentSession();
	for (PatternObjectDependency aux : pod) {
	    GenericDataController.save(aux, session);
	    po.getDependencies().add(aux);
	}
	GenericDataController.update(po, session);
    }

    public static void removeAllCostFunctions(RequirementPatternVersion rpv) throws IntegrityException {
	rpv.getExternalObjects().clear();
	MediatorGeneric.update(rpv);
    }

    public static void removeDependency(long id, long idDependency) {
	Session session = MediatorConnection.getCurrentSession();
	PatternObject po = ObjectDataController.getPatternObject(id, session);
	if (po == null)
	    throw new NotFoundException("PatternObject [" + id + "] not found");

	PatternObjectDependency pod = po.getDependency(idDependency);
	if (pod == null)
	    throw new NotFoundException("Dependency [" + idDependency + "] not found");

	po.getDependencies().remove(pod);
	MediatorGeneric.deleteInSession(pod, session);
	MediatorGeneric.updateInSession(po, session);
	session.flush();

    }

    /**
     * Deletes all the dependencies of the given {@link PatternObject}
     * 
     * @param po
     *            PatternObject which have to delete dependencies
     */
    public static void deleteAllPatternObjectDependency(PatternObject po) {
	for (PatternObjectDependency pod : po.getDependencies()) {
	    pod.setDependency(null);
	}
	po.getDependencies().clear(); // clear is needed so hibernate could delete the instance from DB
	GenericDataController.update(po);
    }

}
