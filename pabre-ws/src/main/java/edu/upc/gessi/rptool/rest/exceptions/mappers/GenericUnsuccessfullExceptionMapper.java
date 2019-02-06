package edu.upc.gessi.rptool.rest.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.upc.gessi.rptool.data.mediators.MediatorConnection;


public abstract class GenericUnsuccessfullExceptionMapper<E extends Throwable> implements ExceptionMapper<E> {
    private static final Logger logger = Logger.getLogger(GenericUnsuccessfullExceptionMapper.class.getName());

    @Override
    public Response toResponse(E exception) {
	treatSuccess(exception);
	return manageException(exception);
    }

    /**
     * Method used to do the rollback
     * 
     * @param t
     *            Throwable that occured
     */
    protected void treatSuccess(Throwable t) {
	Session s = MediatorConnection.getCurrentSession();
	if (s != null && s.isOpen() && s.getTransaction() != null && s.getTransaction().isActive()) {
	    logger.info(t.getMessage());
	    s.getTransaction().rollback();
	}
    }

    protected abstract Response manageException(E exception);

}
