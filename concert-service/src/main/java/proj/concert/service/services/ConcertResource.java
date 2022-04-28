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
        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();
        Concert concert = em.find(Concert.class, id);
        if (concert==null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .build();
        }
        return Response.ok(ConcertMapper.toDTO(concert))
                       .build();
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
        LOGGER.info("Getting a performer of id " + id);
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();

            Performer performer = em.find(Performer.class, id);

            em.getTransaction().commit();

            if (performer == null) {
                LOGGER.debug("No performer with id: " + id + " exists");
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok(PerformerMapper.toDTO(performer)).build();
            }

        } finally {
            em.close();
        }
    }

    @GET
    @Path("/performers")
    public Response getAllPerformers(){
        LOGGER.info("Retrieving All performers.");
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try{
            em.getTransaction().begin();

            List<Performer> performers = em.createQuery("select p from Performer p",Performer.class).getResultList();

            em.getTransaction().commit();

            List<PerformerDTO> performerDTOList = PerformerMapper.listToDTO(performers);
            GenericEntity<List<PerformerDTO>> entity = new GenericEntity<>(performerDTOList) {};
            return Response.ok(entity).build();
        }finally {
            em.close();
        }
    }

    @POST
    @Path("/login")
    public Response Login(UserDTO userDTO) {
        LOGGER.info("Login with username: " + userDTO.getUsername());

        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();

        try {
            em.getTransaction()
              .begin();
            User user  = em.createQuery("select user from User user where user.username = :username", User.class)
                                  .setParameter("username", userDTO.getUsername())
                                  .getSingleResult();
            // User user = em.find(User.class, userDTO.getUsername());
            if (user==null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .build();  // user not found
            } else {
                if (user.getPassword()
                        .equals(userDTO.getPassword())) {
                    String token = UUID.randomUUID()
                                       .toString();
                    user.setCookie(token);
                    em.getTransaction()
                      .commit();
                    return Response.ok(user)
                                   .cookie(makeCookie(token))
                                   .build();
                } else {
                    return Response.status(Response.Status.UNAUTHORIZED)
                                   .build();  // password incorrect
                }
            }
        } finally {
            em.close();
        }
    }


    //    Helper method to create a cookie
    private NewCookie makeCookie(String authToken) {

        NewCookie newCookie = new NewCookie("auth", authToken);
        LOGGER.info("Generated cookie: " + newCookie.getValue());

        return newCookie;
    }

}
