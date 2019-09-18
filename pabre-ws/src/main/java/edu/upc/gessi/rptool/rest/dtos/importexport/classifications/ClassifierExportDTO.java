package edu.upc.gessi.rptool.rest.dtos.importexport.classifications;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.dtos.ClassifierDTOInterface;
import edu.upc.gessi.rptool.rest.dtos.PositionComparator;
import edu.upc.gessi.rptool.rest.dtos.Positionable;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementPatternDTO;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

@JsonInclude(Include.NON_NULL)
public class ClassifierExportDTO extends ClassificationObjectExportDTO implements Positionable, ClassifierDTOInterface {
    protected String name;
    protected long id;
    protected Set<ClassifierExportDTO> internalClassifiers;
    protected int pos;
    protected List<String> requirementPatterns;

    public ClassifierExportDTO(Classifier ic) throws SemanticallyIncorrectException {
	super(ic);
	this.name = ic.getName();
	this.id = ic.getId();
	this.internalClassifiers = makeInternalClassifierDTO(ic);
	this.pos = ic.getPos();
	this.requirementPatterns = new ArrayList<>();
	for (RequirementPattern rp : ic.getPatterns()) {
	    requirementPatterns.add(rp.getName());
	}
    }

    private Set<ClassifierExportDTO> makeInternalClassifierDTO(Classifier rc) throws SemanticallyIncorrectException {
	Set<ClassifierExportDTO> s = new TreeSet<>(new PositionComparator());
	for (Classifier ic : rc.getInternalClassifiers()) {
	    s.add(new ClassifierExportDTO(ic));
	}
	return s;
    }

    public void addRequirementPattern(RequirementPatternDTO rpDTO) {
	this.requirementPatterns.add(rpDTO.getName());
    }

    @JsonIgnore
    public Set<ClassifierExportDTO> getAllInternalClassifiers() {
	if (this.internalClassifiers == null) {
	    return new HashSet<>();
	}

	Set<ClassifierExportDTO> listClassifiers = new HashSet<>();
	for (ClassifierExportDTO icDTO : this.internalClassifiers) {
	    listClassifiers.addAll(icDTO.getAllInternalClassifiers());
	}
	listClassifiers.addAll(this.getInternalClassifiers());
	return listClassifiers;
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
	return this.pos;
    }

    public Set<ClassifierExportDTO> getInternalClassifiers() {
	return internalClassifiers;
    }

    public void setInternalClassifiers(Set<ClassifierExportDTO> internalClassifiers) {
	this.internalClassifiers = internalClassifiers;
    }

    public List<String> getRequirementPatterns() {
	return requirementPatterns;
    }

    public void setRequirementPatterns(List<String> requirementPatterns) {
	this.requirementPatterns = requirementPatterns;
    }

    public void setPos(int pos) {
	this.pos = pos;
    }

    @Override
    public long getId() {
	return this.id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public void setId(long id) {
	this.id = id;
    }

}
