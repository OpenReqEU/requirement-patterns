package edu.upc.gessi.rptool.rest.dtos;

import java.net.URI;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementPatternDTO;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

@JsonInclude(Include.NON_NULL)
public class ClassifierDTO extends ClassificationObjectDTO implements Positionable, ClassifierDTOInterface {

    @InjectLink(value = "schemas/${instance.schemaid}/classifiers/${instance.id}", style = Style.ABSOLUTE)
    private URI uri;

    private String name;
    private int type;
    private Set<ClassifierDTO> internalClassifiers;
    private int pos;
    @JsonIgnore
    private long schemaid;
    private List<RequirementPatternDTO> requirementPatterns;

    public ClassifierDTO(Classifier ic, long schemaid) throws SemanticallyIncorrectException {
	super(ic);
	this.name = ic.getName();
	this.type = ic.getType();
	this.pos = ic.getPos();
	this.requirementPatterns = new LinkedList<RequirementPatternDTO>();
	this.schemaid = schemaid;
	for (RequirementPattern rp : ic.getPatterns()) {
	    // Create a reduced version of the pattern
	    RequirementPatternDTO rpdto = new RequirementPatternDTO(rp.getId(), rp.getName(), null, null, null,
		    (rp.getEditable() != 0), null, null, null, null, rp.findLastVersion().getAvailable(), null, null,
		    null, null, null, null);
	    requirementPatterns.add(rpdto);
	}
    }

    @JsonIgnore
    public Set<ClassifierDTO> getAllInternalClassifiers() {
	if (this.internalClassifiers == null) {
	    return new HashSet<ClassifierDTO>();
	}

	Set<ClassifierDTO> listClassifiers = new HashSet<ClassifierDTO>();
	for (ClassifierDTO icDTO : this.internalClassifiers) {
	    listClassifiers.addAll(icDTO.getAllInternalClassifiers());
	}
	listClassifiers.addAll(this.getInternalClassifiers());
	return listClassifiers;
    }

    public void addRequirementPattern(RequirementPatternDTO rpDTO) {
	this.requirementPatterns.add(rpDTO);
    }

    public void addRequirementsPattern(Set<RequirementPatternDTO> rpDTOSet) {
	this.requirementPatterns.addAll(rpDTOSet);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getType() {
	return type;
    }

    public void setType(int type) {
	this.type = type;
    }

    public Set<ClassifierDTO> getInternalClassifiers() {
	return internalClassifiers;
    }

    public void setInternalClassifiers(Set<ClassifierDTO> internalClassifiers) {
	this.internalClassifiers = internalClassifiers;
    }

    public void setInternalClassifiersRecursive(Set<Classifier> internalClassifiers, long schemaid)
	    throws SemanticallyIncorrectException {
	this.internalClassifiers = new TreeSet<ClassifierDTO>(new PositionComparator());
	for (Classifier ic : internalClassifiers) {
	    ClassifierDTO icDTO = new ClassifierDTO(ic, schemaid);
	    icDTO.setInternalClassifiersRecursive(ic.getInternalClassifiers(), schemaid);
	    this.internalClassifiers.add(icDTO);
	}
    }

    @Override
    public int getPos() {
	return pos;
    }

    public void setPos(int pos) {
	this.pos = pos;
    }

    public URI getUri() {
	return uri;
    }

    public void setUri(URI uri) {
	this.uri = uri;
    }

    public List<RequirementPatternDTO> getRequirementPatterns() {
	return requirementPatterns;
    }

    public void setRequirementPatterns(List<RequirementPatternDTO> requirementPatterns) {
	this.requirementPatterns = requirementPatterns;
    }

    public long getSchemaid() {
	return schemaid;
    }

    public void setSchemaid(long schemaid) {
	this.schemaid = schemaid;
    }

    @Override
    public int hashCode() {
	return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
	return EqualsBuilder.reflectionEquals(this, obj, false);
    }

}
