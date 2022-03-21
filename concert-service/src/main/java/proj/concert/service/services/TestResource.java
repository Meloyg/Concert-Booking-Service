package proj.concert.service.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import proj.concert.service.util.ConcertUtils;

/**
 * This service allows the integration tests to reset the database via an HTTP request. Do not modify this class.
 */
@Path("/concert-service-test")
public class TestResource {

    /**
     * Resets the database to default values for testing purposes.
     */
    @GET
    @Path("/reset")
    public Response resetDatabase() {

        PersistenceManager.instance().reset();
        ConcertUtils.initConcerts();

        return Response.noContent().build();
    }

}
