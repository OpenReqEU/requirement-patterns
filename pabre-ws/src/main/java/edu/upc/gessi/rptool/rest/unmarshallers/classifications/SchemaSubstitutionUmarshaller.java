package edu.upc.gessi.rptool.rest.unmarshallers.classifications;

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.exceptions.IntegrityException;

public class SchemaSubstitutionUmarshaller extends SchemaUnmarshaller {
    @JsonCreator
    public SchemaSubstitutionUmarshaller(
	    @JsonProperty(required = true) Set<ClassifierUnmarshaller> rootClassifiersIds)
	    throws IntegrityException, IOException {
	super(0, null, null, null, null, rootClassifiersIds);
    }

    public void setSchema(ClassificationSchema aux) {
	cs = aux;
    }

    @Override
    protected void setSchemaFields() {
        //not implemented WHY?
    }

    @Override
    protected void setSources() {
        //not implemented WHY?
    }
}
