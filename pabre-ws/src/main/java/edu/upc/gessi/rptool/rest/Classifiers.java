package edu.upc.gessi.rptool.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
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

import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.RedundancyException;
import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.data.SchemaDataController;
import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.dtos.ClassifierDTO;
import edu.upc.gessi.rptool.rest.dtos.SchemaDTO;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.classifications.ClassifierUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.classifications.SchemaSubstitutionUmarshaller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/schemas/{schemaid}/classifiers")
@Api(value = "Schemas", produces = MediaType.APPLICATION_JSON)
public class Classifiers {

    private static final Logger logger = Logger.getLogger(Classifiers.class.getName());
    private static final String IMPORTANT_INFORMATION_TEXT = "<strong>Important information</strong>" + "<ul> "
	    + "<li>All the <strong>keywords</strong> indicated <strong>must</strong> exists before creating the schema.</li>"
	    + "<li>All the <strong>sources</strong> indicated <strong>must</strong> exists before creating the schema.</li>"
	    + "<li>All the <strong>metrics</strong> indicated <strong>must</strong> exists before creating the schema.</li>"
	    + "<li>All the <strong>patterns</strong> indicated <strong>must</strong> exists before creating the schema.</li>"
	    + "</ul>";
    protected static final String CLASSIFIERS_TYPE = " The field type of a classifier could be one of this:<ul> "
	    + "<li><strong>ROOT</strong>: <ul><li>Type: 0</li><li>This type of classifier is the only one allowed to be in the root position.</li> <li>ROOT can't have requirementPatterns</li></ul></li>"
	    + "<li><strong>BASIC</strong>: <ul><li>Type: 1</li><li>Not allowed to have internalClassifiers</li><li>Mandatory to have requirementPatterns</li></ul></li>"
	    + "<li><strong>DESCOMPOSED</strong>: <ul><li>Type: 2</li><li>Mandatory to have internalClassifiers</li><li>Not allowed to have requirementPatterns</li></ul></li>"
	    + "<li><strong>GENERAL</strong>: <ul><li>Type: 3</li><li>Not allowed to have internalClassifiers</li><li>Not allowed to have requirementPatterns</li></ul></li>"
	    + "</ul>";
    private static final String CLASSIFIER_SUBS_TEXT = "Substitute all the classifiers of the given schema.<br/>"
	    + IMPORTANT_INFORMATION_TEXT;

    private static final String CLASSIFIER_UPDATE_TEXT = "Update the given fields of the classifier.<br/>"
	    + IMPORTANT_INFORMATION_TEXT;

    // CLASSFIERS

    /**
     * Get all the classifier of the given schema
     * 
     * @param schemaid
     *            Schema ID where the classifiers are contained
     * @param complete
     *            When is true returns complete information of the schema otherwise
     *            just return the root classifiers
     * @return Set of classifiers
     * @throws SemanticallyIncorrectException
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Get all the Classifiers", notes = "Get all the classifiers from the catalogue with the given schema. <br/> "
	    + CLASSIFIERS_TYPE, response = ClassifierDTO.class, responseContainer = "Set")
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: schemas obtained", response = ClassifierDTO.class, responseContainer = "Set"),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Set<ClassifierDTO> getClassifiers(
	    @ApiParam(value = "Schema ID where the classifiers are contained", required = true) @PathParam("schemaid") long schemaid,
	    @ApiParam(value = "When is true returns complete information of the schema otherwise just return the root classifiers", required = false, defaultValue = "true") @DefaultValue("true") @QueryParam("complete") boolean complete)
	    throws SemanticallyIncorrectException {

	ClassificationSchema sch = Schemas.retrieveClassificationSchema(schemaid);// get the schema
	SchemaDTO schemaDTO = new SchemaDTO(sch);// create the DTO to return

	// In case that isin't complete, just add the root classifiers
	if (!complete) {
	    schemaDTO.setRootClassifiers(sch.getRootClassifiers());
	}
	return schemaDTO.getRootClassifiers();
    }

    /**
     * Substitute all the classifiers of the given schema
     * 
     * @param schemaSubstitutionUnmarshaller
     *            Unmarshaller with classifiers substitution fields
     * @param schemaid
     *            Schema ID where to substitute the classifiers
     * @return HTTP response
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Substitute all the classifiers of the given schema", notes = CLASSIFIER_SUBS_TEXT, response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Schema created", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of something semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response substituteClassifiers(
	    @ApiParam(value = "Unmarshaller with classifiers substitution fields", required = true) SchemaSubstitutionUmarshaller schemaSubstitutionUnmarshaller,
	    @ApiParam(value = "Schema ID where to substitute the classifiers", required = true) @PathParam("schemaid") long schemaid)
	    throws SemanticallyIncorrectException, IntegrityException, RedundancyException {
	ClassificationSchema old = Schemas.retrieveClassificationSchema(schemaid);
	SchemaDataController.deleteReferencedPatternOfTheSchema(old); // Delete all the pattern referenced pattern
	old.getRootClassifiers().clear(); // clear the list of root classifier
	schemaSubstitutionUnmarshaller.setSchema(old); // set the new schema inside the unmarshaller
	ClassificationSchema newcs = schemaSubstitutionUnmarshaller.build();
	SchemaDataController.saveSchema(newcs); // call the controller to save the classifiers
	return Response.status(Status.OK).build();
    }

    /**
     * Get a Classifier
     * 
     * @param schemaid
     *            ID of the schema where the classifier is found
     * @param classifierid
     *            ID of the classifier to find
     * @param complete
     *            When is true returns all the inner classifiers information,
     *            otherwise just the first level of classifiers
     * @return Requested classifier
     * @throws SemanticallyIncorrectException
     */
    @GET
    @Path("{classifierid}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Retrieve a Classifier", notes = "Get a classifier by ID. <br/>"
	    + CLASSIFIERS_TYPE, response = ClassifierDTO.class)
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: Classifier obtained", response = ClassifierDTO.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested schema is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public ClassifierDTO getClassifier(
	    @ApiParam(value = "ID of the schema where the classifier is found", required = true) @PathParam("schemaid") long schemaid,
	    @ApiParam(value = "ID of the classifier to find", required = true) @PathParam("classifierid") long classifierid,
	    @ApiParam(value = "When is true returns all the inner classifiers information, otherwise just the first level of classifiers", required = false, defaultValue = "true") @DefaultValue("true") @QueryParam("complete") boolean complete)
	    throws SemanticallyIncorrectException {
	Classifier ic = retrieveInternalClassifier(classifierid); // Get the classifier
	ClassifierDTO icDTO = new ClassifierDTO(ic, schemaid);// create a DTO
	if (complete) {// if the request is complete set the classifiers recursively
	    icDTO.setInternalClassifiersRecursive(ic.getInternalClassifiers(), schemaid);
	} else { // otherwise just get the first level of classifiers
	    Set<ClassifierDTO> setICDTOs = new HashSet<>();
	    for (Classifier innerIc : ic.getInternalClassifiers()) {
		setICDTOs.add(new ClassifierDTO(innerIc, schemaid));
	    }
	    icDTO.setInternalClassifiers(setICDTOs);
	}
	// get the all the internal classifier and it self in the DTO
	Set<ClassifierDTO> containedClassifiersAndItself = icDTO.getAllInternalClassifiers();
	containedClassifiersAndItself.add(icDTO);
	return icDTO;
    }

    /**
     * Update a classifier
     *            Unmarshaller with the fields to update
     * @param schemaid
     *            ID of the schema to update
     * @param internalId
     *            ID of the classifier to update
     * @return HTTP response
     * @throws Exception
     */
    @PUT
    @Path("{classifierid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update a Classifier", notes = CLASSIFIER_UPDATE_TEXT, response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Classifier updated", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested schema is not found.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of somehing semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updateInternalSchemaFields(
	    @ApiParam(value = "Unmarshaller with the fields to update", required = true) ClassifierUnmarshaller unm,
	    @ApiParam(value = "ID of the schema to update", required = true) @PathParam("schemaid") long schemaid,
	    @ApiParam(value = "ID of the classifier to update", required = true) @PathParam("classifierid") long internalId)
	    throws InternalError {
	logger.debug("Updating classifier");

	// Get the internal classifier
	Classifier old = retrieveInternalClassifier(internalId);
	try {
	    boolean isRoot = old.getType() == Classifier.ROOT;
	    Classifier newInternal = (Classifier) unm.build(isRoot); // Build the new InternalClassifier
	    // Update Classifier with the given patterns
	    SchemaDataController.updateInternalClassifier(old, newInternal);
	} catch (Exception e) {
	    logger.error(e.getMessage());
	    throw new InternalError(e.getMessage());
	}
	return Response.status(Status.OK).build();
    }

    /**
     * Gets InternalClassifier from the DB, throws {@link NotFoundException} when
     * the InternalClassifier is not found
     * 
     * @param id
     *            ID of the required InternalClassifier
     * @return Requested {@link Classifier}
     */
    private Classifier retrieveInternalClassifier(long internalId) {
	Classifier internal = SchemaDataController.getClassifier(internalId);
	if (internal == null)
	    throw new NotFoundException("InternalClassifier [" + internalId + "] not found");
	return internal;

    }

    /**
     * Retrieve the Classifier specified like path inside of the list passed as
     * parameter
     * 
     * @param namesList
     *            List of names to follow, the first element of the list should be
     *            the Schema name, followed by the rootClassifier, followed by N
     *            Classifier and the last name is of the classifier to be retrieved
     * @return Instance of {@link Classifier} if the list has correct values and
     *         exists indicated path otherwise return {@link Null}. The list must
     *         contain at least 3 elements.
     * @throws ValueException
     *             This exception is throwed when the number of elements in the list
     *             is < 3
     */
    public static Classifier getLastInternalClassifier(List<String> namesList) throws ValueException {
	return SchemaDataController.getInternalClassifierByNames(namesList);
    }
}
