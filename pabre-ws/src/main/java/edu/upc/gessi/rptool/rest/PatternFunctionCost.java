package edu.upc.gessi.rptool.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
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
import edu.upc.gessi.rptool.domain.CostFunction;
import edu.upc.gessi.rptool.domain.ExternalObject;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.rest.dtos.CostFunctionDTO;
import edu.upc.gessi.rptool.rest.unmarshallers.externalobjects.CostFunctionUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.externalobjects.CostFunctionsUnmarshaller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/patterns/{patternID}/versions/{versionID}/cost_function")
@Api(value = "Cost function", produces = MediaType.APPLICATION_JSON)
public class PatternFunctionCost {

    private static final Logger logger = Logger.getLogger(PatternFunctionCost.class.getName());

    /**
     * Get all the cost functions related to a pattern and version
     * 
     * @param id
     *            Pattern id
     * @param versionId
     *            Version id
     * @return List of cost functions related to given version id
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "List all the Function cost of the given version", notes = "Returns all the function cost of the given pattern version", response = CostFunctionDTO.class, responseContainer = "List")
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: The request has succeeded.", response = CostFunctionDTO.class, responseContainer = "List"),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public List<CostFunctionDTO> getAllFunctionCostFunctions(
	    @ApiParam(value = "Pattern ID", required = true) @PathParam("patternID") long id,
	    @ApiParam(value = "Version ID", required = true) @PathParam("versionID") long versionId) {
	logger.debug("Obtaining all the cost functions");
	RequirementPatternVersion rpv = retrieveRequirementPatternVersion(versionId);// Obtain the pattern version
	List<CostFunctionDTO> l = new ArrayList<>();
	for (ExternalObject eo : rpv.getExternalObjects()) {// for each external object
	    if (eo instanceof CostFunction) {// check if is a cost function
		CostFunction cf = (CostFunction) eo;// cast the external object to cost function
		l.add(new CostFunctionDTO(cf));// add the dto to the list
	    }
	}
	return l;
    }

    /**
     * Get a cost function given a id
     * 
     * @param patternID
     *            pattern id
     * @param versionId
     *            version id
     * @param id
     *            cost function id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Retrieve a cost function", notes = "Get a cost function by ID", response = CostFunctionDTO.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: function obtained", response = CostFunctionDTO.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested function is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public CostFunctionDTO getCostFunction(
	    @ApiParam(value = "Pattern ID", required = true) @PathParam("patternID") long patternID,
	    @ApiParam(value = "Version ID", required = true) @PathParam("versionID") long versionId,
	    @ApiParam(value = "Function ID", required = true) @PathParam("id") long id) {
	CostFunction cf = retrieveCostFunction(id); // obtain the cost function with the given id
	CostFunctionDTO cfdto = new CostFunctionDTO(cf); // create a dto with the cost function
	return cfdto;
    }

    /**
     * Adds a new costFunction
     * 
     * @param functionsUnmarshaller
     *            JSON to be proceed
     * @param id
     *            ID of the pattern where add the cost function
     * @return HTTP response
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create cost functions to given version", notes = "Given a set of cost functions, add them in the given parttern version", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: functions created", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of somehing semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response addCostFunction(
	    @ApiParam(value = "Cost functions to be created", required = true) CostFunctionsUnmarshaller functionsUnmarshaller,
	    @ApiParam(value = "Pattern ID", required = true) @PathParam("patternID") long id,
	    @ApiParam(value = "Version ID", required = true) @PathParam("versionID") long versionId) throws Exception {
	RequirementPatternVersion rpv = retrieveRequirementPatternVersion(versionId); // obtain the pattern version
	Set<CostFunction> scf;
	scf = functionsUnmarshaller.build();// build the cost function set

	ObjectDataController.addCostFunctions(rpv, scf);// Call the controller to add and persiste the creation
	return Response.status(Status.OK).build();
    }

    /**
     * Substitute the cost functions of given pattern
     * 
     * @param function
     *            functions to be substituted
     * @param id
     *            ID of the pattern where add the cost function
     * @return HTTP response
     * @throws Exception
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Substitute the cost functions of given pattern", notes = "Substitute the cost functions of given pattern", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: functions substituted", response = Response.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of somehing semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updateCostFunctions(
	    @ApiParam(value = "Functions to be substituted", required = true) CostFunctionsUnmarshaller function,
	    @ApiParam(value = "Pattern ID", required = true) @PathParam("patternID") long id,
	    @ApiParam(value = "Version ID", required = true) @PathParam("versionID") long versionId) throws Exception {
	RequirementPatternVersion rpv = retrieveRequirementPatternVersion(versionId); // obtain the pattern version
	Set<CostFunction> scf = function.build(); // build the unmarshaller
	ObjectDataController.updateAllCostFunctions(rpv, scf);// call the controller to update all the cost function
	return Response.status(Status.OK).build();

    }

    /**
     * Update the cost function of given pattern and existing function
     * 
     * @param functions
     *            function to update
     * @param patternID
     *            ID of the pattern where add the cost function
     * @param idFunction
     *            ID of the existing cost function
     * @return HTTP response
     * @throws Exception
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update a given cost function", notes = "Update the given fields of the cost function", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: cost functions updated", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested cost function is not found.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of somehing semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updateCostFunction(
	    @ApiParam(value = "new fields to update", required = true) CostFunctionUnmarshaller functions,
	    @ApiParam(value = "Pattern ID", required = true) @PathParam("patternID") long patternID,
	    @ApiParam(value = "Version ID", required = true) @PathParam("versionID") long versionId,
	    @ApiParam(value = "Cost function ID", required = true) @PathParam("id") long idFunction) throws Exception {
	CostFunction cfOld = retrieveCostFunction(idFunction); // obtain the cost function to update
	CostFunction cfNew = functions.build();
	ObjectDataController.updateCostFunction(cfOld, cfNew); // call the controller to update that cost function
	return Response.status(Status.OK).build();
    }

    /**
     * Deletes a pattern cost function
     * 
     * @param patternID
     *            ID of the pattern where add the cost function
     * @return HTTP response
     * @throws Exception
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete all the cost functions", notes = "Deletes all the functions", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Functions deleted", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested Version is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deleteAllCostFunctions(
	    @ApiParam(value = "Pattern ID", required = true) @PathParam("patternID") long patternID,
	    @ApiParam(value = "Version ID", required = true) @PathParam("versionID") long versionId) throws Exception {
	RequirementPatternVersion rpv = retrieveRequirementPatternVersion(versionId); // Obtain the pattern version
	ObjectDataController.removeAllCostFunctions(rpv); // call the controller to remove all the cost function
	return Response.status(Status.OK).build();
    }

    /**
     * Deletes a pattern cost function
     * 
     * @param patternID
     *            ID of the pattern where add the cost function
     * @param idFunction
     *            ID of the existing cost function
     * @return HTTP response
     * @throws Exception
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete a cost functions", notes = "Deletes the cost function given by the ID", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Function deleted", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested Version or functions not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deleteCostFunction(
	    @ApiParam(value = "Pattern ID", required = true) @PathParam("patternID") long patternID,
	    @ApiParam(value = "Version ID", required = true) @PathParam("versionID") long versionId,
	    @ApiParam(value = "Functions ID", required = true) @PathParam("id") long idFunction) throws Exception {
	RequirementPatternVersion rpv = retrieveRequirementPatternVersion(versionId); // Obtain the pattern version
	CostFunction cf = retrieveCostFunction(idFunction); // Obtain the cost function to delete
	rpv.getExternalObjects().remove(cf); // remove the cost function from the external objects
	ObjectDataController.delete(cf); // Call the controller to delete the cost function
	return Response.status(Status.OK).build();
    }

    /**
     * Gets RequirementPattern from the DB, throws {@link NotFoundException} when
     * the RequirementPattern is not found
     * 
     * @param id
     *            ID of the required RequirementPattern
     * @return Requested {@link RequirementPattern}
     */
    private RequirementPatternVersion retrieveRequirementPatternVersion(long patternId) {
	RequirementPatternVersion m = PatternDataController.getPatternVersion(patternId);
	if (m == null)
	    throw new NotFoundException("RequirementPattern [" + patternId + "] not found");
	return m;
    }

    private CostFunction retrieveCostFunction(long id) {
	CostFunction m = ObjectDataController.getCostFunction(id);
	if (m == null)
	    throw new NotFoundException("CostFunction [" + id + "] not found");
	return m;

    }
}