package edu.upc.gessi.rptool.rest.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.upc.gessi.rptool.data.mediators.MediatorConnection;

@Provider
public class MyResponseFilter implements ContainerResponseFilter {

    static final Logger logger = Logger.getLogger(MyResponseFilter.class);

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
	logger.debug("Entrying in the ResponseFilter to end the Transaction");
	Session s = MediatorConnection.getCurrentSession();

	boolean isSessionNull = s == null;
	boolean isOpen = s.isOpen();
	boolean isTransactionNull = s.getTransaction() == null;
	boolean wasRollbacked = s.getTransaction().wasRolledBack();
	boolean isActive = s.getTransaction().isActive();
	boolean isOK = isOpen && !isTransactionNull && !wasRollbacked && isActive;
	if (isOK) {
	    logger.info("Exception not occured");
	    MediatorConnection.endCurrentSessionTransaction();
	} else {
	    //Exception already must have been treated
	    logger.info("Exception happened or already finished the trasaction");
	    logger.error("Exception happened or already finished the trasaction");
	}

    }

}
