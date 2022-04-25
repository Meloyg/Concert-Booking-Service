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


@Path("/concerts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);


    @GET
    @Path("/concerts")
    public Response getConcerts(@Context UriInfo uriInfo) {
        LOGGER.info("getConcerts");

        EntityManager em = new PersistenceManager().createEntityManager();

        try {
            TypedQuery<Concert> query = em.createQuery("SELECT c FROM Concert c", Concert.class);
            List<Concert> concerts = query.getResultList();

            if (concerts.isEmpty()) {
                return Response.status(204).build();
            }

            Set<ConcertDTO> dtoConcerts = new HashSet<>();
            concerts.forEach(concert -> dtoConcerts.add(ConcertMapper.toDTO(concert));
            em.getTransaction().commit();

            GenericType<Set<ConcertDTO>> result = new GenericType<Set<ConcertDTO>>(dtoConcerts) {};
            return Response.ok(result).build();

        } catch (Exception e) {
            LOGGER.error("Exception in getConcerts", e);
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
        }

    }



}
