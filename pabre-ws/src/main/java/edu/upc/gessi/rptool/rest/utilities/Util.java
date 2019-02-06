package edu.upc.gessi.rptool.rest.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.dtos.ClassifierDTOInterface;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementPatternDTO;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class Util {

    /**
     * Convert a Collection to a list
     * 
     * @param c
     * @return
     */
    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	List<T> list = new ArrayList<T>(c);
	java.util.Collections.sort(list);
	return list;
    }

    public static <T extends Object> List<T> uniqueList(Collection<T> c) {
	LinkedHashSet<T> t = new LinkedHashSet<>(c);
	return new ArrayList<>(t);
    }

    /*
     * OLD method copied justi n case
     */
    @SuppressWarnings("null")
    @Deprecated
    protected <T, Q> void checkType(Set<T> requirementPattersSet, Set<Q> UnmashallersInterClassifers, boolean isRoot)
	    throws SemanticallyIncorrectException {
	Classifier classifierInstance = null;
	int type = classifierInstance.getType();
	// estamos en un internal classifier, no puede ser root
	if (isRoot && (type == 3 || type == 1 || type == 2)) {
	    throw new SemanticallyIncorrectException("Incorrect type value");
	}

	if (type == Classifier.ROOT) {
	    if (!isRoot || requirementPattersSet.size() > 0)
		throw new SemanticallyIncorrectException("Incorrect type value");
	} else if (type == Classifier.GENERAL) {
	    if (requirementPattersSet.size() > 0 || UnmashallersInterClassifers.size() > 0) {
		throw new SemanticallyIncorrectException("Incorrect type value");
	    }
	} else if (type == Classifier.DECOMPOSED) {
	    if (!(UnmashallersInterClassifers.size() > 0 && requirementPattersSet.size() == 0)) {
		throw new SemanticallyIncorrectException("Incorrect type value");
	    }
	} else if (type == Classifier.BASIC) {
	    if (!(UnmashallersInterClassifers.size() == 0 && requirementPattersSet.size() > 0)) {
		throw new SemanticallyIncorrectException("Incorrect type value");
	    }
	} else
	    throw new SemanticallyIncorrectException("Incorrect type value");
    }

    /**
     * Method that get all the patterns of the system and classifies them adding
     * each one under its respective InternalClassifierDTO of the set or puts it in
     * the unbinded set <strong>old method, conserved just in case</strong>
     * 
     * @param setClassifiers
     *            set of InternalClassifierDTOs that will be modified adding to each
     *            one their respective RequirementPatternDTOs, if null all
     *            RequirementPatternDTO will be added to setUnbinded
     * 
     * @param setUnbinded
     *            set of RequirementPatternDTO that will be modified adding to it
     *            all the RequirementPatternDTOs that are not binded to any of the
     *            InternalClassifierDTOs of setClassifiers, if null no
     *            RequirementPatternDTO will be added
     */
    @Deprecated
    public static <T extends ClassifierDTOInterface> void addAllPatternsInClassifiers(Collection<T> setClassifiers,
	    Collection<RequirementPatternDTO> setUnbinded) {
	/*
	 * // Get all the patterns in the catalog with they classifier List<Object[]>
	 * rpTuples =
	 * PatternDataController.listPatternIdWithNameEditableAvailableAndParents();
	 * HashMap<Long, List<Long>> idPatternParentsMap = new HashMap<Long,
	 * List<Long>>(); TreeSet<RequirementPatternDTO> allRpDTOList = new
	 * TreeSet<RequirementPatternDTO>();
	 * 
	 * // Create all the requirement patterns DTOs from the tuples and store // them
	 * in the TreeSet (without repeated patterns) for (Object[] rpObj : rpTuples) {
	 * allRpDTOList.add(new RequirementPatternDTO((Long) rpObj[0], (String)
	 * rpObj[1], null, null, null, (Integer) rpObj[2] == 0 ? false : true, null,
	 * null, null, null, (Boolean) rpObj[3], null, null, null, null, null, null)); }
	 * 
	 * // Create the map that stores for each id of each pattern, a list of the //
	 * ids of the internal classifiers that store that pattern for (Object[] rpObj :
	 * rpTuples) { if (!idPatternParentsMap.containsKey(rpObj[0])) // if the map
	 * dosen't contain the RP then add to the map idPatternParentsMap.put((Long)
	 * rpObj[0], new LinkedList<Long>()); if (rpObj[4] != null) // if pattern has a
	 * internalCLassifer then add it to the map
	 * idPatternParentsMap.get(rpObj[0]).add((Long) rpObj[4]); }
	 * 
	 * // Store each Requirement Pattern DTO that has been previously created in //
	 * the correct internal classifier of the schema // Comented just avoid
	 * compilation error, due to icDTO now dosen't implemente // any getType
	 * 
	 * for (RequirementPatternDTO rpDTO : allRpDTOList) { // for every pattern
	 * obtained boolean rpDTOclassified = false; for (Long icId :
	 * idPatternParentsMap.get(rpDTO.getId())) {// get the classifiers of one
	 * pattern for (ClassifierDTOInterface icDTO : setClassifiers) { // go for every
	 * classifier asked if (icDTO.getType() == 1 && icId == icDTO.getId()) {
	 * icDTO.addRequirementPattern(rpDTO); rpDTOclassified = true; break; } } } if
	 * (setUnbinded != null && !rpDTOclassified) { setUnbinded.add(rpDTO); } }
	 */

    }

}
