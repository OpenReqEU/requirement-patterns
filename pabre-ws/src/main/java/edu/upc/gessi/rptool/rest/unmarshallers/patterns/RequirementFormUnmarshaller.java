package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.IdToDomainObject;
import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;
import edu.upc.gessi.rptool.domain.patternelements.FixedPart;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class RequirementFormUnmarshaller {

    protected RequirementForm rf;
    protected long id;
    protected String name;
    protected String description;
    protected String comments;
    protected Set<Long> sources;
    protected String author;
    protected Date modificationDate;
    protected int numInstances;
    protected int statsNumInstances;
    protected int statsNumAssociates;
    protected int pos;
    protected FixedPartUnmarshaller fixedPart;
    protected Set<ExtendedPartUnmarshaller> extendedParts;
    protected boolean available;

    @JsonCreator
    public RequirementFormUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "sources", required = false) Set<Long> sources,
	    @JsonProperty(value = "author", required = true) String author,
	    @JsonProperty(value = "modificationDate", required = true) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss") Date modificationDate,
	    @JsonProperty(value = "numInstances", required = true) int numInstances,
	    @JsonProperty(value = "statsNumInstances", required = true) int statsNumInstances,
	    @JsonProperty(value = "statsNumAssociates", required = true) int statsNumAssociates,
	    @JsonProperty(value = "pos", required = true) int pos,
	    @JsonProperty(value = "fixedPart", required = true) FixedPartUnmarshaller fixedPart,
	    @JsonProperty(value = "extendedParts", required = false) Set<ExtendedPartUnmarshaller> extendedParts,
	    @JsonProperty(value = "available", required = false) boolean available)
	    throws SemanticallyIncorrectException {
	checkPos(pos);
	this.id = id;
	this.fixedPart = fixedPart;
	this.extendedParts = extendedParts;
	this.sources = sources;
	setBasicValues(name, description, comments, author, modificationDate, numInstances, statsNumInstances,
		statsNumAssociates, pos, available);

    }

    public RequirementFormUnmarshaller(String name, String description, String comments, Set<Long> sources,
	    String author, Date modificationDate, int numInstances, int statsNumInstances, int statsNumAssociates,
	    int pos, FixedPartUnmarshaller fixedPart, Set<ExtendedPartUnmarshaller> extendedParts, boolean available) {
	super();
	this.fixedPart = fixedPart;
	this.extendedParts = extendedParts;
	this.sources = sources;

	setBasicValues(name, description, comments, author, modificationDate, numInstances, statsNumInstances,
		statsNumAssociates, pos, available);
    }

    protected void setBasicValues(String name, String description, String comments, String author,
	    Date modificationDate, int numInstances, int statsNumInstances, int statsNumAssociates, int pos,
	    boolean available) {
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.author = author;
	this.modificationDate = modificationDate;
	this.numInstances = numInstances;
	this.statsNumInstances = statsNumInstances;
	this.statsNumAssociates = statsNumAssociates;
	this.pos = pos;
	this.available = available;
    }

    protected RequirementFormUnmarshaller(long id, String name, String description, String comments, String author,
	    Date modificationDate, int numInstances, int statsNumInstances, int statsNumAssociates, int pos,
	    boolean available) throws SemanticallyIncorrectException {
	super();
	checkPos(pos);
	this.id = id;
	setBasicValues(name, description, comments, author, modificationDate, numInstances, statsNumInstances,
		statsNumAssociates, pos, available);
    }

    protected RequirementFormUnmarshaller(String name, String description, String comments, String author,
	    Date modificationDate, int numInstances, int statsNumInstances, int statsNumAssociates, int pos,
	    boolean available) throws SemanticallyIncorrectException {
	super();
	checkPos(pos);
	setBasicValues(name, description, comments, author, modificationDate, numInstances, statsNumInstances,
		statsNumAssociates, pos, available);
    }

    protected void checkPos(int pos) throws SemanticallyIncorrectException {
	if (pos < 0)
	    throw new SemanticallyIncorrectException("pos must be => 0 in form");
    }

    /**
     * Sets the parts of the form
     * 
     * @throws SemanticallyIncorrectException
     *             Exception with the information to show to user
     */
    protected void setParts() throws SemanticallyIncorrectException {
	buildAndSetFixedPart();

	if (extendedParts == null)
	    extendedParts = new HashSet<>();

	boolean positions[] = new boolean[extendedParts.size()]; // inicializado a falso

	for (ExtendedPartUnmarshaller epu : extendedParts) {
	    checkExtendedPartsBuildAndAdd(positions, epu);

	}
	checkCorrectPosValues(positions);
    }

    /**
     * Builds the fixed part
     * 
     * @throws SemanticallyIncorrectException
     */
    protected void buildAndSetFixedPart() throws SemanticallyIncorrectException {
	rf.setFixedPart((FixedPart) fixedPart.build());
    }

    /**
     * Check the pos value of the extended parts and set inside the array the
     * positions used inside the extended parts, builds the parts and save them in
     * {@link RequirementForm}
     * 
     * @param positions
     *            Array of booleans where to save the positions used
     * @param epu
     *            {@link ExtendedPartUnmarshaller} to build and check
     * @throws SemanticallyIncorrectException
     *             Exception with the needed information to show to user
     */
    protected void checkExtendedPartsBuildAndAdd(boolean[] positions, ExtendedPartUnmarshaller epu)
	    throws SemanticallyIncorrectException {
	try {

	    if (positions[epu.getPos()])
		throw new SemanticallyIncorrectException("Invalid pos value in form extended parts");
	    positions[epu.getPos()] = true;
	    rf.addExtendedPart((ExtendedPart) epu.build());
	} catch (ArrayIndexOutOfBoundsException e) {
	    throw new SemanticallyIncorrectException("Invalid pos value in form extended parts");
	} catch (IntegrityException e) {
	    throw new SemanticallyIncorrectException("Extended part name repeated in form");
	}
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
    protected void checkCorrectPosValues(boolean[] positions) throws SemanticallyIncorrectException {
	for (boolean b : positions) {
	    if (!b)
		throw new SemanticallyIncorrectException("Invalid pos value in form extended parts");
	}
    }

    protected void setSources() throws SemanticallyIncorrectException {
	if (sources == null)
	    sources = new HashSet<>();
	try {
	    rf.setSources(IdToDomainObject.getSources(sources));
	} catch (NotFoundException e) {
	    throw new SemanticallyIncorrectException("Invalid source id in form");
	}

    }

    protected void setName() throws IntegrityException {
	rf.setName(name);
    }

    protected void setFullFields() {
	rf.setPos((short) pos);
    }

    protected void checkSemanticallExceptions() throws SemanticallyIncorrectException {
	if (numInstances < 0)
	    throw new SemanticallyIncorrectException("numInstances must be => 0 in form");
	if (statsNumInstances < 0)
	    throw new SemanticallyIncorrectException("statsNumInstances must be => 0 in form");
	if (statsNumAssociates < 0)
	    throw new SemanticallyIncorrectException("statsNumAssociates must be => 0 in form");
	if (pos < 0)
	    throw new SemanticallyIncorrectException("pos must be => 0 in form");
    }

    public RequirementForm build() throws SemanticallyIncorrectException, IntegrityException {
	checkSemanticallExceptions();
	rf = new RequirementForm();
	rf.setId(id);
	rf.setDescription(description);
	rf.setComments(comments);
	rf.setAuthor(author);
	rf.setVersion(modificationDate);
	rf.setNumInstances(numInstances);
	rf.setStatsNumInstances(statsNumInstances);
	rf.setStatsNumAssociates(statsNumAssociates);
	rf.setAvailable(available);
	setParts();
	setSources();
	setName();
	setFullFields();
	return rf;

    }

    @Override
    public int hashCode() {
	return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
	return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    public int getPos() {
	return pos;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public Set<Long> getSources() {
	return sources;
    }

    public void setSources(Set<Long> sources) {
	this.sources = sources;
    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public Date getModificationDate() {
	return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
	this.modificationDate = modificationDate;
    }

    public int getNumInstances() {
	return numInstances;
    }

    public void setNumInstances(int numInstances) {
	this.numInstances = numInstances;
    }

    public int getStatsNumInstances() {
	return statsNumInstances;
    }

    public void setStatsNumInstances(int statsNumInstances) {
	this.statsNumInstances = statsNumInstances;
    }

    public int getStatsNumAssociates() {
	return statsNumAssociates;
    }

    public void setStatsNumAssociates(int statsNumAssociates) {
	this.statsNumAssociates = statsNumAssociates;
    }

    public void setPos(int pos) {
	this.pos = pos;
    }

    public boolean isAvailable() {
	return available;
    }

    public void setAvailable(boolean available) {
	this.available = available;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public FixedPartUnmarshaller getFixedPart() {
	return fixedPart;
    }

    public void setFixedPart(FixedPartUnmarshaller fixedPart) {
	this.fixedPart = fixedPart;
    }

    public Set<ExtendedPartUnmarshaller> getExtendedParts() {
	return extendedParts;
    }

    public void setExtendedParts(Set<ExtendedPartUnmarshaller> extendedParts) {
	this.extendedParts = extendedParts;
    }

}
