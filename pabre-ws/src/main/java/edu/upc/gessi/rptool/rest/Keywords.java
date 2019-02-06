package edu.upc.gessi.rptool.rest;

import java.io.IOException;
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

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.data.ObjectDataController;
import edu.upc.gessi.rptool.domain.patternelements.Keyword;
import edu.upc.gessi.rptool.rest.dtos.KeywordDTO;
import edu.upc.gessi.rptool.rest.unmarshallers.keywords.KeywordUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.keywords.KeywordUpdater;
import edu.upc.gessi.rptool.rest.utilities.IdFormatter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/keywords")
@Api(value = "Keywords", produces = MediaType.APPLICATION_JSON)
public class Keywords {

    private static final Logger logger = Logger.getLogger(Keywords.class.getName());

    /**
     * Obtain all the keywords.
     * 
     * @return List of keyword
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "List all the keywords", notes = "Returns all the keywords in the catalogue", response = KeywordDTO.class, responseContainer = "List")
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: The request has succeeded.", response = KeywordDTO.class, responseContainer = "List"),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public List<KeywordDTO> getKeywords() {
	logger.info("Retriving all the keywords");
	List<KeywordDTO> keywordsDTOs = new LinkedList<>();
	// Obtain all the keywords from controller
	List<Keyword> keywords = ObjectDataController.listKeywords();
	for (Keyword k : keywords) {
	    keywordsDTOs.add(new KeywordDTO(k));// Generate DTOs to return
	}
	return keywordsDTOs;
    }

    /**
     * Get keyword by given ID
     * 
     * @param id
     *            ID to search
     * @return Searched Keyword
     */
    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.TEXT_PLAIN })
    @ApiOperation(value = "Retrieve a keyword", notes = "Get a keyword by ID", response = KeywordDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Keyword obtained", response = KeywordDTO.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested keyword is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public KeywordDTO getKeyword(
	    @ApiParam(value = "ID of the keyword to obtain", required = true) @PathParam("id") long id) {
	logger.info("Retriving keyword with ID: " + id);
	Keyword k = retrieveKeyword(id);
	KeywordDTO kDTO = new KeywordDTO(k);
	return kDTO;
    }

    /**
     * Given a id, updates all the given fields
     * 
     * @param sourceJson
     *            keyword unmarshaller
     * @param id
     *            ID of the keyword to update
     * @return HTTP message
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * @throws Exception
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Update a keyword", notes = "Update the given fields of the keyword", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Keyword updated", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested keyword is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updateKeyword(
	    @ApiParam(value = "new fields for keyword", required = true) KeywordUnmarshaller sourceJson,
	    @ApiParam(value = "ID of the keyword to obtain", required = true) @PathParam("id") long id)
	    throws JsonParseException, JsonMappingException, IOException {
	logger.info("Updating keyword with ID: " + id);
	Keyword k = retrieveKeyword(id);// Obtain the keyword
	new KeywordUpdater(k, sourceJson).update();// Create a new instance of the updater and call to update
	return Response.status(Status.OK).build();
    }

    /**
     * Create a new Keyword
     * 
     * @param sourceJson
     *            String in JSON format
     * @return HTTP message with id of the created keyword
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a keyword", notes = "Creates a new keyowrd", response = IdFormatter.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Keyword created", response = IdFormatter.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public IdFormatter createKeyword(
	    @ApiParam(value = "Keyword to be created", required = true) KeywordUnmarshaller sourceJson)
	    throws JsonParseException, JsonMappingException, IOException {
	logger.info("Creating Keyword...");
	Keyword s = sourceJson.build();
	GenericDataController.save(s);
	return new IdFormatter(s.getId());
    }

    /**
     * Delete a keyword
     * 
     * @param id
     *            ID of the keyword to delete
     * @return HTTP response
     */
    @DELETE
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Delete a keyword", notes = "Deletes a keyowrd", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Keyword deleted", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deleteKeyword(
	    @ApiParam(value = "ID of the keyword to delete", required = true) @PathParam("id") long id) {
	logger.info("Deleting keyword with ID: " + id);
	Keyword k = retrieveKeyword(id);
	GenericDataController.delete(k);
	return Response.status(Status.OK).build();

    }

    /**
     * Gets Keyword from the DB, throws {@link NotFoundException} when the Keyword
     * is not found
     * 
     * @param id
     *            ID of the required Keyword
     * @return Requested {@link Keyword}
     */
    private Keyword retrieveKeyword(long id) {
	Keyword m = ObjectDataController.getKeyword(id);
	if (m == null)
	    throw new NotFoundException("Keyword [" + id + "] not found");
	return m;

    }

}