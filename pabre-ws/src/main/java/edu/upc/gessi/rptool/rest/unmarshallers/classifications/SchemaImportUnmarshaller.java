package edu.upc.gessi.rptool.rest.unmarshallers.classifications;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.IdToDomainObject;
import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.RedundancyException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class SchemaImportUnmarshaller extends SchemaUnmarshaller {
    private Set<String> sourcesIds;
    private Set<ClassifierImportUnmarshaller> rootClassifiersIds;

    @JsonCreator
    public SchemaImportUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sourcesIds,
	    @JsonProperty(value = "rootClassifiers", required = true) Set<ClassifierImportUnmarshaller> rootClassifiersIds)
	    throws IntegrityException, IOException {
	super(id, name, description, comments);
	this.sourcesIds = sourcesIds;
	setSchemaFields();
	this.rootClassifiersIds = rootClassifiersIds;

    }

    @Override
    protected void unmarshalRoots() throws SemanticallyIncorrectException, IntegrityException, RedundancyException {
	Set<ClassifierUnmarshaller> s = new HashSet<>();
	for (ClassifierUnmarshaller rootClassifierUnmarshaller : rootClassifiersIds) {
	    s.add(rootClassifierUnmarshaller);
	}
	buildAndAddRootClassifiers(s);
    }

    @Override
    protected void setSources() throws SemanticallyIncorrectException {
	if (sourcesIds != null) {
	    try {
		Set<Source> s = IdToDomainObject.getSourcesByIdentifiers(sourcesIds);
		cs.clearAndSetSources(s);
	    } catch (NotFoundException e) {
		throw new SemanticallyIncorrectException("invalid source id");
	    }
	}
    }

    public Set<ClassifierImportUnmarshaller> getUnmImportRootClassifiers() {
	return rootClassifiersIds;
    }

    public Set<String> getSourcesNames() {
	return sourcesIds;
    }

}
