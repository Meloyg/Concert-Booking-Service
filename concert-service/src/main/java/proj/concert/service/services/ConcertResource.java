package proj.concert.service.services;

import proj.concert.common.dto.*;
import proj.concert.common.types.*;
import proj.concert.service.domain.*;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.mappers.*;
import proj.concert.service.util.*;

import org.slf4j.*;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import java.net.URI;

import java.time.LocalDateTime;
import java.util.*;


/**
 * This class is a resource class for ConcertService.
 * The service module is responsible for implementing the Web service
 * and persistence aspects of the concert application.
 *
 * @author Melo Guan, Nancy Zhong, Alex Cao
 * @version 1.0
 */

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);
    private static final String AUTH = "auth";
    private static final List<Subscription> subscriptions = new ArrayList<>();

    // ========================================================
    // ================  Concerts Endpoint ====================
    // ========================================================

    /**
     * Retrieves a Concert based on its unique id.
     *
     * @param id the unique id of the Concert to be returned.
     * @return a concert.
     */
    @GET
    @Path("/concerts/{id}")
    public Response retrieveConcertById(@PathParam("id") long id) {
        LOGGER.info("Retrieving concert with id: " + id);
        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();
        try {
            em.getTransaction()
              .begin();

            Concert concert = em.find(Concert.class, id);

            em.getTransaction()
              .commit();

            if (concert==null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .build();
            }

            ConcertDTO concertDTO = ConcertMapper.toDTO(concert);

            return Response.ok(concertDTO)
                           .build();
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves a list of all Concerts.
     *
     * @return a list of concerts.
     */
    @GET
    @Path("/concerts")
    public Response getAllConcerts() {
        LOGGER.info("Retrieving all concerts.");
        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();

        try {
            em.getTransaction()
              .begin();

            List<Concert> concertList = em.createQuery("select c from Concert c", Concert.class)
                                          .getResultList();

            em.getTransaction()
              .commit();

            List<ConcertDTO> concertDTOList = ConcertMapper.listToDTO(concertList);
            GenericEntity<List<ConcertDTO>> entity = new GenericEntity<>(concertDTOList) {
            };
            return Response.ok(entity)
                           .build();
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves the summary of all Concerts.
     *
     * @return a list of concert summaries.
     */
    @GET
    @Path("/concerts/summaries")
    public Response getConcertSummaries() {
        LOGGER.info("Retrieving concerts summaries.");
        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();

        try {
            em.getTransaction()
              .begin();

            List<Concert> concerts = em.createQuery("select c from Concert c", Concert.class)
                                       .getResultList();

            em.getTransaction()
              .commit();

            List<ConcertSummaryDTO> concertSummaryDTOList = ConcertMapper.listToConcertSummaryDTO(concerts);
            GenericEntity<List<ConcertSummaryDTO>> entity = new GenericEntity<>(concertSummaryDTOList) {
            };
            return Response.ok(entity)
                           .build();
        } finally {
            em.close();
        }
    }

    //    ========================================================
    //    ================  Performer Endpoint ===================
    //    ========================================================

    /**
     * Retrieves a Performer by id.
     *
     * @param id the id of the performer to retrieve.
     * @return the performer.
     */
    @GET
    @Path("/performers/{id}")
    public Response retrievePerformerById(@PathParam("id") long id) {
        LOGGER.info("Getting a performer of id " + id);
        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();

        try {
            em.getTransaction()
              .begin();

            Performer performer = em.find(Performer.class, id);

            em.getTransaction()
              .commit();

            if (performer==null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .build();
            } else {
                return Response.ok(PerformerMapper.toDTO(performer))
                               .build();
            }
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves all Performer.
     *
     * @return a list of all Performer.
     */
    @GET
    @Path("/performers")
    public Response getAllPerformers() {
        LOGGER.info("Retrieving All performers.");
        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();
        try {
            em.getTransaction()
              .begin();

            List<Performer> performers = em.createQuery("select p from Performer p", Performer.class)
                                           .getResultList();

            em.getTransaction()
              .commit();

            List<PerformerDTO> performerDTOList = PerformerMapper.listToDTO(performers);
            GenericEntity<List<PerformerDTO>> entity = new GenericEntity<>(performerDTOList) {
            };
            return Response.ok(entity)
                           .build();
        } finally {
            em.close();
        }
    }

    //    ========================================================
    //    ===================  Auth Endpoint =====================
    //    ========================================================

    /**
     * Login a user.
     *
     * @param UserDTO
     * @return the user with the new generated cookie.
     */
    @POST
    @Path("/login")
    public Response Login(UserDTO userDTO) {
        LOGGER.info("Login with username: " + userDTO.getUsername());

        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();

        try {
            em.getTransaction()
              .begin();

            User user = em.createQuery("select user from User user where user.username = :username", User.class)
                          .setParameter("username", userDTO.getUsername())
                          .getResultList()
                          .stream()
                          .findFirst()
                          .orElse(null);

            if (user==null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .build();  // user not found
            } else {
                if (user.getPassword()
                        .equals(userDTO.getPassword())) {
                    String token = UUID.randomUUID()
                                       .toString();
                    user.setCookie(token);
                    em.merge(user);
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

    //    ========================================================
    //    =================  Booking Endpoint ====================
    //    ========================================================

    /**
     * Attempt to create a booking and
     * notify any subscribed user when a concert is about to sell out.
     *
     * @param BookingRequestDTO
     * @param cookie            token of the user that wants to book
     * @return URI that directs to the created booking
     */
    @POST
    @Path("/bookings")
    public Response createBooking(BookingRequestDTO bookingRequestDTO, @CookieParam(AUTH) Cookie cookie) {
        LOGGER.info("Create booking");

        if (cookie==null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .build();
        }

        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();
        try {
            em.getTransaction()
              .begin();

            User user = authUserWithCookie(em, cookie);

            if (user==null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .build();
            }

            Concert concert = em.find(Concert.class, bookingRequestDTO.getConcertId());

            if (concert==null || !concert.getDates()
                                         .contains(bookingRequestDTO.getDate())) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .build();
            }

            String query = "select s from Seat s where s.isBooked = false and s.date = :date and s.label in :labels";

            List<Seat> seats = em.createQuery(query, Seat.class)
                                 .setLockMode(LockModeType.OPTIMISTIC)
                                 .setParameter("date", bookingRequestDTO.getDate())
                                 .setParameter("labels", bookingRequestDTO.getSeatLabels())
                                 .getResultList();

            Booking newBooking = new Booking(user, bookingRequestDTO.getConcertId(), bookingRequestDTO.getDate());

            if (seats.size()!=bookingRequestDTO.getSeatLabels()
                                               .size()) {
                return Response.status(Response.Status.FORBIDDEN)
                               .build();
            }

            for (Seat seat : seats) {
                seat.setBooked(true);
                newBooking.getSeats()
                          .add(seat);
            }

            em.persist(newBooking);
            em.getTransaction()
              .commit();

            List<Seat> bookedSeatsList = em.createQuery("select s from Seat s where s.date = :date and s.isBooked = true", Seat.class)
                                           .setParameter("date", bookingRequestDTO.getDate())
                                           .getResultList();

            if (!subscriptions.isEmpty()) {
                LOGGER.debug("makeBooking(): Checking to notify subscribers");

                List<Subscription> subs = new ArrayList<>();

                for (Subscription sub : subscriptions) {
                    if ((sub.getSubDto()
                            .getConcertId()==bookingRequestDTO.getConcertId())
                            && (sub.getSubDto()
                                   .getDate()
                                   .equals(bookingRequestDTO.getDate()))) {
                        if (sub.getSubDto()
                               .getPercentageBooked() < ((100 * (bookedSeatsList.size()))
                                / TheatreLayout.NUM_SEATS_IN_THEATRE)) {
                            LOGGER.debug("makeBooking(): Threshold reached: Notifying subscriber");
                            subs.add(sub);
                        }
                    }
                }
                if (!subs.isEmpty()) {
                    postToSubs(subs, TheatreLayout.NUM_SEATS_IN_THEATRE - bookedSeatsList.size());
                }

            }
            LOGGER.debug("makeBooking(): Successfully made booking: " + newBooking.toString());

            return Response.created(URI.create("concert-service/bookings/" + newBooking.getId()))
                           .cookie(makeCookie(cookie.getValue()))
                           .build();

        } catch (Exception e) {
            LOGGER.error("Error creating booking", e);
            return Response.serverError()
                           .build();
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves all Bookings of one user.
     *
     * @param cookie token of the current user.
     * @return all Bookings of that user.
     */
    @GET
    @Path("/bookings")
    public Response getAllBooking(@CookieParam(AUTH) Cookie cookie) {
        LOGGER.info("Get all bookings.");

        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();

        try {
            em.getTransaction()
              .begin();

            if (cookie==null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .build();
            }

            User user = authUserWithCookie(em, cookie);

            if (user==null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .build();
            }

            List<Booking> bookings = em.createQuery("select booking from Booking booking where booking.user = :user", Booking.class)
                                       .setParameter("user", user)
                                       .getResultList();

            List<BookingDTO> bookingsDTO = BookingMapper.listToDTO(bookings);
            GenericEntity<List<BookingDTO>> bookingsEntity = new GenericEntity<>(bookingsDTO) {
            };

            em.getTransaction()
              .commit();

            return Response.ok(bookingsEntity)
                           .cookie(makeCookie(cookie.getValue()))
                           .build();
        } finally {
            em.close();
        }

    }

    /**
     * Retrieves a Booking by id.
     *
     * @param id     the id of the booking to retrieve.
     * @param cookie token of the current user.
     * @return the booking.
     */
    @GET
    @Path("/bookings/{id}")
    public Response getBookingById(@PathParam("id") Long id, @CookieParam(AUTH) Cookie cookie) {
        LOGGER.info("Get booking by id.");

        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();

        try {
            em.getTransaction()
              .begin();

            User user = authUserWithCookie(em, cookie);

            if (user==null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .build();
            }

            Booking booking = em.find(Booking.class, id);

            if (booking==null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .build();
            }

            if (booking.getUser()!=user) {
                return Response.status(Response.Status.FORBIDDEN)
                               .build();
            }

            em.getTransaction()
              .commit();

            BookingDTO bookingDTO = BookingMapper.toDTO(booking);

            return Response.ok(bookingDTO)
                           .cookie(makeCookie(cookie.getValue()))
                           .build();

        } finally {
            em.close();
        }

    }

    //    ========================================================
    //    =================  Seats Endpoint ====================
    //    ========================================================

    /**
     * Retrieves Seats .
     *
     * @param date   the booking of concert's dates.
     * @param status seats status.
     * @return seats.
     */
    @GET
    @Path("seats/{date}")
    public Response getSeats(@PathParam("date") String date, @QueryParam("status") BookingStatus status) {
        LOGGER.debug("getSeats(): Getting " + status.toString() + " seats for date: " + date + " that are " + status);

        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();

        try {
            LocalDateTime ldt = new LocalDateTimeParam(date).getLocalDateTime();
            List<Seat> seats;

            if (status!=BookingStatus.Any) {
                // Get only booked/non-booked seats for the date
                boolean isBooked = (status==BookingStatus.Booked);

                seats = em.createQuery("select s from Seat s where s.date = :date and s.isBooked = :status", Seat.class)
                          .setParameter("date", ldt)
                          .setParameter("status", isBooked)
                          .getResultList();

            } else {
                // Get all seats for the date
                seats = em.createQuery("select s from Seat s where s.date = :date", Seat.class)
                          .setParameter("date", ldt)
                          .getResultList();
            }

            List<SeatDTO> dtos = new ArrayList<>();
            for (Seat c : seats) {
                dtos.add(SeatMapper.toDTO(c));
            }

            LOGGER.debug("getSeats(): Found " + dtos.size() + " " + status.toString() + " seats");

            GenericEntity<List<SeatDTO>> entity = new GenericEntity<List<SeatDTO>>(dtos) {
            };

            return Response.ok(entity)
                           .build();

        } finally {
            em.close();
        }
    }

    //    ========================================================
    //    =================  Subscribe Endpoint ==================
    //    ========================================================

    /**
     * Send subscribe message .
     *
     * @param dto    the ConcertInfoSubscriptionDTO
     * @param cookie token of the user that wants to book
     * @param resp   AsyncResponse.
     */
    @POST
    @Path("/subscribe/concertInfo")
    public void subscribe(ConcertInfoSubscriptionDTO dto, @CookieParam(AUTH) Cookie cookie, @Suspended AsyncResponse resp) {
        LOGGER.debug("subscribe():Trying to subscribe");

        // Check cookie, make sure user is authorized
        if (cookie==null) {
            LOGGER.debug("subscribe(): User not logged in");
            resp.resume(Response.status(Response.Status.UNAUTHORIZED)
                                .build());
        }

        EntityManager em = PersistenceManager.instance()
                                             .createEntityManager();

        try {
            // find the concert
            Concert concert = em.find(Concert.class, dto.getConcertId());

            // concert not exist
            if (concert==null) {
                LOGGER.debug("subscribe(): Concert id: " + dto.getConcertId() + "does not exists");
                resp.resume(Response.status(Response.Status.BAD_REQUEST)
                                    .build());
            }

            // concert is not on right date
            if (!concert.getDates()
                        .contains(dto.getDate())) {
                LOGGER.debug("subscribe(): Concert id: " + dto.getConcertId() + "does not held on" + concert.getDates()
                                                                                                            .toString());
                resp.resume(Response.status(Response.Status.BAD_REQUEST)
                                    .build());
            }

            List<Booking> allmatchedBookings = em.createQuery("select b from Booking b where b.concertId = : id and b.date = :date", Booking.class)
                                                 .setParameter("id", dto.getConcertId())
                                                 .setParameter("date", dto.getDate())
                                                 .getResultList();

            LOGGER.debug("subscribe(): Number of bookings: " + allmatchedBookings.size());

            List<Seat> allmatchedSeats = new ArrayList<>();

            for (Booking b : allmatchedBookings) {
                allmatchedSeats.addAll(b.getSeats());
            }

            LOGGER.debug("subscribe(): Booked seats: " + allmatchedSeats.size());
            LOGGER.debug("subscribe(): This concert has been booked: " + dto.getPercentageBooked() + "%");

            if (dto.getPercentageBooked() < (allmatchedSeats.size() / TheatreLayout.NUM_SEATS_IN_THEATRE) * 100) {

                LOGGER.debug("subscribe():Please notifying subscriber");
                ConcertInfoNotificationDTO infoDto = new ConcertInfoNotificationDTO(
                        TheatreLayout.NUM_SEATS_IN_THEATRE - allmatchedSeats.size());
                resp.resume(Response.ok(infoDto)
                                    .build());
                return;
            }
            // Added in the list will be notified later
            LOGGER.debug("subscribe(): Successfully subscribed");
            subscriptions.add(new Subscription(dto, resp));

        } finally {
            em.close();
        }
    }

    //    ========================================================
    //    ==================  Helper Functions ===================
    //    ========================================================

    /**
     * Helper function to generate a Cookie with a token.
     *
     * @param authToken
     * @return NewCookie
     */
    private NewCookie makeCookie(String authToken) {
        NewCookie newCookie = new NewCookie(AUTH, authToken);
        LOGGER.info("Generated cookie: " + newCookie.getValue());
        return newCookie;
    }

    /**
     * Helper function to post a notification to all subscribers.
     *
     * @param SubscriptionList
     * @param numSeatsRemaining
     */
    private void postToSubs(List<Subscription> subs, int numSeatsRemaining) {
        LOGGER.debug("postToSubs(): Notifying " + subs.size() + " subscribers");

        synchronized (subscriptions) {
            for (Subscription sub : subs) {
                LOGGER.debug("postToSubs(): Notifying sub for concert id: " + sub.getSubDto()
                                                                                 .getConcertId()
                        + " and threshold: " + sub.getSubDto()
                                                  .getPercentageBooked() + "%");

                ConcertInfoNotificationDTO dto = new ConcertInfoNotificationDTO(numSeatsRemaining);
                sub.getResponse()
                   .resume(Response.ok(dto)
                                   .build());

                subscriptions.remove(sub);
            }
        }
    }

    /**
     * Helper function to find a user by their cookie.
     * If the cookie is not found, return null.
     *
     * @param EntityManager
     * @param Cookie
     * @return User or Null
     */
    private User authUserWithCookie(EntityManager em, Cookie cookie) {
        return em.createQuery("select user from User user where user.cookie = :cookie", User.class)
                 .setParameter("cookie", cookie.getValue())
                 .getResultList()
                 .stream()
                 .findFirst()
                 .orElse(null);
    }

}
