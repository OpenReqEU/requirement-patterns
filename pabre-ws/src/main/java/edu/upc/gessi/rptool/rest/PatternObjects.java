package edu.upc.gessi.rptool.rest;

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
import edu.upc.gessi.rptool.domain.PatternObjectDependency;
import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.domain.patternelements.PatternObject;
import edu.upc.gessi.rptool.rest.dtos.PatternObjectDTO;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.dependencies.PatternObjectDependenciesUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.dependencies.PatternObjectDependencyUpdater;
import edu.upc.gessi.rptool.rest.unmarshallers.dependencies.PutPatternObjectDependencyUnmarshaller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/pattern_elements")
@Api(value = "Dependencies", produces = MediaType.APPLICATION_JSON)
public class PatternObjects {

    private static final Logger logger = Logger.getLogger(PatternObjects.class.getName());

    /**
     * Get all the dependencies of the given pattern object ID
     * 
     * @param id
     *            ID of the pattern object which dependencies will be returned
     * @return list of dependencies of that pattern object
     */
    @GET
    @Path("{id}/dependencies")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get all the dependencies", notes = "Get all the dependencies of the given element. DependencyClass can be:<ul>"
	    + "<li>RequirementForm</li>" + "<li>RequirementPattern</li>" + "<li>RequirementPatternVersion</li>"
	    + "<li>FixedPart</li>" + "<li>ExtendedPart</li>" + "<li>Parameter</li>"
	    + "</ul>", response = PatternObjectDTO.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: dependencies obtained", response = PatternObjectDTO.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested element is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public PatternObjectDTO getDependencies(@ApiParam(value = "Object ID", required = true) @PathParam("id") long id) {
	PatternObject po = retrievePatternObject(id); // obtain the pattern object
		return new PatternObjectDTO(po);

    }

    /**
     * Replace all the dependencies to the pattern object, substitute the new
     * dependencies with the old one
     * 
     * @param dependencies
     *            Dependencies to add/replace
     * @param id
     *            pattern object id
     * @return HTTP response
     * @throws Exception
     */
    @POST
    @Path("{id}/dependencies")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Replace dependencies of the given PatternObject", notes = "Replace all the dependencies to the pattern object, substitute the new dependencies with the old one."
	    + "<br/><br/><strong>Important</strong> Dependency type only can be: " + "<li>IMPLIES</li>"
	    + "<li>EXCLUDES</li>" + "<li>CONTRIBUTES</li>" + "<li>DAMAGES</li>" + "<ul></ul>"
	    + "<br/><strong>Important</strong> Dependency direction only can be: " + "<li>UNIDIRECTIONAL</li>"
	    + "<li>BIDIRECTIONAL</li>" + "<ul></ul>", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: dependecies created", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response setNewDependencies(
	    @ApiParam(value = "Dependecies to add", required = true) PatternObjectDependenciesUnmarshaller dependencies,
	    @ApiParam(value = "PatternObject ID", required = true) @PathParam("id") long id) throws SemanticallyIncorrectException {
	logger.debug("Creating dependency");
	PatternObject po = retrievePatternObject(id);// obtain the pattern object
	Set<PatternObjectDependency> pod;
	pod = dependencies.build();
	ObjectDataController.deleteAllPatternObjectDependency(po);
	ObjectDataController.saveNewPatternObjectDependency(po, pod);// call the controller to save the dependencies
	return Response.status(Status.OK).build();
    }

    /**
     * Add all the dependencies to the given pattern object
     * 
     * @param depends
     *            Dependencies to add
     * @param id
     *            ID of the pattern object to update
     * @return HTTP response
     * @throws SemanticallyIncorrectException
     * @throws Exception
     */
    @PUT
    @Path("{id}/dependencies")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add all the dependecies to the pattern object", notes = "Add all the dependencies to the given pattern object"
	    + "<br/><br/><strong>Important</strong> Dependency type only can be: " + "<li>IMPLIES</li>"
	    + "<li>EXCLUDES</li>" + "<li>CONTRIBUTES</li>" + "<li>DAMAGES</li>" + "<ul></ul>"
	    + "<br/><strong>Important</strong> Dependency direction only can be: " + "<li>UNIDIRECTIONAL</li>"
	    + "<li>BIDIRECTIONAL</li>" + "<ul></ul>", response = Response.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: dependencies substituted", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested element is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response addDependencies(
	    @ApiParam(value = "Dependencies to add", required = true) PatternObjectDependenciesUnmarshaller depends,
	    @ApiParam(value = "Pattern Element ID", required = true) @PathParam("id") long id)
	    throws SemanticallyIncorrectException {

	PatternObject po = retrievePatternObject(id);// obtain the pattern object
	Set<PatternObjectDependency> pod;
	// Deserialize and build the unmarshaller
	pod = depends.build();
	ObjectDataController.addNewPatternObjectDependency(po, pod); // call the controller to add all the
								     // dependencies
	return Response.status(Status.OK).build();

    }

    /**
     * Delete all the dependencies of the given pattern object
     * 
     * @param id
     *            Pattern object id
     * @return HTTP response
     * @throws Exception
     */
    @DELETE
    @Path("{id}/dependencies")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete all the delependencies of the given component", notes = "Delete all the delependencies of the given component", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: dependencies deleted", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested element is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deleteDependencies(@ApiParam(value = "Element ID", required = true) @PathParam("id") long id) {
	PatternObject po = retrievePatternObject(id);// obtain the pattern object
	ObjectDataController.deleteAllPatternObjectDependency(po); // Call the controllers to delete all the pattern
								   // object
	return Response.status(Status.OK).build();
    }

    /**
     * Update a dependencies between two pattern objects
     * 
     * @param unmarshaller
     *            Update unmarshaller
     * @param id
     *            ID of the pattern object
     * @param idDependency
     *            Dependency id
     * @return HTTP response
     * @throws Exception
     */
    @PUT
    @Path("{id}/dependencies/{idDependency}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update a dependency", notes = "Update fields of given dependency"
	    + "<br/><br/><strong>Important</strong> Dependency type only can be: " + "<li>IMPLIES</li>"
	    + "<li>EXCLUDES</li>" + "<li>CONTRIBUTES</li>" + "<li>DAMAGES</li>" + "<ul></ul>"
	    + "<br/><strong>Important</strong> Dependency direction only can be: " + "<li>UNIDIRECTIONAL</li>"
	    + "<li>BIDIRECTIONAL</li>" + "<ul></ul>", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: dependency updated", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested element is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updateDependency(
	    @ApiParam(value = "Unmarshaller with the given fields", required = true) PutPatternObjectDependencyUnmarshaller unmarshaller,
	    @ApiParam(value = "Element ID", required = true) @PathParam("id") long id,
	    @ApiParam(value = "Dependency ID", required = true) @PathParam("idDependency") long idDependency)
	    throws SemanticallyIncorrectException{
	PatternObject po = retrievePatternObject(id); // obtain the pattern object
	PatternObjectDependency pod = po.getDependency(idDependency); // Obtain the dependency
	if (pod == null)
	    throw new NotFoundException("Dependency [" + idDependency + "] not found");

	// Create the updater
	PatternObjectDependencyUpdater updater = new PatternObjectDependencyUpdater(pod, unmarshaller);
	updater.update(); // Update the fields

	return Response.status(Status.OK).build();
    }

    /**
     * Delete a given ID dependency
     * 
     * @param id
     *            Pattern object id
     * @param idDependency
     *            Dependency id
     * @return HTTP response
     * @throws Exception
     */
    @DELETE
    @Path("{id}/dependencies/{idDependency}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete a dependency", notes = "Delete a dependency", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: dependency deleted", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested element is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deleteDependency(@ApiParam(value = "Element ID", required = true) @PathParam("id") long id,
	    @ApiParam(value = "Dependency ID", required = true) @PathParam("idDependency") long idDependency)
	    throws Exception {
	ObjectDataController.removeDependency(id, idDependency);// call the controller to delete the dependency
	return Response.status(Status.OK).build();
    }

    /**
     * Gets PatternObject from the DB, throws {@link NotFoundException} when the
     * PatternObject is not found
     * 
     * @param id
     *            ID of the required PatternObject
     * @return Requested {@link Parameter}
     */
    private PatternObject retrievePatternObject(long id) {
	PatternObject po = ObjectDataController.getPatternObject(id);
	if (po == null)
	    throw new NotFoundException("PatternObject [" + id + "] not found");
	return po;
    }
}