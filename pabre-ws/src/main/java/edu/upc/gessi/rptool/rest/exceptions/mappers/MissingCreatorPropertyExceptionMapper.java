package edu.upc.gessi.rptool.rest.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.rest.exceptions.MissingCreatorPropertyException;

@Provider
public class MissingCreatorPropertyExceptionMapper
	extends GenericUnsuccessfullExceptionMapper<MissingCreatorPropertyException> {
    private static final Logger logger = Logger.getLogger(MissingCreatorPropertyExceptionMapper.class.getName());

    @Override
    protected Response manageException(MissingCreatorPropertyException exception) {
	logger.error("MissingCreatorPropertyException", exception);
	String message = "[MissingCreatorPropertyException]: ";
	if (exception.getCause() != null && exception.getCause().getMessage() != null) {
	    message += exception.getCause().getMessage();
	} else {
	    message += exception.getMessage();
	}
	logger.error(message);
	return Response.status(400).entity(message).type("text/plain").build();
    }

}