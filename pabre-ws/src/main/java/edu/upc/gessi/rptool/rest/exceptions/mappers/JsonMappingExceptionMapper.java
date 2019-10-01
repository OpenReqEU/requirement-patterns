package edu.upc.gessi.rptool.rest.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonMappingException;

@Provider
public class JsonMappingExceptionMapper extends GenericUnsuccessfullExceptionMapper<JsonMappingException> {
    private static final Logger logger = Logger.getLogger(JsonMappingExceptionMapper.class.getName());

    @Override
    protected Response manageException(JsonMappingException exception) {
	logger.error("JsonMappingException", exception);
	String message = "[JsonMappingException]: ";
	if (exception.getCause() != null && exception.getCause().getMessage() != null) {
	    message += exception.getCause().getMessage();
	} else {
	    message += exception.getMessage();
	}
	System.err.println(message);
	return Response.status(Status.BAD_REQUEST).entity(message).type("text/plain").build();
    }
}
