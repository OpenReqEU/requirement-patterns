package edu.upc.gessi.rptool.rest;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.data.PatternDataController;
import edu.upc.gessi.rptool.data.SchemaDataController;
import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.RedundancyException;
import edu.upc.gessi.rptool.rest.dtos.SchemaDTO;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementPatternDTO;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.classifications.SchemaUnmarshaller;
import edu.upc.gessi.rptool.rest.utilities.IdFormatter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/schemas")
@Api(value = "Schemas", produces = MediaType.APPLICATION_JSON)
public class Schemas {

    private static final Logger logger = Logger.getLogger(Schemas.class.getName());
    private static final String IMPORTANT_INFORMATION_TEXT = "<strong>Important information</strong>" + "<ul> "
	    + "<li>All the <strong>keywords</strong> indicated <strong>must</strong> exists before creating the schema.</li>"
	    + "<li>All the <strong>sources</strong> indicated <strong>must</strong> exists before creating the schema.</li>"
	    + "<li>All the <strong>metrics</strong> indicated <strong>must</strong> exists before creating the schema.</li>"
	    + "<li>All the <strong>patterns</strong> indicated <strong>must</strong> exists before creating the schema.</li>"
	    + "</ul>";

    private static final String SCHEMA_CREATION_TEXT = "Create a new Schema with all the classifiers. <br/>"
	    + IMPORTANT_INFORMATION_TEXT;

    private static final String SCHEMA_UPDATE_TEXT = "Update the given fields of the schema, all the componentes inside the schema should have a ID assigned, if the ID is already used throw a code 400.";

    /**
     * Get all the schemas
     * 
     * @param namesList
     *            List of all the names of the schemas to show, if is empty no
     *            filter will be applied
     * @return list of schemas
     * @throws SemanticallyIncorrectException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all the Schemas", notes = "Get all the schemas from the catalogue. <br/>"
	    + Classifiers.CLASSIFIERS_TYPE, response = SchemaDTO.class, responseContainer = "Set")
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: schemas obtained", response = SchemaDTO.class, responseContainer = "Set"),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Set<SchemaDTO> getSchemas(
	    @ApiParam(value = "List of all the names of schema to show, if is empty no filter will be applied", required = false) @QueryParam("names") List<String> namesList)
	    throws SemanticallyIncorrectException {
	boolean shouldFilter = namesList != null && !namesList.isEmpty(); // check if the result need to be filtered
	List<ClassificationSchema> listSchemas = SchemaDataController.listSchemas(); // get all the schemas
	Set<SchemaDTO> listSchemasDTOs = new LinkedHashSet<>();
	for (ClassificationSchema sch : listSchemas) { // for each schema obtained from the DB
	    // check if it should be filtered, in hat case check if the name is inside the
	    // given list, otherwise just create the DTOs and add it to the list to return
	    if ((shouldFilter && namesList.contains(sch.getName())) || !shouldFilter) {
		SchemaDTO schemaDTO = new SchemaDTO(sch);
		listSchemasDTOs.add(schemaDTO);
	    }
	}
	return listSchemasDTOs;
    }

    /**
     * Create a Schema
     * 
     * @param creationgUnmarshaller
     *            Unmarshaller with schema to create
     * 
     * @return ID of the created schema
     * @throws HibernateException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws IntegrityException
     * @throws RedundancyException
     * @throws SemanticallyIncorrectException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a Schema", notes = SCHEMA_CREATION_TEXT, response = IdFormatter.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Schema created", response = IdFormatter.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of something semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public IdFormatter createSchema(
	    @ApiParam(value = "Unmarshaller with schema to create", required = true) SchemaUnmarshaller creationgUnmarshaller)
	    throws HibernateException, IntegrityException,
	    RedundancyException, SemanticallyIncorrectException {
	logger.debug("Creating Schema");
	ClassificationSchema cs = creationgUnmarshaller.build();
	logger.debug("Unmarshaller builded");
	SchemaDataController.saveSchema(cs);
	return new IdFormatter(cs.getId());
    }

    /**
     * Get a schema
     * 
     * @param id
     *            ID of the schema to obtain
     * @param unbinded
     *            When is true show all the pattern who are not contained inside any
     *            schema
     * @param complete
     *            When is true returns complete information of the schema otherwise
     *            just return the root classifiers
     * @return Requested schema
     * @throws SemanticallyIncorrectException
     */
    @GET
    @Path("{schemaid}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Retrieve a Schema", notes = "Get a schema by ID", response = SchemaDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Schema obtained", response = SchemaDTO.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested schema is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public SchemaDTO getSchema(
	    @ApiParam(value = "ID of the schema to obtain", required = true) @PathParam("schemaid") long id,
	    @ApiParam(value = "When is true show all the pattern who are not contained inside any schema", required = false, defaultValue = "false") @DefaultValue("false") @QueryParam("unbinded") boolean unbinded,
	    @ApiParam(value = "When is true returns complete information of the schema otherwise just return the root classifiers", required = false, defaultValue = "true") @DefaultValue("true") @QueryParam("complete") boolean complete)
	    throws SemanticallyIncorrectException {
	logger.debug("Obtaining schema...");
	ClassificationSchema sch = retrieveClassificationSchema(id); // Get the schema from DB
	logger.debug("Schemas, Root classifiers size in GET: " + sch.getRootClassifiers().size());
	SchemaDTO schemaDTO = new SchemaDTO(sch); // create the DTO

	// if the user dosen't want the complete schema
	if (!complete)
	    schemaDTO.setRootClassifiers(sch.getRootClassifiers());

	// if the user want the unbinded patterns
	if (unbinded) {
	    Set<RequirementPatternDTO> unbindedPatterns = new HashSet<>();
	    PatternDataController.getUnbindedPatternsDTO(unbindedPatterns);
	    schemaDTO.setUnbindedPatterns(unbindedPatterns);
	}
	return schemaDTO;
    }

    /**
     * Update schema
     * 
     * @param unmarshaller
     *            Unmarshaller with the fields to update
     * @param schemaid
     *            Schema ID to update
     * @return HTTP response
     * @throws Exception
     */
    @PUT
    @Path("{schemaid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update a Schema", notes = SCHEMA_UPDATE_TEXT, response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Schema updated", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested schema is not found.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of somehing semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updateSchemaFields(
	    @ApiParam(value = "Unmarshaller with the fields to update", required = true) SchemaUnmarshaller unmarshaller,
	    @ApiParam(value = "Schema ID to update", required = true) @PathParam("schemaid") long schemaid)
	    throws IntegrityException, SemanticallyIncorrectException, NotFoundException, RedundancyException {
	ClassificationSchema parent = retrieveClassificationSchema(schemaid);// get the schema
	// Delete the old version of the schema
	SchemaDataController.delete(parent);
	GenericDataController.flush();
	// check if all the inner unmarshallers contains ID
	boolean b = unmarshaller.checkAllItemsContainsID();
	if (!b) {
	    throw new NotFoundException("Missing ID in one of the fields");
	}

	// build the new unmarshaller
	ClassificationSchema aux = unmarshaller.build();
	// save the new schema
	SchemaDataController.saveSchema(aux);

	return Response.status(Status.OK).build();
    }

    /**
     * Delete a Schema
     * 
     * @param schemaid
     *            Schema ID to be deleted
     * @return HTTP response
     * @throws HibernateException
     * @throws IntegrityException
     * @throws SemanticallyIncorrectException
     */
    @DELETE
    @Path("{schemaid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete a Schema", notes = "Deletes a schema", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Schema deleted", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deleteSchema(
	    @ApiParam(value = "Schema ID to be deleted", required = true) @PathParam("schemaid") long schemaid)
	    throws HibernateException, IntegrityException, SemanticallyIncorrectException {
	ClassificationSchema parent = retrieveClassificationSchema(schemaid);// get the schema
	SchemaDataController.delete(parent);// call the controller to delete the schema
	return Response.status(Status.OK).build();
    }

    // CLASSFIERS

    /**
     * Gets ClassificationSchema from the DB, throws {@link NotFoundException} when
     * the ClassificationSchema is not found
     * 
     * @param id
     *            ID of the required ClassificationSchema
     * @return Requested {@link ClassificationSchema}
     */
    public static ClassificationSchema retrieveClassificationSchema(long schemaid) {
	ClassificationSchema sch = SchemaDataController.getSchema(schemaid);
	if (sch == null)
	    throw new NotFoundException("Schema [" + schemaid + "] not found");
	return sch;

    }

}
