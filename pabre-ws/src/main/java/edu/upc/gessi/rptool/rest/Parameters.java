package edu.upc.gessi.rptool.rest;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.data.ObjectDataController;
import edu.upc.gessi.rptool.data.PatternDataController;
import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.domain.patternelements.PatternItem;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.ParametersUnmarshaller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/patterns/{patternId}/versions/{versionId}/forms/{formId}/parts/{partId}/parameters")
@Api(value = "Parameters", produces = MediaType.APPLICATION_JSON)
public class Parameters {

    private static final Logger logger = Logger.getLogger(Parameters.class.getName());
    private static final String PARAM_ADD_TEXT = "Update the part adding all the given parameters <br/><br/>"
	    + "<strong>Important information</strong>" + "<ul> "
	    + "<li>All the <strong>metrics</strong> indicated <strong>must</strong> exists before creating the pattern.</li>"
	    + "<li>If the ID of the element is provided, that ID it will be assigned.</li>" + "</ul>";

    /**
     * Update all the params of the given part
     * 
     * @param sourceJson
     *            String in JSON format
     * @param patternId
     *            Pattern id
     * @param versionId
     *            Version id
     * @param formId
     *            Form id
     * @param partId
     *            Part id
     * @return HTTP response with code 200 if is OK
     * @throws Exception
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.TEXT_PLAIN })
    @ApiOperation(value = "Add parameters to the given fixed or extended part", notes = PARAM_ADD_TEXT, response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Parameter updated", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested parameter is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response addParameters(@ApiParam(value = "Parameters to add", required = true) ParametersUnmarshaller source,
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId,
	    @ApiParam(value = "ID of the form", required = true) @PathParam("formId") long formId,
	    @ApiParam(value = "ID of the part to update", required = true) @PathParam("partId") long partId)
	    throws Exception {
	PatternItem pi = retrievePatternItem(partId);// Obtain the pattern item
	Set<Parameter> s = source.build();
	for (Parameter aux : s) {
	    pi.addParameter(aux);// Add all the parameter in the set
	}
	PatternDataController.update(pi);// Update the pattern item
	return Response.status(Status.OK).build();

    }

    /**
     * Delete all the Parameters from a PatternItem
     * 
     * @param partId
     *            PatternItem id
     * @return HTTP response with code 200 if is successful
     * @throws Exception
     */
    @DELETE
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Delete all the paramters", notes = "Deletes all the parameters of the given fixed or extended part", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Parameters deleted", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deleteParameters(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId,
	    @ApiParam(value = "ID of the form", required = true) @PathParam("formId") long formId,
	    @ApiParam(value = "ID of the part where to delete all the parameters", required = true) @PathParam("partId") long partId)
	    throws Exception {
	logger.debug("deleting");
	PatternItem pi = retrievePatternItem(partId); // Obtain the paramneter
	pi.getParameters().clear(); // Clear the old parameters
	PatternDataController.update(pi);// Update the pattern Item
	return Response.status(Status.OK).build();
    }

    /**
     * Delete a Parameter from a PatternItem
     * 
     * @param partId
     *            PatternItem id
     * @param id
     *            Parameter ID
     * @return HTTP response with 200 if is successful
     * @throws Exception
     */
    @DELETE
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Delete a parameter", notes = "Deletes a parameter from a fixed or extended part", response = Response.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: parameter from a fixed or extended part deleted", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deleteParameter(
	    @ApiParam(value = "ID of the pattern", required = true) @PathParam("patternId") long patternId,
	    @ApiParam(value = "ID of the version", required = true) @PathParam("versionId") long versionId,
	    @ApiParam(value = "ID of the form", required = true) @PathParam("formId") long formId,
	    @ApiParam(value = "ID of the fixed or extended part", required = true) @PathParam("partId") long partId,
	    @ApiParam(value = "ID of the parameter to delete", required = true) @PathParam("id") long id)
	    throws Exception {
	PatternItem pi = retrievePatternItem(partId);// Obtain the patternItem
	Parameter p = retrieveParameter(pi, id);// Obtain the Parameter
	pi.getParameters().remove(p);// Remove the parameter from the patternItem
	PatternDataController.update(pi);// Update the patternItem
	return Response.status(Status.OK).build();
    }

    /**
     * Gets PatternItem from the DB, throws {@link NotFoundException} when the
     * PatternItem is not found
     * 
     * @param id
     *            ID of the required PatternItem
     * @return Requested {@link PatternItem}
     */
    private PatternItem retrievePatternItem(long id) {
	PatternItem p = ObjectDataController.getPatternItem(id);
	if (p == null)
	    throw new NotFoundException("PatternItem [" + id + "] not found");
	return p;
    }

    /**
     * Gets Parameter from the DB, throws {@link NotFoundException} when the
     * Parameter is not found
     * 
     * @param id
     *            ID of the required Parameter
     * @return Requested {@link Parameter}
     */
    private Parameter retrieveParameter(PatternItem pi, long id) {
	Parameter p = pi.getParameter(id);
	if (p == null)
	    throw new NotFoundException("Parameter [" + id + "] not found");
	return p;
    }

}