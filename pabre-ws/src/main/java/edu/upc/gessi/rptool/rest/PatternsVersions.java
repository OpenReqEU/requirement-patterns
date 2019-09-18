package edu.upc.gessi.rptool.rest;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.data.PatternDataController;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementPatternVersionDTO;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementPatternVersionReducedDTO;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.RequirementPatternVersionPutUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.RequirementPatternVersionsUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.updaters.RequirementPatternVersionUpdater;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/patterns/{patternId}/versions")
@Api(value = "Pattern versions", produces = MediaType.APPLICATION_JSON)
public class PatternsVersions {

    private static final Logger logger = Logger.getLogger(PatternsVersions.class.getName());

    private static final String ADD_VERSION_TEXT = "Add a new version to the pattern <br/><br/>"
	    + "<strong>Important information</strong>" + "<ul> "
	    + "<li>All the <strong>keywords</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li>All the <strong>sources</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li>All the <strong>metrics</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "</ul>";

    private static final String UPDATE_VERSION_TEXT = "Update the given fields of the version. <br/><br/>"
	    + "<strong>Important information</strong>" + "<ul> "
	    + "<li>All the <strong>keywords</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li>All the <strong>sources</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li><strong>forms</strong> field cannot be updated.</li>" + "</ul>";

    /**
     * Get all the pattern reduced information versions
     * 
     * @param patternId
     *            ID of the pattern
     * @return Set of pattern versions
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "List all the versions of the pattern", notes = "List all the versions of the given pattern, with reduced information", response = RequirementPatternVersionReducedDTO.class, responseContainer = "Set")
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: The request has succeeded.", response = RequirementPatternVersionReducedDTO.class, responseContainer = "Set"),
	    @ApiResponse(code = 404, message = "Not Found: The requested pattern is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Set<RequirementPatternVersionReducedDTO> getPatternVersions(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId) {
	logger.debug("Getting all the versions");
	RequirementPattern rp = Patterns.retrieveRequirementPattern(patternId); // get the pattern
	// create a Treeset to sort the version
	SortedSet<RequirementPatternVersionReducedDTO> rpvSet = new TreeSet<>();
	for (RequirementPatternVersion rpv : rp.getVersions()) {
	    rpvSet.add(new RequirementPatternVersionReducedDTO(rpv));// create the DTO and add it to the treeset
	}
	return rpvSet;
    }

    /**
     * Add new version to the pattern
     * 
     * @param unmarshaller
     *            unmarshaller with new version to add
     * @param patternId
     *            ID of the pattern
     * @return HTTP response
     * @throws Exception
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.TEXT_PLAIN })
    @ApiOperation(value = "Add a new version to the pattern", notes = ADD_VERSION_TEXT, response = Response.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: Version added correctly", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of something semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updatePatternVersions(
	    @ApiParam(value = "unmarshaller with new version to add", required = true) RequirementPatternVersionsUnmarshaller unmarshaller,
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId)
	    throws Exception {
	logger.debug("Updating all the versions");
	RequirementPattern rp = Patterns.retrieveRequirementPattern(patternId); // get the pattern
	Set<RequirementPatternVersion> versions = unmarshaller.build();
	// Call the controller to add the version to the pattern
	PatternDataController.saveNewVersionsIntoPattern(rp, versions);
	return Response.status(Status.OK).build();
    }

    /**
     * Get a version with the given a ID
     * 
     * @param patternId
     *            ID of the pattern
     * @param versionId
     *            ID of the version
     * @return Requested version
     */
    @GET
    @Path("{versionId}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get a version with the given a ID", notes = "Get a version with the given a ID", response = RequirementPatternVersionDTO.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: Version obtained", response = RequirementPatternVersionDTO.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public RequirementPatternVersionDTO getPatternVersion(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId) {
	logger.debug("Retreiving version");
	RequirementPatternVersion rpv = Patterns.retrieveRequirementPatternVersion(versionId, patternId);
	return new RequirementPatternVersionDTO(rpv);
    }

    /**
     * Update a pattern version with the new fields
     * 
     * @param unmarshaller
     *            String in JSON format
     * @param patternId
     *            Pattern ID
     * @param versionId
     *            Version ID
     * @return HTTP response
     * @throws Exception
     */
    @PUT
    @Path("{versionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update a version with the new fields", notes = UPDATE_VERSION_TEXT, response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Form updated", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested pattern, version is not found.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of somehing semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updateVersion(
	    @ApiParam(value = "Unmarshaller with the new fields to update", required = true) RequirementPatternVersionPutUnmarshaller unmarshaller,
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId)
	    throws Exception {
	logger.debug("Updating version fields");
	// obtain the pattern version
	RequirementPatternVersion rpv = Patterns.retrieveRequirementPatternVersion(versionId, patternId);
	// create the updater and call to update the fields
	new RequirementPatternVersionUpdater(rpv, unmarshaller).update();
	return Response.status(Status.OK).build();
    }

    /**
     * Delete a version
     * 
     * @param unmarshaller
     *            String in JSON format
     * @param id
     *            Pattern ID
     * @param versionId
     *            Version ID
     * @return HTTP response
     * @throws Exception
     */
    @DELETE
    @Path("{versionId}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Delete a Version", notes = "Deletes a version given the ID", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Version deleted", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested pattern, version is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deletePatternVersion(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long id,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId) {
	logger.debug("Deleting version");
	RequirementPattern rp = Patterns.retrieveRequirementPattern(id);// get the pattern

	if (rp.getVersions().size() == 1) {
	    // if the pattern has only 1 version throw exception, because pattern must have
	    // 1 version
	    return Response.status(Status.BAD_REQUEST).entity("RequirementPattern[" + id + "] only has one version")
		    .type("text/plain").build();
	}

	// Get the version
	RequirementPatternVersion v = Patterns.retrieveRequirementPatternVersion(versionId, id);
	// call the controller to delete the version
	rp.getVersions().remove(v);
	PatternDataController.deletePatternVersion(v);

	return Response.status(Status.OK).build();
    }

}