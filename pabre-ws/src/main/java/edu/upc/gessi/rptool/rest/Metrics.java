package edu.upc.gessi.rptool.rest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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

import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.data.MetricDataController;
import edu.upc.gessi.rptool.domain.metrics.DomainMetric;
import edu.upc.gessi.rptool.domain.metrics.FloatMetric;
import edu.upc.gessi.rptool.domain.metrics.IntegerMetric;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.SetMetric;
import edu.upc.gessi.rptool.domain.metrics.StringMetric;
import edu.upc.gessi.rptool.domain.metrics.TimePointMetric;
import edu.upc.gessi.rptool.domain.metrics.Type;
import edu.upc.gessi.rptool.rest.dtos.metrics.DomainMetricDTO;
import edu.upc.gessi.rptool.rest.dtos.metrics.FloatMetricDTO;
import edu.upc.gessi.rptool.rest.dtos.metrics.IntegerMetricDTO;
import edu.upc.gessi.rptool.rest.dtos.metrics.MetricDTO;
import edu.upc.gessi.rptool.rest.dtos.metrics.SetMetricDTO;
import edu.upc.gessi.rptool.rest.dtos.metrics.StringMetricDTO;
import edu.upc.gessi.rptool.rest.dtos.metrics.TimePointMetricDTO;
import edu.upc.gessi.rptool.rest.exceptions.MissingCreatorPropertyException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.DomainMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.FloatMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.IntegerMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.SetMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.StringMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.TimeMetricUnmarshaller;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters.DomainMetricUpdater;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters.FloatMetricUpdater;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters.IntegerMetricUpdater;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters.SetMetricUpdater;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters.StringMetricUpdater;
import edu.upc.gessi.rptool.rest.unmarshallers.metrics.updaters.TimeMetricUpdater;
import edu.upc.gessi.rptool.rest.utilities.Deserializer;
import edu.upc.gessi.rptool.rest.utilities.IdFormatter;
import edu.upc.gessi.rptool.rest.utilities.JsonUtilities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/metrics")
@Api(value = "Metrics", produces = MediaType.APPLICATION_JSON)
public class Metrics {

    private static final Logger logger = Logger.getLogger(Metrics.class.getName());

    // SWAGGER DOCUMENTATION NEEDED STRING
    private static final String FLOATTEXT = "The mandatory fields for a Float metric are: <strong>NAME</strong>, <strong>DESCRIPTION</strong>, <strong>COMMENTS</strong>, <strong>MINVALUE</strong> and <strong>MAXVALUE</strong>.<br/>";
    private static final String FLOATEXAMPLE = "Example: <br/>{\r\n" + "  \"name\": \"float metric\",\r\n"
	    + "  \"description\": \"Metric desc.\",\r\n" + "  \"comments\": \"Metric comments\",\r\n"
	    + "  \"sources\": [],\r\n" + "  \"minValue\": 0,\r\n" + "  \"maxValue\": 0,\r\n"
	    + "  \"defaultValue\": 0\r\n" + "}";
    private static final String INTEGERTEXT = "The mandatory fields for a Integer metric are: <strong>NAME</strong>, <strong>DESCRIPTION</strong>, <strong>COMMENTS</strong>, <strong>MINVALUE</strong> and <strong>MAXVALUE</strong>.<br/>";
    private static final String INTEGEREXAMPLE = "Example: <br/>{\r\n" + "  \"name\": \"integer metric\",\r\n"
	    + "  \"description\": \"Metric desc.\",\r\n" + "  \"comments\": \"Metric comments\",\r\n"
	    + "  \"sources\": [],\r\n" + "  \"minValue\": 0,\r\n" + "  \"maxValue\": 0,\r\n"
	    + "  \"defaultValue\": 0\r\n" + "}";
    private static final String STRINGTEXT = "The mandatory fields for a String metric are: <strong>NAME</strong>, <strong>DESCRIPTION</strong> and <strong>COMMENTS</strong>.<br/>";
    private static final String STRINGEXAMPLE = "Example: <br/>{\r\n" + "  \"name\": \"string metric\",\r\n"
	    + "  \"description\": \"Metric desc.\",\r\n" + "  \"comments\": \"Metric comments\",\r\n"
	    + "  \"sources\": [],\r\n" + "  \"defaultValue\": \"\"\r\n" + "}";
    private static final String TIMETEXT = "The mandatory fields for a Time metric are: <strong>NAME</strong>, <strong>DESCRIPTION</strong> and <strong>COMMENTS</strong>.<br/>";
    private static final String TIMEEXAMPLE = "Example: <br/>{\r\n" + "  \"name\": \"time metric\",\r\n"
	    + "  \"description\": \"Metric desc.\",\r\n" + "  \"comments\": \"Metric comments\",\r\n"
	    + "  \"sources\": [],\r\n" + "  \"date\": \"2014-12-25 22:56:44\"\r\n" + "}";
    private static final String SETTEXT = "The mandatory fields for a Set metric are: <strong>NAME</strong>, <strong>DESCRIPTION</strong>, <strong>COMMENTS</strong> and <strong>IDSIMPLE</strong>.<br/>";
    private static final String SETEXAMPLE = "Example: <br/>{\r\n" + "  \"name\": \"set metric\",\r\n"
	    + "  \"description\": \"Metric desc.\",\r\n" + "  \"comments\": \"Metric comments\",\r\n"
	    + "  \"sources\": [],\r\n" + "  \"idSimple\": 0\r\n" + "}";
    private static final String DOMAINTEXT = "The mandatory fields for a Domain metric are: <strong>NAME</strong>, <strong>DESCRIPTION</strong> and <strong>COMMENTS</strong>.<br/>";
    private static final String DOMAINEXAMPLE = "Example: <br/>{\r\n" + "  \"name\": \"domain metric\",\r\n"
	    + "  \"description\": \"Metric desc.\",\r\n" + "  \"comments\": \"Metric comments\",\r\n"
	    + "  \"sources\": [],\r\n" + "  \"defaultValue\": \"\",\r\n"
	    + "  \"possibleValues\": [\"a\", \"b\", \"c\", \"d\"]\r\n" + "}";
    private static final String METRICCREATION = "The metric to be created should have this follow formats in JSON:"
	    + "<ul>" + "<li><h3><strong>Float</strong></h3>: " + FLOATTEXT + FLOATEXAMPLE + " </li>"
	    + "<li><h3><strong>Integer</strong></h3>: " + INTEGERTEXT + INTEGEREXAMPLE + " </li>"
	    + "<li><h3><strong>String</strong></h3>: " + STRINGTEXT + STRINGEXAMPLE + " </li>"
	    + "<li><h3><strong>Time</strong></h3>: " + TIMETEXT + TIMEEXAMPLE + " </li>"
	    + "<li><h3><strong>Set</strong></h3>: " + SETTEXT + SETEXAMPLE + " </li>"
	    + "<li><h3><strong>Domain</strong></h3>: " + DOMAINTEXT + DOMAINEXAMPLE + " </li>" + "</ul>";

    /**
     * Get all the metrics in the catalogue.
     * 
     * @param complete
     *            Boolean that indicates if the return is complete information
     * @return List of metrics
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "List all the metrics", notes = "Returns all the metrics in the catalogue", response = MetricDTO.class, responseContainer = "List")
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "OK: The request has succeeded.", response = MetricDTO.class, responseContainer = "List"),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public List<MetricDTO> getMetrics(
	    @ApiParam(value = "True if the metric should have all the values", required = true, defaultValue = "false") @DefaultValue("false") @QueryParam("complete") boolean complete) {
	logger.info("Getting all the metrics...");
	List<MetricDTO> listMetricsDTOs = new LinkedList<>();
	List<Metric> listMetrics = MetricDataController.listMetrics(); // Obtain the metrics with the controller
	for (Metric m : listMetrics) {// for each metric obtained create the corresponding DTO
	    MetricDTO metricDTO;
	    if (complete && m.getType() == Type.SET) metricDTO = new SetMetricDTO((SetMetric) m);
	    else if (complete && m.getType() == Type.DOMAIN) metricDTO = new DomainMetricDTO((DomainMetric) m);
	    else {
		metricDTO = new MetricDTO(m);
		if (m instanceof Metric) {
		    logger.debug("Getting all the metrics... Metric ID: " + m.getId());
		}
	    }
	    listMetricsDTOs.add(metricDTO);// add the DTO to the list to return
	}
	return listMetricsDTOs;
    }

    /**
     * Get the metric with the given ID
     * 
     * @param id
     *            ID of the metric searched
     * @return HTTP message with the searched metric
     */
    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Retrieve a metric", notes = "Get a metric by ID", response = MetricDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Metric obtained", response = MetricDTO.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested metric is not found.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public MetricDTO getMetric(
	    @ApiParam(value = "ID of the metric to obtain", required = true) @PathParam("id") long id) {
	logger.info("Retreiving metric with ID: " + id);
	Metric m = retrieveMetric(id);// Obtain the metric
	logger.info("Getting metric with type: " + m.getType().toString());
	MetricDTO metricDTO;
	if (m.getType() == Type.DOMAIN) { // Depending of the type of the metric generate the DTO
	    metricDTO = new DomainMetricDTO((DomainMetric) m);
	} else if (m.getType() == Type.FLOAT) {
	    metricDTO = new FloatMetricDTO((FloatMetric) m);
	} else if (m.getType() == Type.INTEGER) {
	    metricDTO = new IntegerMetricDTO((IntegerMetric) m);
	} else if (m.getType() == Type.SET) {
	    metricDTO = new SetMetricDTO((SetMetric) m);
	} else if (m.getType() == Type.STRING) {
	    metricDTO = new StringMetricDTO((StringMetric) m);
	} else if (m.getType() == Type.TIME) {
	    metricDTO = new TimePointMetricDTO((TimePointMetric) m);
	} else {
	    metricDTO = new MetricDTO(m);
	}

	return metricDTO;
    }

    /**
     * Create a Metric
     * 
     * @param metricJson
     *            String in JSON format, should be deserialize in one of this:
     *            {@link FloatMetricUnmarshaller},
     *            {@link IntegerMetricUnmarshaller}, {@link SetMetricUnmarshaller},
     *            {@link TimeMetricUnmarshaller}, {@link DomainMetricUnmarshaller},
     *            {@link StringMetricUnmarshaller}
     * @param type
     *            type of the metric to create
     * @return HTTP message with the assigned ID to that metric
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a Metric", notes = "Creates a new metric.<br/><br/> <ul><li>Before creating a SetMetric, the simple metric should be created</li></ul>", response = IdFormatter.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Metric created", response = IdFormatter.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public IdFormatter createMetric(@ApiParam(value = METRICCREATION, required = true) String metricJson,
	    @ApiParam(value = "Type of metric to be created", required = true) @QueryParam("type") String type)
	    throws MissingCreatorPropertyException, IntegrityException, SemanticallyIncorrectException, ValueException, IOException {
	Metric m = null;
	logger.debug("Creating Metric...");
	logger.debug("Received type: " + type);
	if (type == null) {
	    throw new MissingCreatorPropertyException("Type not indicated");
	}
	try {
	    // Depending in which type of metric is, deserialize and build the the
	    // unmarshaller
	    if (type.equals("float")) {
		FloatMetricUnmarshaller fmu = Deserializer.deserialize(metricJson, FloatMetricUnmarshaller.class);
		m = fmu.build();
	    } else if (type.equals("integer")) {
		IntegerMetricUnmarshaller imu = Deserializer.deserialize(metricJson, IntegerMetricUnmarshaller.class);
		m = imu.build();
	    } else if (type.equals("set")) {
		SetMetricUnmarshaller imu = Deserializer.deserialize(metricJson, SetMetricUnmarshaller.class);
		m = imu.build();
	    } else if (type.equals("time")) {
		TimeMetricUnmarshaller fmu = Deserializer.deserialize(metricJson, TimeMetricUnmarshaller.class);
		m = fmu.build();
	    } else if (type.equals("domain")) {
		// Before saving the domain metric, we have to save the domain possible values
		DomainMetricUnmarshaller fmu = Deserializer.deserialize(metricJson, DomainMetricUnmarshaller.class);
		m = fmu.build();
		MetricDataController.save((DomainMetric) m);
		return new IdFormatter(m.getId());
	    } else if (type.equals("string")) {
		StringMetricUnmarshaller fmu = Deserializer.deserialize(metricJson, StringMetricUnmarshaller.class);
		m = fmu.build();
	    } else {
		throw new MissingCreatorPropertyException("Unsupported Type");
	    }
	} catch (IOException e) {
	    logger.error("Could not create Metric!", e);
	    throw e;
	}
	// Save the metric by the controller
	MetricDataController.save(m);
	return new IdFormatter(m.getId());
    }

    /**
     * Delete a metric
     * 
     * @param id
     *            given a ID delete the metric with that id.
     * @return HTTP message
     */
    @DELETE
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Delete a Metric", notes = "Deletes a metric", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Metric deleted", response = Response.class),
	    @ApiResponse(code = 400, message = "Bad request: The request has not been applied because of wrong information, for more information check the Body.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response deleteMetric(
	    @ApiParam(value = "ID of the metric to delete", required = true) @PathParam("id") long id) {
	logger.debug("Deleting metric with ID: " + id);
	Metric m = retrieveMetric(id);// Obtain the metric
	MetricDataController.delete(m); // delete it by the controller
	return Response.status(Status.OK).build();

    }

    /**
     * Update a metric
     * 
     * @param metricJson
     *            String in JSON format
     * @param id
     *            ID of the metric to update
     * @return HTTP message
     * @throws Exception
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Update a metric", notes = "Update the given fields of the metric", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK: Metric updated", response = Response.class),
	    @ApiResponse(code = 404, message = "Not Found: The requested metric is not found.", response = String.class),
	    @ApiResponse(code = 422, message = "Bad request: The request has not been applied because of somehing semantically incorrect.", response = String.class),
	    @ApiResponse(code = 500, message = "Internal Server Error. For more information see ‘message’ in the Response Body.", response = String.class) })
    public Response updateMetric(@ApiParam(value = METRICCREATION, required = true) String metricJson,
	    @ApiParam(value = "ID of the metric to obtain", required = true) @PathParam("id") long id)
	    throws Exception {
	logger.debug("Updating metric with ID: " + id);
	Metric m = retrieveMetric(id);// Obtain the metric
	// Depending of the type create a updater and update the fields
	try {
	    switch (m.getType()) {
	    case DOMAIN:
		new DomainMetricUpdater((DomainMetric) m, metricJson).update();
		break;
	    case FLOAT:
		new FloatMetricUpdater((FloatMetric) m, metricJson).update();
		break;
	    case INTEGER:
		new IntegerMetricUpdater((IntegerMetric) m, metricJson).update();
		break;
	    case SET:
		new SetMetricUpdater(m, metricJson).update();
		break;
	    case STRING:
		new StringMetricUpdater(m, metricJson, JsonUtilities.jsonHasField(metricJson, "defaultValue")).update();
		break;
	    case TIME:
		new TimeMetricUpdater(m, metricJson, JsonUtilities.jsonHasField(metricJson, "date")).update();
		break;
	    default:
		throw new SemanticallyIncorrectException("Type not valid");
	    }

	} catch (Exception e) {
	    logger.error(e.getMessage());
	    throw e;
	}

	return Response.status(Status.OK).build();
    }

    /**
     * Gets Metric from the DB, throws {@link NotFoundException} when the metric is
     * not found
     * 
     * @param id
     *            ID of the required Metric
     * @return Requested {@link Metric}
     */
    private Metric retrieveMetric(long id) {
	Metric m = MetricDataController.getMetric(id);
	if (m == null)
	    throw new NotFoundException("Metric [" + id + "] not found");
	return m;

    }

}
