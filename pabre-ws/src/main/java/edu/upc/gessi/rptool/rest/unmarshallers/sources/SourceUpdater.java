package edu.upc.gessi.rptool.rest.unmarshallers.sources;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class SourceUpdater {
    private Source s;
    private Source aux;
    private PutSourceUnmarshaller sourceUnma;

    public SourceUpdater(Source s, PutSourceUnmarshaller metricJson) {
	this.s = s;
	this.sourceUnma = metricJson;
    }

    private void unmarshall()
	    throws JsonParseException, JsonMappingException, IOException, SemanticallyIncorrectException {
	aux = sourceUnma.build();
    }

    public void update() throws Exception {
	unmarshall();
	updateFields();
	save();
    }

    private void save() {
	GenericDataController.update(s);
    }

    private void updateFields() throws SemanticallyIncorrectException {
	if (aux.getIdentifier() != null)
	    s.setIdentifier(aux.getIdentifier());
	if (aux.getComments() != null)
	    s.setComments(aux.getComments());
	if (aux.getType() != null)
	    s.setType(aux.getType());
	if (aux.getReference() != null)
	    s.setReference(aux.getReference());
    }

}
