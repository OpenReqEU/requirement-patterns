package edu.upc.gessi.rptool.rest.unmarshallers.keywords;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.domain.patternelements.Keyword;

public class KeywordUpdater {
    protected Keyword k;
    protected Keyword aux;
    protected KeywordUnmarshaller metricJson;

    public KeywordUpdater(Keyword k, KeywordUnmarshaller sourceJson) {
	this.k = k;
	this.metricJson = sourceJson;
    }

    private void unmarshall() {
	aux = metricJson.build();
    }

    public void update() {
	unmarshall();
	updateFields();
	save();
    }

    private void save() {
	GenericDataController.update(k);
    }

    private void updateFields() {
	k.setName(aux.getName());
    }
}
