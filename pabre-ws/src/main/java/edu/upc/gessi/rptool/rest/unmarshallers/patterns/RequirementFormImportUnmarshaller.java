package edu.upc.gessi.rptool.rest.unmarshallers.patterns;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.IdToDomainObject;
import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;
import edu.upc.gessi.rptool.domain.patternelements.FixedPart;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class RequirementFormImportUnmarshaller extends RequirementFormUnmarshaller {
    protected Set<String> sources;
    protected FixedPartImportUnmarshaller fixedPart;
    protected Set<ExtendedPartImportUnmarshaller> extendedParts;

    @JsonCreator
    public RequirementFormImportUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "sourcesByIdentifier", required = false) Set<String> sources,
	    @JsonProperty(value = "author", required = true) String author,
	    @JsonProperty(value = "modificationDate", required = true) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss") Date modificationDate,
	    @JsonProperty(value = "numInstances", required = true) int numInstances,
	    @JsonProperty(value = "statsNumInstances", required = true) int statsNumInstances,
	    @JsonProperty(value = "statsNumAssociates", required = true) int statsNumAssociates,
	    @JsonProperty(value = "pos", required = true) int pos,
	    @JsonProperty(value = "fixedPart", required = true) FixedPartImportUnmarshaller fixedPart,
	    @JsonProperty(value = "extendedParts", required = false) Set<ExtendedPartImportUnmarshaller> extendedParts,
	    @JsonProperty(value = "available", required = false) boolean available)
	    throws SemanticallyIncorrectException {

	super(id, name, description, comments, author, modificationDate, numInstances, statsNumInstances,
		statsNumAssociates, pos, available);

	this.sources = sources;
	this.fixedPart = fixedPart;
	this.extendedParts = extendedParts;

    }

    @Override
    protected void setParts() throws SemanticallyIncorrectException {

	buildAndSetFixedPart();

	if (extendedParts == null)
	    extendedParts = new HashSet<>();
	boolean positions[] = new boolean[extendedParts.size()];

	for (ExtendedPartImportUnmarshaller extendedPartImportUnmarshaller : extendedParts) {
	    checkExtendedPartsBuildAndAdd(positions, extendedPartImportUnmarshaller);
	}

	checkCorrectPosValues(positions);
    }

    @Override
    protected void buildAndSetFixedPart() throws SemanticallyIncorrectException {
	rf.setFixedPart((FixedPart) fixedPart.build());
    }

    @Override
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

    @Override
    protected void setSources() throws SemanticallyIncorrectException {
	if (sources == null)
	    sources = new HashSet<>();
	try {
	    rf.setSources(IdToDomainObject.getSourcesByIdentifiers(sources));
	} catch (NotFoundException e) {
	    throw new SemanticallyIncorrectException("Invalid source id in form");
	}

    }

    public boolean checkAllItemsContainsID() {
	boolean b = id != 0;
	if (!fixedPart.checkAllItemsContainsID()) {
	    b = false;
	}
	if (extendedParts != null) {
	    for (ExtendedPartImportUnmarshaller ext : extendedParts) {
		if (!ext.checkAllItemsContainsID()) {
		    b = false;
		}
	    }
	}

	return b;
    }
}
