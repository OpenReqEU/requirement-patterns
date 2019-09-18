package edu.upc.gessi.rptool.listeners;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import edu.upc.gessi.rptool.rest.filters.MyRequestFilter;
import edu.upc.gessi.rptool.rest.filters.MyResponseFilter;

@ApplicationPath("api")
public class JerseyApplication extends ResourceConfig {
    public JerseyApplication() {
	// Register the filters
	register(MyRequestFilter.class);
	register(MyResponseFilter.class);
	
	//Register Jackson
	register(MyObjectMapperProvider.class);
	
	//Register the Linking to use the @InjectLink feature
	register(DeclarativeLinkingFeature.class);

	// Register swagger packages
	packages("io.swagger.jaxrs.json");
	packages("io.swagger.jaxrs.listing");

	// Register own API
	packages("edu.upc.gessi.rptool.rest");

    }

}
