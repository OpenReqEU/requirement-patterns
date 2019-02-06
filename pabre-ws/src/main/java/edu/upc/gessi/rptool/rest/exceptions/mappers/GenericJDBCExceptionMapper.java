package edu.upc.gessi.rptool.rest.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.hibernate.exception.GenericJDBCException;

@Provider
public class GenericJDBCExceptionMapper extends GenericUnsuccessfullExceptionMapper<GenericJDBCException> {
    private static final Logger logger = Logger.getLogger(GenericJDBCExceptionMapper.class.getName());

    @Override
    protected Response manageException(GenericJDBCException exception) {
	logger.error("GenericJDBCException", exception);

	String message = "[GenericJDBCException]: ";
	if (exception.getCause() != null && exception.getCause().getMessage() != null) {
	    message += exception.getCause().getMessage();
	} else {
	    message += exception.getMessage();
	}
	System.err.println("Message: " + message);
	return Response.status(Status.BAD_REQUEST).entity(message).type("text/plain").build();
    }

}
