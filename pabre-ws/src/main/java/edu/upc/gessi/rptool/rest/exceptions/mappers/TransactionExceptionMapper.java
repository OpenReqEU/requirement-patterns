package edu.upc.gessi.rptool.rest.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.hibernate.TransactionException;

@Provider
public class TransactionExceptionMapper extends GenericUnsuccessfullExceptionMapper<TransactionException> {
    private static final Logger logger = Logger.getLogger(TransactionExceptionMapper.class.getName());

    @Override
    protected Response manageException(TransactionException exception) {
	logger.error("TransactionException", exception);
	if (exception.getMessage().contains("JDBC rollback")) {
	    return Response.status(422).entity("Failed to connect to the database").type("text/plain").build();
	} else if (exception.getMessage().contains("not successfully started")) {
	    return Response.status(422).entity("Database not connected").type("text/plain").build();
	}
	String message = "[TransactionException]: ";
	if (exception.getCause() != null && exception.getCause().getMessage() != null) {
	    message += exception.getCause().getMessage();
	} else {
	    message += exception.getMessage();
	}
	System.err.println(message);
	return Response.status(422).entity(message).type("text/plain").build();
    }

}
