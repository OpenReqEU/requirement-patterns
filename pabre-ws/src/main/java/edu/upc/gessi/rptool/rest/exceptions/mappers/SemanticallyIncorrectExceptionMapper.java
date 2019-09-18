package edu.upc.gessi.rptool.rest.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

@Provider
public class SemanticallyIncorrectExceptionMapper
	extends GenericUnsuccessfullExceptionMapper<SemanticallyIncorrectException> {
    private static final Logger logger = Logger.getLogger(SemanticallyIncorrectExceptionMapper.class.getName());

    @Override
    protected Response manageException(SemanticallyIncorrectException exception) {
	logger.error("SemanticallyIncorrectException", exception);
	String message = "[SemanticallyIncorrectException]: ";
	if (exception.getCause() != null && exception.getCause().getMessage() != null) {
	    message += exception.getCause().getMessage();
	} else {
	    message += exception.getMessage();
	}
	logger.error(message);
	return Response.status(422).entity(message).type("text/plain").build();
    }

}
