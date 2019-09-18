package edu.upc.gessi.rptool.rest;

import java.util.LinkedList;
import java.util.List;

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

import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.data.ObjectDataController;
import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.rest.dtos.SourceDTO;
import edu.upc.gessi.rptool.rest.unmarshallers.sources.PutSourceUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.sources.SourceUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.sources.SourceUpdater;
import edu.upc.gessi.rptool.rest.utilities.IdFormatter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/sources")
@Api(value = "Sources", produces = MediaType.APPLICATION_JSON)
public class Sources {

    private static final Logger logger = Logger.getLogger(Sources.class.getName());

    /**
     * Get all the Sources in the catalog
     * 
     * @return List of all the sources
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all the sources", notes = "Get all the sources in the catalogue.", response = SourceDTO.class, responseContainer = "List")
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: sources obtained", response = SourceDTO.class, responseContainer = "List"),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public List<SourceDTO> getSources() {
	logger.debug("Retreving all the sources...");
	List<Source> sources = ObjectDataController.listSources();
	List<SourceDTO> sourcesDTOs = new LinkedList<>();
	for (Source s : sources) {
	    sourcesDTOs.add(new SourceDTO(s, true));
	}
	return sourcesDTOs;
    }

    /**
     * Get a source with id
     * 
     * @param id
     *            Source ID
     * @return Source requested
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieve a source", notes = "Get a source by ID", response = SourceDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: source obtained", response = SourceDTO.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested source is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public SourceDTO getSource(
	    @ApiParam(value = "ID of the source to be obtained", required = true) @PathParam("id") long id) {
	logger.debug("Retreving source with ID: " + id);
	Source source = retrieveSource(id);
		return new SourceDTO(source);
    }

    /**
     * Update a source
     * 
     * @param sourceUnmarshaller
     *            Unmarshaller with fields to be updated
     * @param id
     *            ID of the source to be updated
     * @return HTTP response
     * @throws Exception
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update a source", notes = "Update the given fields of the source, if the identifier is null it will be ignored.", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Source updated", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested source is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updateSource(
	    @ApiParam(value = "Unmarshaller with fields to update", required = true) PutSourceUnmarshaller sourceUnmarshaller,
	    @ApiParam(value = "ID of the source to be updated", required = true) @PathParam("id") long id)
	    throws SemanticallyIncorrectException {
	logger.debug("Updating source with ID: " + id);
	Source s = retrieveSource(id);
	new SourceUpdater(s, sourceUnmarshaller).update();
	return Response.status(Status.OK).build();
    }

    /**
     * Create a source
     * 
     * @param sourcesUnmarshaller
     *            source fields to be created
     * @return ID of the created source
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a Source", notes = "Creates a new source", response = IdFormatter.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Source created", response = IdFormatter.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public IdFormatter createSource(
	    @ApiParam(value = "Unmarshaller with fields to created the source", required = true) SourceUnmarshaller sourcesUnmarshaller)
	    throws SemanticallyIncorrectException {
	logger.debug("Creating source...");
	Source s = sourcesUnmarshaller.build();
	GenericDataController.save(s);
	return new IdFormatter(s.getId());
    }

    /**
     * Delete a source
     * 
     * @param id
     *            Source ID
     * @return HTTP response
     * @throws Exception
     */
    @DELETE
    @Path("{id}")
    @ApiOperation(value = "Delete a Source", notes = "Deletes a source", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Source deleted", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested source is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deleteMetric(
	    @ApiParam(value = "ID of the source to be deleted", required = true) @PathParam("id") long id)
	    throws Exception {
	logger.debug("Delete source with ID: " + id);
	Source s = retrieveSource(id);
	ObjectDataController.delete(s);
	return Response.status(Status.OK).build();

    }

    /**
     * Gets Source from the DB, throws {@link NotFoundException} when the source is
     * not found
     * 
     * @param id
     *            ID of the required Source
     * @return Requested {@link Source}
     */
    private Source retrieveSource(long id) {
	Source m = ObjectDataController.getSource(id);
	if (m == null)
	    throw new NotFoundException("Source [" + id + "] not found");
	return m;

    }
}
