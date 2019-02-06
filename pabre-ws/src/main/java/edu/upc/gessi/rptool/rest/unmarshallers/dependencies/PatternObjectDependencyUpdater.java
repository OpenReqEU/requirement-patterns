package edu.upc.gessi.rptool.rest.unmarshallers.dependencies;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.domain.PatternObjectDependency;

public class PatternObjectDependencyUpdater {

    protected PatternObjectDependency m;
    protected PatternObjectDependency aux;
    protected PutPatternObjectDependencyUnmarshaller unmarshaller;

    public PatternObjectDependencyUpdater(PatternObjectDependency m,
	    PutPatternObjectDependencyUnmarshaller unmarshall) {
	this.m = m;
	this.unmarshaller = unmarshall;
    }

    private void unmarshall() throws Exception {
	aux = unmarshaller.build();

    }

    private void updateFields() {
	m.setDependencyType(aux.getDependencyType());
	m.setDependencyDirection(aux.getDependencyDirection());
    }

    private void save() {
	GenericDataController.update(m);
    }

    public void update() throws Exception {
	unmarshall();
	updateFields();
	save();

    }
}
