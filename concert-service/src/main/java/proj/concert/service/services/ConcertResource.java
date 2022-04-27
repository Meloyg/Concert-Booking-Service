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
    public Response retrieveConcertById(@PathParam("id") long id) {
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
    @Path("/concerts")
    public Response getAllConcerts() {
        LOGGER.info("Retrieving all concerts.");
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try{
            em.getTransaction().begin();

            List<Concert> concertList = em.createQuery("select c from Concert c", Concert.class) .getResultList();
            em.getTransaction().commit();

            List<ConcertDTO> concertDTOList = ConcertMapper.listToDTO(concertList);
            GenericEntity<List<ConcertDTO>> entity = new GenericEntity<>(concertDTOList) {};
            return Response.ok(entity).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/concerts/summaries")
    public Response getConcertSummaries(){
        LOGGER.info("Retrieving concerts summaries.");
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try{
            em.getTransaction().begin();

            List<Concert> concerts = em.createQuery("select c from Concert c", Concert.class).getResultList();
            em.getTransaction().commit();

            List<ConcertSummaryDTO> concertSummaryDTOList = ConcertMapper.listToConcertSummaryDTO(concerts);
            GenericEntity<List<ConcertSummaryDTO>> entity = new GenericEntity<>(concertSummaryDTOList) {};
            return Response.ok(entity).build();
        }finally {
            em.close();
        }
    }

    @GET
    @Path("/performers/{id}")
    public Response retrievePerformerById(@PathParam("id") long id) {
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
