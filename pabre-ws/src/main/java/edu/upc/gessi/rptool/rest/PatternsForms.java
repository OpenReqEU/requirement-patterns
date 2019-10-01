package edu.upc.gessi.rptool.rest;

import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAException;

import edu.upc.gessi.rptool.data.NLPController;
import edu.upc.gessi.rptool.data.PatternDataController;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementFormDTO;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.RequirementFormPutUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.RequirementFormsUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.updaters.RequirementFormUpdater;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/patterns/{patternId}/versions/{versionId}/forms")
@Api(value = "Pattern forms", produces = MediaType.APPLICATION_JSON)
public class PatternsForms {

    private static final Logger logger = Logger.getLogger(PatternsForms.class.getName());
    private static final String FORM_SUBSTITUTE_TEXT = "Create/Substitute the forms in forms.<br/><br/>"
	    + "<strong>Important information</strong>" + "<ul> "
	    + "<li>All the <strong>metrics</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li>Delete all the older forms.</li>" + "</ul>";
    private static final String FORM_UPDATE_TEXT = "Update the given fields of the form.<br/><br/>"
	    + "<strong>Important information</strong>" + "<ul> "
	    + "<li>To update fixed part or extended part call the corresponding part path</li>" + "</ul>";

    /**
     * Get all the forms of a given version
     * 
     * @param patternId
     *            ID of the pattern
     * @param versionId
     *            ID of the version
     * @return Set of form DTO
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "List all the form of the given version", notes = "List all the form of the given version", response = RequirementFormDTO.class, responseContainer = "Set")
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: The request has succeeded.", response = RequirementFormDTO.class, responseContainer = "Set"),
	    @ApiResponse(code = 404, message = "Not Found: The requested pattern, version is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Set<RequirementFormDTO> getPatternVersionForms(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId) {
	logger.debug("Getting all the form");
	// obtain the pattern version
	RequirementPatternVersion rpv = Patterns.retrieveRequirementPatternVersion(versionId, patternId);
	Set<RequirementForm> formsSet = rpv.getForms(); // get all the forms of that version
	Set<RequirementFormDTO> formsDTOSet = new TreeSet<RequirementFormDTO>();
	for (RequirementForm rf : formsSet) {// for each form create DTO and add it the set
	    formsDTOSet.add(new RequirementFormDTO(rf, patternId, versionId));
	}

	return formsDTOSet;
    }

    /**
     * Substitute all the forms of the given version
     * 
     * @param formCreationUnmarshaller
     *            Unmarshaller with the form field to create/replace
     * @param patternId
     *            ID of the pattern
     * @param versionId
     *            ID of the version
     * @return HTTP response
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.TEXT_PLAIN })
    @ApiOperation(value = "Create/Substitute the forms in a version", notes = FORM_SUBSTITUTE_TEXT, response = Response.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: Form created/replaced correctly", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of something semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response substituteFormsOnVersion(
	    @ApiParam(value = "Unmarshaller with the form field to create/replace", required = true) RequirementFormsUnmarshaller formCreationUnmarshaller,
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId)
	    throws Exception {
	logger.debug("Subtitiong form");
	RequirementPatternVersion rpv = Patterns.retrieveRequirementPatternVersion(versionId, patternId);
	Set<RequirementForm> forms = formCreationUnmarshaller.build();
	PatternDataController.substituteNewFormsIntoVersion(forms, rpv);
	return Response.status(Status.OK).build();
    }

    /**
     * Get a form with the given a ID
     * 
     * @param patternId
     *            ID of the pattern
     * @param versionId
     *            ID of the version
     * @param formId
     *            ID of the form
     * 
     * @return Form DTO with all the fields
     */
    @GET
    @Path("{formId}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get a form with the given a ID", notes = "Get a form with the given a ID", response = RequirementFormDTO.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: Form obtained", response = RequirementFormDTO.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public RequirementFormDTO getPatternVersionForm(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId,
	    @ApiParam(value = "ID of the form", required = true) @PathParam("formId") long formId) {
	logger.debug("Getting form with ID: " + formId);
	RequirementPatternVersion rpv = Patterns.retrieveRequirementPatternVersion(versionId, patternId);
	RequirementForm rf = Patterns.retrieveRequirementForm(versionId, formId, rpv);
	RequirementFormDTO formDTO = new RequirementFormDTO(rf, patternId, versionId);
	return formDTO;
    }

    /**
     * Update the given fields of the form. NOTE: To update fixed part or extended
     * part call the corresponding part path
     * 
     * @param updateUnmarshaller
     *            Unmarshaller with all the fields to update
     * @param patternId
     *            ID of the pattern
     * @param versionId
     *            ID of the version
     * @param formId
     *            ID of the form
     * @return HTTP response
     * @throws Exception
     */
    @PUT
    @Path("{formId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Update a form", notes = FORM_UPDATE_TEXT, response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Form updated", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested pattern, version, form is not found.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of somehing semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updateForm(
	    @ApiParam(value = "Unmarshaller with all the fields to update", required = false) RequirementFormPutUnmarshaller updateUnmarshaller,
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId,
	    @ApiParam(value = "ID of the form", required = true) @PathParam("formId") long formId) throws Exception {
	logger.debug("Updating form with ID: " + formId);
	RequirementPatternVersion rpv = Patterns.retrieveRequirementPatternVersion(versionId, patternId);
	RequirementForm f = Patterns.retrieveRequirementForm(versionId, formId, rpv);
	new RequirementFormUpdater(f, updateUnmarshaller).update();
	rpv.setLemmatizedVersion(NLPController.lemmatizeVersion(rpv));
	PatternDataController.update(rpv);
	return Response.status(Status.OK).build();
    }

    @DELETE
    @Path("{formId}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Delete a Form", notes = "Deletes a form given the ID", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Form deleted", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested pattern, version, form is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deleteForm(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId,
	    @ApiParam(value = "ID of the form", required = true) @PathParam("formId") long formId)
	    throws IntegrityException, UIMAException {
	RequirementPatternVersion rpv = Patterns.retrieveRequirementPatternVersion(versionId, patternId);
	RequirementForm f = Patterns.retrieveRequirementForm(versionId, formId, rpv);
	PatternDataController.deleteForm(f);
	rpv.setLemmatizedVersion(NLPController.lemmatizeVersion(rpv));
	return Response.status(Status.OK).build();

    }

}