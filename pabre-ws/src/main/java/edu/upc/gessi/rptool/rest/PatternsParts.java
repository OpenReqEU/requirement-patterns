package edu.upc.gessi.rptool.rest;

import java.util.Set;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.data.PatternDataController;
import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;
import edu.upc.gessi.rptool.domain.patternelements.FixedPart;
import edu.upc.gessi.rptool.domain.patternelements.PatternItem;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.rest.dtos.patternelements.ExtendedPartDTO;
import edu.upc.gessi.rptool.rest.dtos.patternelements.FixedPartDTO;
import edu.upc.gessi.rptool.rest.dtos.patternelements.PatternItemDTO;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementFormPartsDTO;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.ExtendedPartsUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.PatternItemPutUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.updaters.RequirementPatternItemUpdater;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/patterns/{patternId}/versions/{versionId}/forms/{formId}/parts")
@Api(value = "Pattern parts", produces = MediaType.APPLICATION_JSON)
public class PatternsParts {

    private static final Logger logger = Logger.getLogger(PatternsParts.class.getName());

    private static final String PART_CREATE_TEXT = "Create/Substitute the parts in forms.<br/><br/>"
	    + "<strong>Important information</strong>" + "<ul> "
	    + "<li>All the <strong>metrics</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li>This call only substitute the extended parts.</li>" + "</ul>";

    /**
     * Get all the parts of the given form
     * 
     * @param patternId
     *            ID of the pattern
     * @param versionId
     *            ID of the version
     * @param formId
     *            ID of the form where the parts are located
     * @return Fixed part, and list of all the extended parts
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get all the parts of the given form", notes = "Get all the parts from the given form", response = RequirementFormPartsDTO.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: Parts obtained", response = RequirementFormPartsDTO.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public RequirementFormPartsDTO getPatternVersionFormParts(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId,
	    @ApiParam(value = "ID of the form where the parts are located", required = true) @PathParam("formId") long formId) {
	logger.debug("Obtaining all the parts");
	RequirementPatternVersion rpv = Patterns.retrieveRequirementPatternVersion(versionId, patternId);
	RequirementForm rf = Patterns.retrieveRequirementForm(versionId, formId, rpv);
	return new RequirementFormPartsDTO(rf.getFixedPart(), rf.getExtendedParts(), patternId, versionId, formId);
    }

    /**
     * Create/Substitute the parts in forms
     * 
     * @param replaceExtendedUnmarshaller
     *            Unmarshaller with new extended parts
     * @param patternId
     *            Pattern ID
     * @param versionId
     *            Version ID
     * @param formId
     *            Form ID
     * @return HTTP response
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.TEXT_PLAIN })
    @ApiOperation(value = "Create/Substitute the parts in forms", notes = PART_CREATE_TEXT, response = Response.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: Part created or replaced correctly", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of something semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updateExtendedPartsInForm(
	    @ApiParam(value = "Unmarshaller with new extended parts", required = true) ExtendedPartsUnmarshaller replaceExtendedUnmarshaller,
	    @ApiParam(value = "Pattern ID", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "Version ID", required = true) @PathParam("versionId") long versionId,
	    @ApiParam(value = "Form ID", required = true) @PathParam("formId") long formId) throws Exception {
	RequirementForm rf = Patterns.getFormAndCheckExceptions(versionId, patternId, formId); // get the form
	// Build the unmarshaller
	Set<ExtendedPart> extendedParts = replaceExtendedUnmarshaller.build();
	// call the controller to Substitute the parts
	PatternDataController.substituteNewPartsIntoForm(extendedParts, rf);
	return Response.status(Status.OK).build();

    }

    /**
     * Get a pattern item by ID
     * 
     * @param patternId
     *            Pattern ID
     * @param versionId
     *            Version ID
     * @param formId
     *            Form ID
     * @param partId
     *            Part Item ID
     * @return PatternItem DTO
     * @throws Exception
     */
    @GET
    @Path("{partId}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get a part given a ID", notes = "Get a part by the given ID", response = PatternItemDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Part obtained", response = PatternItemDTO.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public PatternItemDTO getPatternVersionFormPart(
	    @ApiParam(value = "ID of the Pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the Version", required = true) @PathParam("versionId") long versionId,
	    @ApiParam(value = "ID of the Form", required = true) @PathParam("formId") long formId,
	    @ApiParam(value = "ID of the Part", required = true) @PathParam("partId") long partId) throws BadRequestException {
	PatternItem pi = Patterns.retreivePatternItem(patternId, versionId, formId, partId);
	// Depending on the pattern item type create corresponding DTO
	if (pi instanceof FixedPart) {
	    return new FixedPartDTO((FixedPart) pi, patternId, versionId, formId, pi.getArtifactRelation());
	} else if (pi instanceof ExtendedPart) {
	    return new ExtendedPartDTO((ExtendedPart) pi, patternId, versionId, formId, pi.getArtifactRelation());
	} else {
	    throw new BadRequestException("Requested Part cannot be serialized, dosen't have a DTO");
	}
    }

    /**
     * Update a Pattern item
     * 
     * @param partUpdaterUnmarshaller
     *            Unmarshaller with the fields to update
     * @param patternId
     *            ID of the Pattern
     * @param versionId
     *            ID of the Version
     * @param formId
     *            ID of the Form
     * @param partId
     *            ID of the Part
     * @return HTTP response
     * @throws Exception
     */
    @PUT
    @Path("{partId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.TEXT_PLAIN })
    @ApiOperation(value = "Update a part item", notes = "Update the given fields of the part, only the fields numInstaces, available and statsNumInstaces", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Part updated", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested pattern, version, form, part is not found.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of somehing semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updatePatternItemInForm(
	    @ApiParam(value = "Unmarshaller with the fields to update", required = true) PatternItemPutUnmarshaller partUpdaterUnmarshaller,
	    @ApiParam(value = "ID of the Pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the Version", required = true) @PathParam("versionId") long versionId,
	    @ApiParam(value = "ID of the Form", required = true) @PathParam("formId") long formId,
	    @ApiParam(value = "ID of the Part", required = true) @PathParam("partId") long partId) throws SemanticallyIncorrectException, UnrecognizedPropertyException {

	PatternItem pi = Patterns.retreivePatternItem(patternId, versionId, formId, partId); // obtain pattern Item
	new RequirementPatternItemUpdater(pi, partUpdaterUnmarshaller).update(); // create a updater and update the
										 // pattern item
	return Response.status(Status.OK).build();
    }

}