package proj.concert.service.services;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;
import proj.concert.service.mappers.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;


@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    // TODO Implement this.
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);

    @GET
    @Path("/concerts/{id}")
    public Response retrieveConcert(@PathParam("id") long id) {
        LOGGER.info("Retrieving concert with id: " + id);

        // look up the concert in memory data structure
        EntityManager em = PersistenceManager.instance().createEntityManager();
        Concert concert = em.find(Concert.class, id);
        if (concert == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ConcertMapper.toDTO(concert)).build();
    }

    @GET
    @Path("/performers/{id}")
    public Response retrievePerformer(@PathParam("id") long id) {
        LOGGER.info("Retrieving performer with id: " + id);
        // look up the concert in memory data structure
        EntityManager em = PersistenceManager.instance().createEntityManager();
        Performer concert = em.find(Performer.class, id);
        if (concert == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(concert).build();
    }

    @POST
    @Path("/login/{id}")
    public Response Login(@PathParam("id") long id) {
        LOGGER.info("Retrieving performer with id: " + id);
        // look up the concert in memory data structure
        EntityManager em = PersistenceManager.instance().createEntityManager();
        Performer concert = em.find(Performer.class, id);
        if (concert == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(concert).build();
    }

}
