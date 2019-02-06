package edu.upc.gessi.rptool.rest.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.hibernate.PropertyValueException;

@Provider
public class PropertyValueExceptionMapper extends GenericUnsuccessfullExceptionMapper<PropertyValueException> {
    private static final Logger logger = Logger.getLogger(PropertyValueExceptionMapper.class.getName());

    @Override
    protected Response manageException(PropertyValueException exception) {
	logger.error("PropertyValueException", exception);
	String message = "[PropertyValueException]: ";
	if (exception.getCause() != null && exception.getCause().getMessage() != null) {
	    message += exception.getCause().getMessage();
	} else {
	    message += exception.getMessage();
	}
	System.err.println(message);
	return Response.status(Status.BAD_REQUEST).entity(message).type("text/plain").build();
    }

}
