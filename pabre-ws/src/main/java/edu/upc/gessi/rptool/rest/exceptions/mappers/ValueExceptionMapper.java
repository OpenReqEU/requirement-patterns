package edu.upc.gessi.rptool.rest.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.exceptions.ValueException;

@Provider
public class ValueExceptionMapper extends GenericUnsuccessfullExceptionMapper<ValueException> {
    private static final Logger logger = Logger.getLogger(ValueExceptionMapper.class.getName());

    @Override
    protected Response manageException(ValueException exception) {
	logger.error("ValueException", exception);
	String message = "[ValueException]: ";
	if (exception.getCause() != null && exception.getCause().getMessage() != null) {
	    message += exception.getCause().getMessage();
	} else {
	    message += exception.getMessage();
	}
	System.err.println(message);
	return Response.status(422).entity(exception.getMessage()).type("text/plain").build();
    }

}
