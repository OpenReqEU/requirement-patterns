package edu.upc.gessi.rptool.rest;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import edu.upc.gessi.rptool.data.CatalogueDataController;
import edu.upc.gessi.rptool.data.GenericDataController;
import edu.upc.gessi.rptool.data.PatternDataController;
import edu.upc.gessi.rptool.data.SchemaDataController;
import edu.upc.gessi.rptool.data.mediators.MediatorPatterns;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.schema.Classifier;

@Path("/tests")
public class Tests {

    /**
     * Pending to improve the security identification
     */
    private static final String SECURITYPASS = "qwerty";

    /**
     * Method created to confirm that the current database will be reseted and
     * 
     * @param securityPass
     *            Security password
     * @return HTTP response
     */
    @POST
    @Path("/confirmTestingDB")
    public Response confirmTestingDB(String securityPass) {
	String returnEntity = "Working with DEVDB and is cleaned";
	if (securityPass.equals(SECURITYPASS)) {
	    GenericDataController.createSchema();
	}
	return Response.status(Status.OK).entity(returnEntity).build();
    }

    /**
     * Delete all the content of the Database
     * 
     * 
     * @return HTTP Response
     * @throws Exception
     */
    @GET
    @Path("/clearDB")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response clearAllDB() throws Exception {
	CatalogueDataController.clearDatabase();
	return Response.status(Status.OK).build();
    }

    /**
     * Testing Method
     * 
     * @param string
     * @param id
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @GET
    @Path("/1")
    @Produces({ MediaType.TEXT_PLAIN })
    public Response getTest1Result(@DefaultValue("") @QueryParam("keyword") String string,
	    @DefaultValue("24") @QueryParam("id1") long id1, @DefaultValue("24") @QueryParam("id2") long id2,
	    @DefaultValue("24") @QueryParam("id3") long id3, @DefaultValue("24") @QueryParam("id4") long id4)
	    throws Exception {
	RequirementPattern m = PatternDataController.getPattern(id1);
	if (m != null) {
	    System.out.println(m.getClassifiers().size());
	}

	Classifier internal = SchemaDataController.getClassifier(id2);
	if (internal != null) {
	    System.out.println("Number of patterns: " + internal.getPatterns().size());
	    if (internal.getParentClassifier() != null) {
		System.out.println("Parent classifiers: " + internal.getParentClassifier().getName());
	    }

	}

	List<RequirementPattern> l = MediatorPatterns.listPatternsWithoutClassifiers();
	for (RequirementPattern rp : l) {
	    System.out.println("Pattern: " + rp);
	}

	String version = "Executed";
	return Response.status(Status.OK).entity(version).build();
    }

}
