package edu.upc.gessi.rptool.rest.dtos.importexport.classifications;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.rest.dtos.PositionComparator;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

@JsonInclude(Include.NON_NULL)
public class SchemaExportDTO extends ClassificationObjectExportDTO {
    private String name;
    private Set<ClassifierExportDTO> rootClassifiers;

    public SchemaExportDTO(ClassificationSchema cs) throws SemanticallyIncorrectException {
	super(cs);
	this.name = cs.getName();
	setRootClassifiersExport(cs.getRootClassifiers());
    }

    public void setRootClassifiersExport(Set<Classifier> rootClassifiers) throws SemanticallyIncorrectException {
	if (rootClassifiers != null) {
	    this.rootClassifiers = new TreeSet<ClassifierExportDTO>(new PositionComparator());
	    for (Classifier rc : rootClassifiers) {
		this.rootClassifiers.add(new ClassifierExportDTO(rc));
	    }
	}
    }

    @JsonIgnore
    public Set<ClassifierExportDTO> getAllInternalClassifiers() {
	Set<ClassifierExportDTO> listClassifiers = new HashSet<ClassifierExportDTO>();
	for (ClassifierExportDTO icDTO : this.rootClassifiers) {
	    listClassifiers.addAll(icDTO.getAllInternalClassifiers());
	}
	return listClassifiers;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Set<ClassifierExportDTO> getRootClassifiers() {
	return rootClassifiers;
    }

    public void setRootClassifiers(Set<ClassifierExportDTO> rootClassifiers) {
	this.rootClassifiers = rootClassifiers;
    }

}
