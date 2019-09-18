package edu.upc.gessi.rptool.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import edu.upc.gessi.rptool.exceptions.IntegrityException;
import org.apache.log4j.Logger;
import org.apache.uima.UIMAException;

import edu.upc.gessi.rptool.data.CatalogueDataController;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.rest.dtos.importexport.ExportDTO;
import edu.upc.gessi.rptool.rest.dtos.importexport.ImportDTO;
import edu.upc.gessi.rptool.rest.dtos.patternelements.RequirementPatternDTO;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.ImportUnmarshaller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/catalogue")
@Api(value = "Catalogue", produces = MediaType.APPLICATION_JSON)
public class Catalogue {

    private static final Logger logger = Logger.getLogger(Catalogue.class.getName());

    private static final String IMPORT_TEXT = "Import a complete catalogue inside the system. <br/><strong>Important information</strong>"
	    + "<ul> " + "<li>If any ID is provided it will be assigned to that item</li>"
	    + "<li>The <em>TimeMetric</em>, can have null values adding `XX` instead of any value, for example when you want to set null day possible can be: \"2018-06-XX 16:53:20\"</li>"
	    + "<li> Order of creation of the imports:<ul>"
	    + "<li> Creating a set of metric, first the simple metric should be indicated with the same id has idSimple</li>"
	    + "<li>In case that a Set is composed of another set, then the inner set should be created before that it use it</li>"
	    + "</ul></li>"
	    + "<li><strong>Metrics</strong>: If the metric to import is a SetMetric, then the simple metric should have a ID and in the simpleId that ID should be indicated.</li>"
	    + "<li><strong>Patterns</strong>: The sources, keywords and metrics are indicated by the name.</li>"
	    + "<li><strong>Patterns</strong>: The cost function is indicated directly as costFunctions inside the versions.</li>"
	    + "<li><strong>Schemas</strong>: The sources and patterns inside the classifiers are indicated by the name.</li>"
	    + "</ul>";

    /**
     * Search if a ID is being used or not.
     * 
     * @param id
     *            ID to check
     * @return Boolean telling that the ID is being used or not. True is the ID is
     *         being used, false otherwise
     */
    @GET
    @Path("ids")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Check if the given ID is already used", notes = "Given a ID, check if this ID is already in use or not", response = Boolean.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "That ID is NOT beeing used", response = Boolean.class),
	    @ApiResponse(code = 400, message = "That ID is already being used", response = Boolean.class) })
    public Response checkID(
	    @ApiParam(value = "ID to check", required = true) @DefaultValue("") @QueryParam("id") Long id) {
	logger.info("Checking ID...");
	boolean b = CatalogueDataController.checkIfExists(id);
	if (b) {
	    Response.status(Status.BAD_REQUEST).entity(b).build();
	}
	return Response.status(Status.OK).entity(b).build();
    }

    /**
     * Import all the items of the catalog
     *
     * @return HTTP response, with all the ID of the items imported
     * @throws Exception
     */
    @POST
    @Path("import")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Import catalogue", notes = IMPORT_TEXT, response = ImportDTO.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: The request has succeeded and the catalogue element has been imported, the response will contain the IDs assigned.", response = ImportDTO.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information to import, can be due to json malformed, missing mandatory field, unrecognized field, name already exists. For more information check the Body.", response = String.class),
	    @ApiResponse(code = 422, message = "Semantically Incorrect: The request has not been applied because the semantically incorrect information. check the body for more information", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response importCatalogue(
	    @ApiParam(value = "The JSON file to be imported", required = true) ImportUnmarshaller iu) throws IntegrityException, SemanticallyIncorrectException, InternalError {
	logger.info("Importing catalogue...");

	logger.debug("Deleteing database");
	CatalogueDataController.clearDatabase();// Clear the database

	try {
	    // Call the controller to import all the content of the unmarshaller
	    ImportDTO i = CatalogueDataController.importCatalogue(iu);
	    // Return created
	    return Response.status(Status.CREATED).entity(i).build();

	} catch (Exception e) {
	    logger.error(e.getMessage());

	    // When Jackson library throws a exception while deserializing a object is
	    // always JsonMapping or something related to that library, so when we make our
	    // custom exception for semantically incorrect object we have to cast it to
	    // Exception so Jersey don't use JsonMappingExceptionMapper instead of
	    // SemanticallyIncorrectExceptionMapper
	    if (e.getCause() instanceof SemanticallyIncorrectException) {
			throw (InternalError) e.getCause();
	    }
	    throw new InternalError(e.getMessage());
	}

    }

    /**
     * Export all the catalog
     * 
     * @return HTTP response, with all the items in Catalog
     * @throws SemanticallyIncorrectException
     */
    @GET
    @Path("export")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Export catalogue", notes = "Export catalogue with all the Sources, Keywords, Metrics, RequirementsPattern and Schemas, in JSON format so can be imported again<ul>"
	    + "<li><em>TimeMetric</em> can have null values inside the date, that are marked as 'X'</li>"
	    + "<ul>", response = ExportDTO.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: The export JSON has been created and returned.", response = ExportDTO.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response exportCatalogue() throws SemanticallyIncorrectException {
	logger.info("Exporting...");
	// Obtain the export DTO from the controller
	ExportDTO export = CatalogueDataController.exportCatalogue();
	// Create return message with OK status
	return Response.status(Status.OK).entity(export).build();
    }

    /**
     * Given a list of words, separated by spaces, returns the reduced pattern
     * information. the system search in the following elements:
     * <ul>
     * <li><strong>Pattern</strong>: Search inside the <em>name</em> or
     * <em>description</em></li>
     * <li><strong>Keywords</strong>: Search inside the last <em>version</em>
     * keywords</li>
     * <li><strong>Forms</strong>: Search inside the <em>name</em> of all the forms
     * inside the last version</li>
     * <ul>
     * 
     * @return HTTP response, with all the items in Catalog
     * @throws UIMAException
     */
    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Search in catalogue", notes = "Given a list of words, separated by spaces, returns the reduced pattern information. the system search in the following elements: "
	    + "<ul>" + "<li><strong>Pattern</strong>:  Search inside the <em>name</em> or <em>description</em></li>"
	    + "<li><strong>Keywords</strong>:  Search inside the  last <em>version</em> keywords</li>"
	    + "<li><strong>Forms</strong>:  Search inside the <em>name</em> of all the forms inside the last version</li>"
	    + "<ul>", response = ExportDTO.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: Search completed, returned pattern accomplishing search criteria", response = RequirementPatternDTO.class, responseContainer = "Set"),
	    @ApiResponse(code = 404, message = "Not Found: No pattern contains that words.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response searchInCatalogue(
	    @ApiParam(value = "Words to search, all the words should be separated by space or tabs", required = true) @DefaultValue("") @QueryParam("words") String words)
	    throws UIMAException {
	logger.info("Searching...");

	Collection<RequirementPattern> list = CatalogueDataController.searchPatternWithWords(words);

	// Generate DTO
	List<RequirementPatternDTO> ret = new ArrayList<>();
	for (RequirementPattern rp : list) {
	    RequirementPatternDTO rpdto = new RequirementPatternDTO(rp);
	    rpdto.reduceFields();
	    ret.add(rpdto);
	}
	if (ret.isEmpty()) {
	    throw new NotFoundException("No pattern contains that words");
	}
	// Create return message with OK status
	return Response.status(Status.OK).entity(ret).build();
    }

}
