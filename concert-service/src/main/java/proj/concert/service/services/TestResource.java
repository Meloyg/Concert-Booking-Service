package proj.concert.service.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proj.concert.service.util.ConcertUtils;

/**
 * This service allows the integration tests to reset the database via an HTTP
 * request. Do not modify this class.
 */
@Path("/concert-service-test")
public class TestResource {

    private static Logger LOGGER = LoggerFactory.getLogger(TestResource.class);

    /**
     * Resets the database to default values for testing purposes.
     */
    @GET
    @Path("/reset")
    public Response resetDatabase() {

        PersistenceManager.instance().reset();
        ConcertUtils.initConcerts();
        LOGGER.info("reset done");

        return Response.noContent().build();
    }

}
