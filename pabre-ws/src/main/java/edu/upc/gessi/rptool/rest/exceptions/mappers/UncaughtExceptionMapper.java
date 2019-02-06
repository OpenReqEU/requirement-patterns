package edu.upc.gessi.rptool.rest.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

@Provider
public class UncaughtExceptionMapper extends GenericUnsuccessfullExceptionMapper<Throwable> {
    private static final Logger logger = Logger.getLogger(UncaughtExceptionMapper.class.getName());

    @Override
    protected Response manageException(Throwable exception) {
	logger.error("UncaughtException", exception);
	return Response.status(Status.INTERNAL_SERVER_ERROR).entity(
		"[UncaughtException] Something bad happened. Please try again or contact with the development team with the following message:\n"
			+ exception.getMessage() + "\n" + ExceptionUtils.getStackTrace(exception))
		.type("text/plain").build();
    }

}
