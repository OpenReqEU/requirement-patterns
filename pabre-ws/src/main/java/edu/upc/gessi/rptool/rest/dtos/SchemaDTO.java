package edu.upc.gessi.rptool.rest.dtos;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementPatternDTO;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

@JsonInclude(Include.NON_NULL)
public class SchemaDTO extends ClassificationObjectDTO {
    final static Logger logger = Logger.getLogger(SchemaDTO.class);

    private String name;
    @InjectLink(value = "schemas/${instance.id}", style = Style.ABSOLUTE)
    private URI uri;
    private Set<ClassifierDTO> rootClassifiers;
    private Set<RequirementPatternDTO> unbindedPatterns;

    public SchemaDTO(ClassificationSchema cs) throws SemanticallyIncorrectException {
	super(cs);
	this.name = cs.getName();
	setRootClassifiersRecursive(cs.getRootClassifiers());
    }

    public SchemaDTO(long id, String name, String description, String comments, Set<SourceDTO> sources,
	    Set<Classifier> rootClassifiers) throws SemanticallyIncorrectException {
	super(id, description, comments, sources);
	this.name = name;
	setRootClassifiers(rootClassifiers);
    }

    public Set<ClassifierDTO> getRootClassifiers() {
	return rootClassifiers;
    }

    /**
     * Method used to set only the root classifiers and not all the internal
     * classifiers
     * 
     * @param rootClassifiers
     *            Classifiers to set
     * @throws SemanticallyIncorrectException
     */
    public void setRootClassifiers(Set<Classifier> rootClassifiers) throws SemanticallyIncorrectException {
	if (rootClassifiers != null) {
	    this.rootClassifiers = new TreeSet<ClassifierDTO>(new PositionComparator());
	    for (Classifier rc : rootClassifiers) {
		this.rootClassifiers.add(new ClassifierDTO(rc, this.getId()));
	    }
	}
    }

    public void setRootClassifiersRecursive(Set<Classifier> rootClassifiers) throws SemanticallyIncorrectException {
	if (rootClassifiers != null) {
	    this.rootClassifiers = new TreeSet<ClassifierDTO>(new PositionComparator());
	    for (Classifier rc : rootClassifiers) {
		ClassifierDTO rcDTO = new ClassifierDTO(rc, this.getId());
		rcDTO.setInternalClassifiersRecursive(rc.getInternalClassifiers(), this.getId());
		if (!this.rootClassifiers.add(rcDTO)) {
		    logger.debug("could not add classifier");
		}
	    }
	}
    }

    @JsonIgnore
    public Set<ClassifierDTO> getAllInternalClassifiers() {
	Set<ClassifierDTO> listClassifiers = new HashSet<ClassifierDTO>();
	for (ClassifierDTO icDTO : this.rootClassifiers) {
	    listClassifiers.addAll(icDTO.getAllInternalClassifiers());
	}
	return listClassifiers;
    }

    public Set<RequirementPatternDTO> getUnbindedPatterns() {
	return unbindedPatterns;
    }

    public void setUnbindedPatterns(Set<RequirementPatternDTO> unbindedPatterns) {
	this.unbindedPatterns = unbindedPatterns;
    }

    public void addUnbindedPattern(RequirementPatternDTO rpDTO) {
	if (this.unbindedPatterns == null)
	    this.unbindedPatterns = new HashSet<RequirementPatternDTO>();
	this.unbindedPatterns.add(rpDTO);
    }

    public URI getUri() {
	return uri;
    }

    public void setUri(URI uri) {
	this.uri = uri;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	SchemaDTO other = (SchemaDTO) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

}
