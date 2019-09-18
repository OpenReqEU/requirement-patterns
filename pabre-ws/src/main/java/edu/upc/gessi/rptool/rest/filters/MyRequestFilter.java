package edu.upc.gessi.rptool.rest.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.data.mediators.MediatorConnection;

@Provider
public class MyRequestFilter implements ContainerRequestFilter {
    static final Logger logger = Logger.getLogger(MyRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext arg0) throws IOException {
	logger.debug("Entrying in the RequestFilter to beginTransaction");
	MediatorConnection.beginTransaction();

    }

}
