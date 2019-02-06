package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.IdToDomainObject;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class RequirementPatternVersionUnmarshaller {

    protected RequirementPatternVersion rpv;
    protected long id;
    protected Date versionDate;
    protected String author;
    protected String goal;
    protected String reason;
    protected Integer numInstances;
    protected Boolean available;
    protected Integer statsNumInstances;
    protected Integer statsNumAssociates;
    protected ArrayList<RequirementFormUnmarshaller> forms;
    protected Set<String> keywords;
    protected String artifactsRelation;

    @JsonCreator
    public RequirementPatternVersionUnmarshaller(@JsonProperty(value = "id", required = false) long id,
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
	    @JsonProperty(value = "forms", required = true) ArrayList<RequirementFormUnmarshaller> forms) {
	this.id = id;
	this.versionDate = versionDate;
	this.author = author;
	this.goal = goal;
	this.reason = reason;
	this.numInstances = numInstances;
	this.available = available;
	this.statsNumInstances = statsNumInstances;
	this.statsNumAssociates = statsNumAssociates;
	this.keywords = keywords;
	this.forms = forms;
	this.artifactsRelation = artifactsRelation;
    }

    public RequirementPatternVersionUnmarshaller(Date versionDate, String author, String goal, String reason,
	    int numInstances, boolean available, int statsNumInstances, int statsNumAssociates, Set<String> keywords,
	    String artifactsRelation, ArrayList<RequirementFormUnmarshaller> forms) {
	this.versionDate = versionDate;
	this.author = author;
	this.goal = goal;
	this.reason = reason;
	this.numInstances = numInstances;
	this.available = available;
	this.statsNumInstances = statsNumInstances;
	this.statsNumAssociates = statsNumAssociates;
	this.keywords = keywords;
	this.forms = forms;
	this.artifactsRelation = artifactsRelation;
    }

    public RequirementPatternVersionUnmarshaller(long id, Date versionDate, String author, String goal, String reason,
	    Integer numInstances, Boolean available, Integer statsNumInstances, Integer statsNumAssociates,
	    String artifactsRelation) {
	super();
	this.id = id;
	this.versionDate = versionDate;
	this.author = author;
	this.goal = goal;
	this.reason = reason;
	this.numInstances = numInstances;
	this.available = available;
	this.statsNumInstances = statsNumInstances;
	this.statsNumAssociates = statsNumAssociates;
	this.artifactsRelation = artifactsRelation;
    }

    public RequirementPatternVersionUnmarshaller(Date versionDate, String author, String goal, String reason,
	    Integer numInstances, Boolean available, Integer statsNumInstances, Integer statsNumAssociates,
	    String artifactsRelation) {
	super();
	this.versionDate = versionDate;
	this.author = author;
	this.goal = goal;
	this.reason = reason;
	this.numInstances = numInstances;
	this.available = available;
	this.statsNumInstances = statsNumInstances;
	this.statsNumAssociates = statsNumAssociates;
	this.artifactsRelation = artifactsRelation;
    }

    protected void buildForms() throws IntegrityException, SemanticallyIncorrectException {
	boolean positions[] = new boolean[forms.size()]; // inicializado a falso

	for (RequirementFormUnmarshaller rfu : forms) {
	    checkPositionsAndBuildForms(positions, rfu);
	}
	checkContainsInvalidPosValue(positions);
    }

    /**
     * Check if the array contains a false position, and throws the exception
     * informing that forms contains invalid pos value in version
     * 
     * @param positions
     *            Array to check
     * @throws SemanticallyIncorrectException
     *             Exception informing about the problem
     */
    protected void checkContainsInvalidPosValue(boolean[] positions) throws SemanticallyIncorrectException {
	for (boolean b : positions) {
	    if (!b) {
		throw new SemanticallyIncorrectException("invalid form pos value in version");
	    }
	}
    }

    /**
     * Check the pos value of the forms, and set inside the array the positions Used
     * inside the forms, builds the forms and save them in
     * {@link RequirementPatternVersion}
     * 
     * @param positions
     *            Array of booleans where to save the positions used
     * @param rfu
     *            {@link RequirementFormsUnmarshaller} to build and check
     * @throws SemanticallyIncorrectException
     *             Exception with the needed information to show to user
     * @throws IntegrityException
     *             Exception with information for the user, about any integrity
     *             violation
     */
    protected void checkPositionsAndBuildForms(boolean[] positions, RequirementFormUnmarshaller rfu)
	    throws SemanticallyIncorrectException, IntegrityException {
	try {
	    if (positions[rfu.getPos()])
		throw new SemanticallyIncorrectException("invalid form pos value in version");
	    positions[rfu.getPos()] = true;
	    rpv.addForm(rfu.build());
	} catch (ArrayIndexOutOfBoundsException e) {
	    throw new SemanticallyIncorrectException("invalid form pos value in version");
	}
    }

    protected void buildKeywords() throws SemanticallyIncorrectException {

	if (keywords != null) {
	    try {
		rpv.setKeywords(IdToDomainObject.getKeywordsByNames(keywords));
	    } catch (NotFoundException e) {
		throw new SemanticallyIncorrectException("invalid keyword id in version");
	    }
	} else {
	    rpv.setKeywords(null);
	}
    }

    protected void checkFormsSize() throws SemanticallyIncorrectException {
	if (forms.size() == 0)
	    throw new SemanticallyIncorrectException("no forms provided in version");
    }

    @Override
    public int hashCode() {
	return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
	return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    /**
     * Method used to build custom fields of the childs
     */
    protected void buildOwnFields() {
    }

    public RequirementPatternVersion build() throws SemanticallyIncorrectException, IntegrityException {
	if (numInstances < 0)
	    throw new SemanticallyIncorrectException("numInstances must be => 0 in version");
	if (statsNumInstances < 0)
	    throw new SemanticallyIncorrectException("statsNumInstances must be => 0 in version");
	if (statsNumAssociates < 0)
	    throw new SemanticallyIncorrectException("statsNumAssociates must be => 0 in version");
	checkFormsSize();

	rpv = new RequirementPatternVersion();
	rpv.setId(id);
	rpv.setVersionDate(versionDate);
	rpv.setAuthor(author);
	rpv.setGoal(goal);
	rpv.setReason(reason);
	rpv.setNumInstances(numInstances);
	rpv.setAvailable(available);
	rpv.setStatsNumInstances(statsNumInstances);
	rpv.setStatsNumAssociates(statsNumAssociates);
	rpv.setArtifactRelation(artifactsRelation);
	buildForms();
	buildKeywords();
	buildOwnFields();
	return rpv;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public Date getVersionDate() {
	return versionDate;
    }

    public void setVersionDate(Date versionDate) {
	this.versionDate = versionDate;
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public String getGoal() {
	return goal;
    }

    public void setGoal(String goal) {
	this.goal = goal;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public Integer getNumInstances() {
	return numInstances;
    }

    public void setNumInstances(Integer numInstances) {
	this.numInstances = numInstances;
    }

    public Boolean getAvailable() {
	return available;
    }

    public void setAvailable(Boolean available) {
	this.available = available;
    }

    public Integer getStatsNumInstances() {
	return statsNumInstances;
    }

    public void setStatsNumInstances(Integer statsNumInstances) {
	this.statsNumInstances = statsNumInstances;
    }

    public Integer getStatsNumAssociates() {
	return statsNumAssociates;
    }

    public void setStatsNumAssociates(Integer statsNumAssociates) {
	this.statsNumAssociates = statsNumAssociates;
    }

    public ArrayList<RequirementFormUnmarshaller> getForms() {
	return forms;
    }

    public void setForms(ArrayList<RequirementFormUnmarshaller> forms) {
	this.forms = forms;
    }

    public Set<String> getKeywords() {
	return keywords;
    }

    public void setKeywords(Set<String> keywords) {
	this.keywords = keywords;
    }

    public String getArtifactsRelation() {
	return artifactsRelation;
    }

    public void setArtifactsRelation(String artifactsRelation) {
	this.artifactsRelation = artifactsRelation;
    }

}
