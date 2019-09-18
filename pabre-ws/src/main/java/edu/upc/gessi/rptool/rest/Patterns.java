package edu.upc.gessi.rptool.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.data.PatternDataController;
import edu.upc.gessi.rptool.data.SchemaDataController;
import edu.upc.gessi.rptool.domain.PatternObjectCompleteDependency;
import edu.upc.gessi.rptool.domain.patternelements.PatternItem;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.dtos.CompletePatternDependenciesDTO;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementFormDTO;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementPatternDTO;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.RequirementPatternImportUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.RequirementPatternUnmarshaller;
import edu.upc.gessi.rptool.rest.utilities.IdFormatter;
import edu.upc.gessi.rptool.rest.utilities.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.uima.UIMAException;

@Path("/patterns")
@Api(value = "Patterns", produces = MediaType.APPLICATION_JSON)
public class Patterns {

    private static final Logger logger = Logger.getLogger(Patterns.class.getName());
    private static final String NAMELIST_TEXT = "When is provided then the system will filter the patterns and will return only that ones who are inside the indicated internalClassifiers. "
	    + "The classifier is indicated as a path straight from the schema followed by RootClassifier and followed by N internalCLassifiers and the call will return the patterns inside the last internalClassifier."
	    + "<strong>The list must have at least 3 names to search (SchemaClassifier/RootClassifier/InternalClassifier) and it must be <a href=\"https://www.w3schools.com/tags/ref_urlencode.asp\">encoded</a>.</strong>"
	    + "<ul><li>EXAMPLE: Get patterns from the inernalclassifier: <strong>schemaname1/rootname1/internal1/internal2/internal3</strong>. "
	    + "we have to make the request: /patterns?namesList=<strong>schemaname1</strong>&namesList=<strong>rootname1</strong>&namesList=<strong>internal1</strong>&namesList=<strong>internal2</strong>&namesList=<strong>internal3</strong></li></ul><br/>"
	    + "In case that you just give one name in the list is considered that the name of the classifiers are unique in this catalogue, so the search will happen in that classifier given in the list, otherwise returns a Error";
    private static final String PATTERN_CREATE_TEXT = "Create a new pattern <br/><br/>"
	    + "<strong>Important information</strong>" + "<ul> "
	    + "<li>All the <strong>keywords</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li>All the <strong>sources</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li>All the <strong>metrics</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li>If the ID of the element is provided, that ID it will be assigned.</li>" + "</ul>";
    private static final String PATTERN_UPDATE_TEXT = "Update the given pattern, if the ID is provided that ID will try to use, if the ID is already used throw a code 400 "
	    + "<strong>Important information</strong>" + "<ul> "
	    + "<li>All the <strong>Versions</strong>, <strong>Forms</strong>, <strong>FixedPart</strong>, <strong>ExtendedPart</strong>, <strong>Parameters</strong> <strong>MUST</strong> have a ID field.</li>"
	    + "<li>If you change any element ID it will be changed inside the pattern.</li>"
	    + "<li>All the <strong>keywords</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li>All the <strong>sources</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li>All the <strong>metrics</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "</ul>";

    /**
     * Get the patterns from the catalog
     * 
     * @param keyword
     *            (Optional) Keyword that must have inside the pattern
     * @param namesList
     *            (Optional) List with the path of the concrete InternalClassifier
     *            where you want to get the patterns. The minimum number of elements
     *            are 3 (Schema name, RootClassifier name and InternalClassifier
     *            name). In case that the list contains only 1 name, it's considered
     *            the name of the classifiers are unique in this catalogue, so the
     *            search will happen on the given name, in case that exists more
     *            then one classifier with same name returns a error.
     * @param isComplete
     *            (Optional) Boolean telling that if the pattern should have all the
     *            fields or not. In case that searching patterns with the given
     *            classifier this boolean indicates that should give all the inner
     *            classifiers patterns too or not.
     * @param isRecursive
     *            (Optional) Usefull only if the search is by classifiers names, if
     *            this parameter is true,then returns not only the patterns of that
     *            classifier, returns also the inner classifiers patterns
     * @return Set of the patterns found with the corresponding filters
     * @throws ValueException
     *             Exception thrown when the given parameters are wrong
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "List all the pattern in the catalogue", notes = "List all the pattern in the catalogue", response = RequirementPatternDTO.class, responseContainer = "Set")
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: The request has succeeded.", response = RequirementPatternDTO.class, responseContainer = "Set"),
	    @ApiResponse(code = 404, message = "Not Found: The requested pattern, version is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Set<RequirementPatternDTO> getPatterns(
	    @ApiParam(value = "Keyword that must have inside the pattern", required = false) @DefaultValue("") @QueryParam("keyword") String keyword,
	    @ApiParam(value = NAMELIST_TEXT, required = false) @QueryParam("namesList") List<String> namesList,
	    @ApiParam(value = "Boolean telling that if the pattern should have all the fields or not, In case we are searching with the classifier names, if this parameter is true,"
		    + " then returns not only the patterns of that classifier, returns also the inner classifiers patterns", required = false) @DefaultValue("false") @QueryParam("complete") boolean isComplete,
	    @ApiParam(value = "Usefull only if the search is by classifiers names, if this parameter is true,"
		    + " then returns not only the patterns of that classifier, returns also the inner classifiers patterns", required = false) @DefaultValue("false") @QueryParam("completeClassifiers") boolean isRecursive)
	    throws ValueException {
	logger.info("Getting all the patterns");

	List<RequirementPattern> lrp; // List used to store pattern which are complete

	if (!keyword.equals("")) { // Case when some one indicate the keywords to filter
	    lrp = PatternDataController.listPatternWithGivenKeyword(keyword); // get all the patterns with that keyword

	} else if (namesList != null && !namesList.isEmpty()) {
	    // Case when someone wants patterns from a specified InternalClassifier
	    lrp = getPatternsInClassifiers(namesList, isRecursive);

	} else { // Case when dosen't exists any filter
	    lrp = PatternDataController.listPatterns();// Get all the pattern
	}

	// Create all the DTOs to return
	Set<RequirementPatternDTO> rpDTOs = new TreeSet<>(); // Set use to return the pattern DTO
	// create all the DTO for the list with the complete pattern
	for (int i = 0; i < lrp.size(); i++) {
	    RequirementPattern rp = lrp.get(i);
	    rpDTOs.add(new RequirementPatternDTO(rp));
	}

	if (!isComplete) {
	    // If the request patterns are not complete, reduce the DTO to reduced version
	    // with less fields
	    for (RequirementPatternDTO requirementDTO : rpDTOs) {
		requirementDTO.reduceFields();
	    }
	}

	return rpDTOs;
    }

    /**
     * Create a new pattern
     * 
     * @param unmarshaller
     *            Unmarshaller with the new pattern fields
     * @return ID of the created pattern
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a new Pattern", notes = PATTERN_CREATE_TEXT, response = IdFormatter.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: Pattern created correctly", response = IdFormatter.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of something semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public IdFormatter createPattern(
	    @ApiParam(value = "Unmarshaller with the new pattern fields", required = true) RequirementPatternUnmarshaller unmarshaller)
	    throws UIMAException, SemanticallyIncorrectException, IntegrityException {
	logger.info("Creating pattern");
	RequirementPattern rp = null;
	rp = unmarshaller.build(); // build the unmarshaller
	PatternDataController.saveNewPattern(rp); // Call the controller to save the pattern
	return new IdFormatter(rp.getId());
    }

    /**
     * Get a pattern with the given ID
     * 
     * @param patternId
     *            ID of the pattern
     * @return Pattern with the given ID
     */
    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get a pattern by ID", notes = "Return the pattern request by ID", response = RequirementPatternDTO.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: Pattern obtained", response = RequirementFormDTO.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public RequirementPatternDTO getPattern(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("id") long patternId) {
	logger.info("Retrieving pattern...");
	RequirementPattern rp = retrieveRequirementPattern(patternId); // Obtain the pattern
	return new RequirementPatternDTO(rp); // return the DTO with that pattern
    }

    /**
     * Update a pattern
     * 
     * @param unmarshaller
     *            Unmarshaller with the new pattern fields
     * @param patternId
     *            ID of the pattern to update
     * @return HTTP response
     * @throws Exception
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update a pattern", notes = PATTERN_UPDATE_TEXT, response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Pattern updated", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested pattern is not found.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of somehing semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updatePattern(
	    // Using import unmarshaller to reuse the code
	    @ApiParam(value = "Unmarshaller with the new pattern fields", required = true) RequirementPatternImportUnmarshaller unmarshaller,
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("id") long patternId) throws SemanticallyIncorrectException, NotFoundException, IntegrityException, UIMAException {
	logger.info("Updating pattern");
	RequirementPattern rp = retrieveRequirementPattern(patternId); // obtain the requirement pattern
	PatternDataController.deletePattern(rp);// Delete older pattern
	logger.debug("Old pattern deleted");
	GenericDataController.flush();
	boolean b = unmarshaller.checkAllItemsContainsID();
	if (!b) {
	    throw new NotFoundException("Missing ID in one of the fields");
	}

	rp = unmarshaller.build(); // Build the new Pattern
	PatternDataController.saveNewPattern(rp); // Save the new pattern

	return Response.status(Status.OK).build();
    }

    /**
     * Delete a pattern with the given ID
     * 
     * @param patternId
     *            Pattern object ID
     * @return HTTP response
     * @throws Exception
     */
    @DELETE
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Delete a Pattern", notes = "Deletes a pattern given the ID", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Pattern deleted", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested pattern is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deletePattern(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("id") long patternId) throws SemanticallyIncorrectException {
	logger.info("Deleting pattern");
	RequirementPattern rp = retrieveRequirementPattern(patternId);
	PatternDataController.deletePattern(rp);
	return Response.status(Status.OK).build();

    }

    /**
     * Get all the dependencies of the given pattern and all the components inside
     * of it (version, forms, parts, etc.)
     * 
     * @param id
     *            Pattern object ID
     * @return DTO with all the dependencies
     */
    @GET
    @Path("{id}/dependencies")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get all the dependencies of the pattern", notes = "Get all the dependencies of the given pattern and all the components inside of it (version, forms, parts, etc.)<br/>"
	    + "Given a ID of pattern, returns a list of all the dependencies of this patterns, includes the follow elements dependencies::<ul>"
	    + "<li>RequirementPattern</li>" + "<li>PatternVersion</li>" + "<li>RequirementForm</li>"
	    + "<li>FixedPart</li>" + "<li>ExtendedPart</li>" + "<li>Params</li>"
	    + "</ul>", response = CompletePatternDependenciesDTO.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: Request processed", response = CompletePatternDependenciesDTO.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public CompletePatternDependenciesDTO getPatternCompleteDependencies(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("id") long id) {
	logger.info("Getting dependencies");
	// Obtain the requirement pattern
	RequirementPattern rp = retrieveRequirementPattern(id);
	// call the method to obtain all the dependencies
	Set<PatternObjectCompleteDependency> l = rp.getAllPatternDependencies();
	// Create all the DTOs with the dependencies
		return new CompletePatternDependenciesDTO(rp, l);
    }

    // PRIVATE USAGE METHOD

    /**
     * This method returns the patterns of the given names of classifiers.
     * <ul>
     * <li>List with only one name, do a search in the catalogue and returns the
     * patterns in that classifier</li>
     * <li>List with 3 or more names, get the patterns under that tree of
     * classifiers (minimum 3 name: Schema, Root, Classifier)</li>
     * </ul>
     * 
     * @param namesList
     *            List with the path of the concrete InternalClassifier where you
     *            want to get the patterns. The minimum number of elements are 3
     *            (Schema name, RootClassifier name and InternalClassifier name). In
     *            case that there are only one name in the list it will search that
     *            classifier considering that the name of the classifiers are unique
     *            in the current catalogue
     * @param isRecursive
     *            if this parameter is true, then returns not only the patterns of
     *            that classifier, returns also the inner classifiers patterns
     * @return List of pattern accomplishing the indicated criteria
     * @throws ValueException
     */
    protected List<RequirementPattern> getPatternsInClassifiers(List<String> namesList, boolean isRecursive)
	    throws ValueException {
	List<RequirementPattern> lrp;
	// Get the last internal classifier of the given list of names
	Classifier ic = null;
	if (namesList.size() == 1) {
	    ic = SchemaDataController.getClassifierByName(namesList.get(0));
	} else {
	    ic = Classifiers.getLastInternalClassifier(namesList);
	}

	if (ic == null) { // when the classifier is not found throw Not found exception
	    throw new NotFoundException("One of the indicated classifiers in the route are not found");
	}

	if (isRecursive) {
	    // Obtain the list of patterns in the given classifier and the inner classifiers
	    lrp = PatternDataController.listPatternsClassifierAndAllInnerClassifiers(ic);
	} else {
	    // Obtain the list of pattern in the given classifier
	    lrp = Util.asSortedList(ic.getPatterns());
	}
	return lrp;
    }

    // STATIC METHODS TO OBTAIN OBJECT FROM CONTROLLER

    /**
     * Obtain the form from the catalog after checking the version exists
     * 
     * @param versionId
     *            ID of the version
     * @param patternId
     *            ID of the pattern
     * @param formId
     *            ID of the form
     * @return Requirement form
     */
    public static RequirementForm getFormAndCheckExceptions(long versionId, long patternId, long formId) {
	RequirementPatternVersion rpv = retrieveRequirementPatternVersion(versionId, patternId);
	logger.debug("RequirementForm, getFormAndCheckException, Checking: " + rpv.getRequirementPattern().getId());
		return retrieveRequirementForm(versionId, formId, rpv);
    }

    /**
     * Retrieve a Form
     * 
     * @param versionId
     *            ID of the version
     * @param patternId
     *            ID of the pattern
     * @param formId
     *            ID of the form
     * @return Requirement form
     */
    public static RequirementForm retrieveRequirementForm(long versionId, long formId, RequirementPatternVersion rpv) {
	RequirementForm rf = rpv.getFormById(formId);
	if (rf == null)
	    throw new NotFoundException(
		    "RequirementForm [" + formId + "] of Requirement Pattern Version [" + versionId + "] not found");
	return rf;
    }

    /**
     * Retrieve pattern item
     * 
     * @param patternId
     *            Pattern id
     * @param versionId
     *            Version ID
     * @param formId
     *            Form ID
     * @param partId
     *            Part ID
     * @return Pattern item searched
     */
    public static PatternItem retreivePatternItem(long patternId, long versionId, long formId, long partId) {
	RequirementPatternVersion rpv = retrieveRequirementPatternVersion(versionId, patternId); // Obtain pattern
												 // version
	RequirementForm rf = retrieveRequirementForm(versionId, formId, rpv); // Obtain the form
	PatternItem pi = rf.getPatternItemById(partId); // Obtain the pattern

	if (pi == null)
	    throw new NotFoundException(
		    "RequirementForm-Part [" + partId + "] of Requirement Form [" + formId + "] not found");
	return pi;
    }

    /**
     * Gets RequirementPatternVersion from the DB, throws {@link NotFoundException}
     * when the RequirementPatternVersion is not found
     * 
     * @param versionId
     *            ID of the required RequirementPatternVersion
     * @return Requested {@link RequirementPatternVersion}
     */
    public static RequirementPatternVersion retrieveRequirementPatternVersion(long versionId, long patternId) {
	RequirementPatternVersion rpv = PatternDataController.getPatternVersion(versionId);
	if (rpv == null || rpv.getRequirementPattern().getId() != patternId)
	    throw new NotFoundException(
		    "Version [" + versionId + "] of Requirement Pattern [" + patternId + "] not found");
	return rpv;

    }

    /**
     * Gets RequirementPattern from the DB, throws {@link NotFoundException} when
     * the RequirementPattern is not found
     * 
     * @param patternId
     *            ID of the required RequirementPattern
     * @return Requested {@link RequirementPattern}
     */
    public static RequirementPattern retrieveRequirementPattern(long patternId) {
	RequirementPattern m = PatternDataController.getPattern(patternId);
	if (m == null)
	    throw new NotFoundException("RequirementPattern [" + patternId + "] not found");
	return m;

    }

}