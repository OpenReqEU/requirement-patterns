package edu.upc.gessi.rptool.domain;

import edu.upc.gessi.rptool.domain.patternelements.PatternObject;

public class PatternObjectCompleteDependency {
    private PatternObject patternObject;
    private PatternObjectDependency patternObjectDependency;

    public PatternObjectCompleteDependency(PatternObject patternObject,
	    PatternObjectDependency patternObjectDependency) {
	this.patternObject = patternObject;
	this.patternObjectDependency = patternObjectDependency;
    }

    public PatternObject getPatternObject() {
	return patternObject;
    }

    public void setPatternObject(PatternObject patternObject) {
	this.patternObject = patternObject;
    }

    public PatternObjectDependency getPatternObjectDependency() {
	return patternObjectDependency;
    }

    public void setPatternObjectDependency(PatternObjectDependency patternObjectDependency) {
	this.patternObjectDependency = patternObjectDependency;
    }

}
