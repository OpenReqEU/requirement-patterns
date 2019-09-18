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
	List<T> list = new ArrayList<>(c);
	java.util.Collections.sort(list);
	return list;
    }

    public static <T> List<T> uniqueList(Collection<T> c) {
	LinkedHashSet<T> t = new LinkedHashSet<>(c);
	return new ArrayList<>(t);
    }

}
