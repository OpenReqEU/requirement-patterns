package edu.upc.gessi.rptool.rest.unmarshallers.sources;

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
	    throws SemanticallyIncorrectException {
	aux = sourceUnma.build();
    }

    public void update() throws SemanticallyIncorrectException {
	unmarshall();
	updateFields();
	save();
    }

    private void save() {
	GenericDataController.update(s);
    }

    private void updateFields() {
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
