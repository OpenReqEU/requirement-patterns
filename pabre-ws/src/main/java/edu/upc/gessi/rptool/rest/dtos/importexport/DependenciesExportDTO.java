package edu.upc.gessi.rptool.rest.dtos.importexport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.upc.gessi.rptool.domain.PatternObjectDependency;

public class DependenciesExportDTO {

    private long idDependant;
    private List<PatternObjectDependencyExportDTO> dependencies;

    public DependenciesExportDTO(long idDependant, Set<PatternObjectDependency> pod) {
	this.idDependant = idDependant;
	dependencies = new ArrayList<>();
	for (PatternObjectDependency patternObjectDependency : pod) {
	    PatternObjectDependencyExportDTO pode = new PatternObjectDependencyExportDTO(
                patternObjectDependency);
	    dependencies.add(pode);
	}
    }

    public long getIdDependant() {
	return idDependant;
    }

    public void setIdDependant(long idDependant) {
	this.idDependant = idDependant;
    }

    public List<PatternObjectDependencyExportDTO> getDependencies() {
	return dependencies;
    }

    public void setDependencies(List<PatternObjectDependencyExportDTO> dependencies) {
	this.dependencies = dependencies;
    }

}
