package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.CostFunction;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class RequirementPatternVersionImportUnmarshaller extends RequirementPatternVersionUnmarshaller {

    protected ArrayList<RequirementFormImportUnmarshaller> forms;
    protected List<CostFunctionImportUnmarshaller> costFunctions;

    @JsonCreator
    public RequirementPatternVersionImportUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "versionDate", required = true) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss") Date versionDate,
	    @JsonProperty(value = "author", required = true) String author,
	    @JsonProperty(value = "goal", required = true) String goal,
	    @JsonProperty(value = "reason", required = false) String reason,
	    @JsonProperty(value = "numInstances", required = true) int numInstances,
	    @JsonProperty(value = "available", required = true) boolean available,
	    @JsonProperty(value = "statsNumInstances", required = true) int statsNumInstances,
	    @JsonProperty(value = "statsNumAssociates", required = true) int statsNumAssociates,
	    @JsonProperty(value = "keywords", required = false) Set<String> keywords,
	    @JsonProperty(value = "artifactsRelation", required = false) String artifactsRelation,
	    @JsonProperty(value = "forms", required = true) ArrayList<RequirementFormImportUnmarshaller> forms,
	    @JsonProperty(value = "costFunctions", required = false) ArrayList<CostFunctionImportUnmarshaller> costFunctions) {

	super(id, versionDate, author, goal, reason, numInstances, available, statsNumInstances, statsNumAssociates,
		artifactsRelation);
	this.costFunctions = costFunctions;
	this.keywords = keywords;
	this.forms = forms;
    }

    @Override
    protected void buildForms() throws SemanticallyIncorrectException, IntegrityException {
	boolean positions[] = new boolean[forms.size()]; // inicializado a falso
	for (RequirementFormImportUnmarshaller requirementFormImportUnmarshaller : forms) {
	    checkPositionsAndBuildForms(positions, requirementFormImportUnmarshaller);
	}
	checkContainsInvalidPosValue(positions);
    }

    @Override
    protected void checkFormsSize() throws SemanticallyIncorrectException {
	if (forms.size() == 0)
	    throw new SemanticallyIncorrectException("no forms provided in version");
    }

    @Override
    protected void buildOwnFields() {
	buildCostFuction();
    }

    private void buildCostFuction() {
	if (costFunctions != null) {
	    for (int i = 0; i < costFunctions.size(); i++) {
		CostFunction cf = costFunctions.get(i).build();
		rpv.addExternalObject(cf);
	    }
	}
    }

    public boolean checkAllItemsContainsID() {
	boolean b = id != 0;
	for (RequirementFormImportUnmarshaller forms : forms) {
	    if(!forms.checkAllItemsContainsID()) {
		b = false;
	    }
	}
	if (costFunctions != null) {
	    for (CostFunctionImportUnmarshaller costs : costFunctions) {
		    if(!costs.checkAllItemsContainsID()) {
			b = false;
		    }
		}
	}
	
	return b;
    }
}
