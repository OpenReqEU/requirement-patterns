package edu.upc.gessi.rptool.data;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import org.hibernate.Session;

import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.patternelements.Keyword;
import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;

public class IdToDomainObject {

	private IdToDomainObject() {
		//utility class
	}

    public static RequirementPattern getReqPattern(Long id) throws NotFoundException {
	RequirementPattern ret = PatternDataController.getPattern(id);
	if (ret == null)
	    throw new NotFoundException("RequirementPattern [" + id + "] not found");
	return ret;

    }

    public static RequirementPattern getReqPattern(String name) throws NotFoundException {
	RequirementPattern ret = PatternDataController.getPattern(name);
	if (ret == null)
	    throw new NotFoundException("RequirementPattern [" + name + "] not found");
	return ret;

    }

    public static Set<RequirementPattern> getReqPatterns(Set<Long> ids) throws NotFoundException {
	Set<RequirementPattern> ret = new HashSet<>();
	for (Long id : ids) {
	    ret.add(getReqPattern(id));
	}
	return ret;
    }

    public static Set<RequirementPattern> getReqPatternsByNames(Set<String> names) throws NotFoundException {
	Set<RequirementPattern> ret = new HashSet<>();
	for (String name : names) {
	    ret.add(getReqPattern(name));
	}
	return ret;
    }

    public static Set<Source> getSources(Set<Long> ids) throws NotFoundException {
	Set<Source> ret = new HashSet<>();
	for (Long id : ids) {
	    Source s = getSource(id);
	    if (s == null)
		throw new NotFoundException("Source [" + id + "] not found");
	    ret.add(s);
	}
	return ret;
    }

    public static Set<Source> getSourcesByIdentifiers(Set<String> ids) throws NotFoundException {
	Set<Source> ret = new HashSet<>();
	for (String id : ids) {
	    Source s = getSourceByIdentifier(id);
	    if (s == null) throw new NotFoundException("Source [" + id + "] not found");
	    ret.add(s);
	}
	return ret;
    }

    public static Set<Source> getSourcesByIdentifiers(Set<String> sources, Session session) {
	Set<Source> ret = new HashSet<>();
	for (String id : sources) {
	    Source s = getSourceByIdentifier(id, session);
	    if (s == null) throw new NotFoundException("Source [" + id + "] not found");
	    ret.add(s);
	}
	return ret;
    }

    public static Source getSource(Long id) {
	return ObjectDataController.getSource(id);
    }

    public static Source getSourceByIdentifier(String id) {
	return ObjectDataController.getSource(id);
    }

    public static Source getSourceByIdentifier(String identifier, Session session) {
	return ObjectDataController.getSource(identifier, session);
    }

    public static Keyword getKeyword(long id) throws NotFoundException {
	Keyword ret = ObjectDataController.getKeyword(id);
	if (ret == null) throw new NotFoundException("Keyword [" + id + "] not found");
	return ret;
    }

    public static Keyword getKeywordByName(String id) throws NotFoundException {
	Keyword ret = ObjectDataController.getKeyword(id);
	if (ret == null) throw new NotFoundException("Keyword [" + id + "] not found");
	return ret;
    }

    public static Keyword getKeywordByName(String id, Session session) throws NotFoundException {
	Keyword ret = ObjectDataController.getKeyword(id, session);
	if (ret == null) throw new NotFoundException("Keyword [" + id + "] not found");
	return ret;
    }

    public static Set<Keyword> getKeywords(Set<Long> ids) throws NotFoundException {
	Set<Keyword> ret = new HashSet<>();
	for (Long id : ids) {
	    Keyword s = getKeyword(id);
	    ret.add(s);
	}
	return ret;
    }

    public static Set<Keyword> getKeywordsByNames(Set<String> ids) throws NotFoundException {
	Set<Keyword> ret = new HashSet<>();
	for (String id : ids) {
	    Keyword s = getKeywordByName(id);
	    ret.add(s);
	}
	return ret;
    }

    public static Set<Keyword> getKeywordsByNames(Set<String> keywords, Session session) {
	Set<Keyword> ret = new HashSet<>();
	for (String id : keywords) {
	    Keyword s = getKeywordByName(id, session);
	    ret.add(s);
	}
	return ret;
    }

    public static Parameter getParameter(long id) throws NotFoundException {
	Parameter ret = ObjectDataController.getParameter(id);
	if (ret == null)
	    throw new NotFoundException("Parameter [" + id + "] not found");
	return ret;
    }

    public static Set<Parameter> getParameters(Set<Integer> ids) throws NotFoundException {
	Set<Parameter> ret = new HashSet<>();
	for (Integer id : ids) {
	    Parameter s = getParameter(id);
	    ret.add(s);
	}
	return ret;
    }

}
