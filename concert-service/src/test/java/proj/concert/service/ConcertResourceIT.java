package proj.concert.service;

import org.junit.*;

import proj.concert.common.dto.*;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.types.Genre;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ConcertResourceIT {

    private static final String WEB_SERVICE_URI = "http://localhost:10000/services/concert-service";
    private Client client;

    /**
     * Ensures the DB is in the same state before running each test.
     */
    @Before
    public void setUp() {

        client = ClientBuilder.newClient();

        Response response = client
                .target(WEB_SERVICE_URI + "-test/reset")
                .request().get();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    /**
     * Kills the client after every test, to get rid of any leftover cookies.
     */
    @After
    public void tearDown() {
        client.close();
        client = null;
    }

    /**
     * Tests that we can get a single concert from the web service, given its id.
     * <p>
     * The request should return a 200 response, with the requested concert DTO. The concert DTO should contain all
     * performers and dates for that concert.
     */
    @Test
    public void testGetSingleConcert() {

        ConcertDTO concert = client.target(WEB_SERVICE_URI + "/concerts/1").request().get(ConcertDTO.class);

        assertEquals("PTX: The World Tour", concert.getTitle());
        assertEquals("concerts/ptx.jpg", concert.getImageName());

        assertEquals(1, concert.getPerformers().size());

        PerformerDTO performer = concert.getPerformers().get(0);

        assertEquals("Pentatonix", performer.getName());
        assertEquals("performers/ptx.jpg", performer.getImageName());
        assertEquals(Genre.Acappella, performer.getGenre());

        assertEquals(1, concert.getDates().size());
        assertEquals(LocalDateTime.of(2020, 2, 15, 20, 0, 0), concert.getDates().get(0));
    }

    /**
     * A more advanced version of the test above. Makes sure the web service still functions correctly when requesting
     * a concert with multiple performers and dates.
     */
    @Test
    public void testGetSingleConcertWithMultiplePerformersAndDates() {

        ConcertDTO concert = client.target(WEB_SERVICE_URI + "/concerts/4").request().get(ConcertDTO.class);

        assertEquals("Hugh Jackman: The Man. The Music. The Show.", concert.getTitle());

        assertEquals(2, concert.getPerformers().size());

        concert.getPerformers().sort(Comparator.comparing(PerformerDTO::getId));
        assertEquals("Hugh Jackman", concert.getPerformers().get(0).getName());
        assertEquals("Keala Settle", concert.getPerformers().get(1).getName());

        assertEquals(2, concert.getDates().size());
        concert.getDates().sort(Comparator.naturalOrder());
        assertEquals(LocalDateTime.of(2019, 9, 6, 20, 0, 0), concert.getDates().get(0));
        assertEquals(LocalDateTime.of(2019, 9, 7, 20, 0, 0), concert.getDates().get(1));
    }

    /**
     * Tests that a 404 response is returned when requesting a nonexistent concert.
     */
    @Test
    public void testGetNonExistentConcert() {
        Response response = client.target(WEB_SERVICE_URI + "/concerts/100").request().get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    /**
     * Tests that all concerts are returned when requested.
     */
    @Test
    public void testGetAllConcerts() {

        List<ConcertDTO> concerts = client
                .target(WEB_SERVICE_URI + "/concerts")
                .request()
                .get(new GenericType<List<ConcertDTO>>() {
                });

        assertEquals(8, concerts.size());

        concerts.sort(Comparator.comparing(ConcertDTO::getId));

        assertEquals("PTX: The World Tour", concerts.get(0).getTitle());
        assertEquals("Fleetwood Mac", concerts.get(1).getTitle());
        assertEquals("Bastille: Doom Days Tour", concerts.get(2).getTitle());
        assertEquals("Hugh Jackman: The Man. The Music. The Show.", concerts.get(3).getTitle());
        assertEquals("KISS: End of the Road World Tour", concerts.get(4).getTitle());
        assertEquals("Khalid: Free Spirit Tour", concerts.get(5).getTitle());
        assertEquals("Little Mix: LM5 Tour", concerts.get(6).getTitle());
        assertEquals("Shawn Mendes, with special guest Ruel", concerts.get(7).getTitle());

        for (ConcertDTO c : concerts) {
            assertTrue(c.getPerformers().size() > 0);
            assertTrue(c.getDates().size() > 0);
        }

    }

    /**
     * Tests that all concert summaries are returned when requested. Concert summaries contain only the id, title, and
     * image name for each concert.
     */
    @Test
    public void testGetConcertSummaries() {

        List<ConcertSummaryDTO> concerts = client
                .target(WEB_SERVICE_URI + "/concerts/summaries")
                .request()
                .get(new GenericType<List<ConcertSummaryDTO>>() {
                });

        assertEquals(8, concerts.size());

        concerts.sort(Comparator.comparing(ConcertSummaryDTO::getId));

        assertEquals("PTX: The World Tour", concerts.get(0).getTitle());
        assertEquals("Fleetwood Mac", concerts.get(1).getTitle());
        assertEquals("Bastille: Doom Days Tour", concerts.get(2).getTitle());
        assertEquals("Hugh Jackman: The Man. The Music. The Show.", concerts.get(3).getTitle());
        assertEquals("KISS: End of the Road World Tour", concerts.get(4).getTitle());
        assertEquals("Khalid: Free Spirit Tour", concerts.get(5).getTitle());
        assertEquals("Little Mix: LM5 Tour", concerts.get(6).getTitle());
        assertEquals("Shawn Mendes, with special guest Ruel", concerts.get(7).getTitle());

    }

    /**
     * Tests that a 200 response is returned, along with the correct performer info, when requesting a performer with
     * a given id.
     */
    @Test
    public void testGetSinglePerformer() {

        PerformerDTO performer = client.target(WEB_SERVICE_URI + "/performers/1").request().get(PerformerDTO.class);

        assertEquals("Pentatonix", performer.getName());
        assertEquals("performers/ptx.jpg", performer.getImageName());
        assertEquals(Genre.Acappella, performer.getGenre());

    }

    /**
     * Tests that a 404 response is returned when requesting a nonexistent performer.
     */
    @Test
    public void testGetNonExistentPerformer() {

        Response response = client.target(WEB_SERVICE_URI + "/performers/100").request().get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

    }

    /**
     * Tests that all performers are returned when requested.
     */
    @Test
    public void testGetAllPerformers() {

        List<PerformerDTO> performers = client
                .target(WEB_SERVICE_URI + "/performers")
                .request()
                .get(new GenericType<List<PerformerDTO>>() {
                });

        assertEquals(11, performers.size());

        performers.sort(Comparator.comparing(PerformerDTO::getId));

        assertEquals("Pentatonix", performers.get(0).getName());
        assertEquals("Fleetwood Mac", performers.get(1).getName());
        assertEquals("Bastille", performers.get(2).getName());
        assertEquals("Hugh Jackman", performers.get(3).getName());
        assertEquals("Keala Settle", performers.get(4).getName());
        assertEquals("KISS", performers.get(5).getName());
        assertEquals("Khalid", performers.get(6).getName());
        assertEquals("Little Mix", performers.get(7).getName());
        assertEquals("Robinson", performers.get(8).getName());
        assertEquals("Shawn Mendes", performers.get(9).getName());
        assertEquals("Ruel", performers.get(10).getName());

    }

    /**
     * Tests that a 401 error is returned when an incorrect username is supplied on login, and makes sure that
     * no authentication token is generated.
     */
    @Test
    public void testFailedLogin_IncorrectUsername() {
        // Log in
        Response loginResponse = login(client, "tesftuser", "pa55word");
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), loginResponse.getStatus());
        assertNull(loginResponse.getCookies().get("auth"));
    }

    /**
     * Tests that a 401 error is returned when an incorrect password is supplied on login, and makes sure that
     * no authentication token is generated.
     */
    @Test
    public void testFailedLogin_IncorrectPassword() {
        // Log in
        Response loginResponse = login(client, "testuser", "pa5word");
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), loginResponse.getStatus());
        assertNull(loginResponse.getCookies().get("auth"));
    }

    /**
     * tests that a 200 response is returned when the correct username and password are supplied on login, and that
     * a cookie named "auth" is generated.
     */
    @Test
    public void testSuccessfulLogin() {
        // Log in
        Response loginResponse = login(client, "testuser", "pa55word");
        assertEquals(Response.Status.OK.getStatusCode(), loginResponse.getStatus());
        Cookie authCookie = loginResponse.getCookies().get("auth");
        assertNotNull(authCookie.getValue());
        assertFalse(authCookie.getValue().isEmpty());
    }

    /**
     * Tests that a 401 error is returned when attempting to book while not logged in, and that no booking is actually
     * made.
     */
    @Test
    public void testAttemptUnauthorizedBooking() {

        List<String> seatLabels = Arrays.asList("C5", "C6");

        BookingRequestDTO bReq = new BookingRequestDTO(
                1, LocalDateTime.of(2020, 2, 15, 20, 0, 0), seatLabels);

        // Try to book
        Response response = client.target(WEB_SERVICE_URI + "/bookings")
                .request().post(Entity.json(bReq));

        // Make sure it didn't work
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());

        // Make sure no seats were booked
        List<SeatDTO> bookedSeats = client.target(WEB_SERVICE_URI + "/seats/2020-02-15T20:00:00?status=Booked")
                .request().get(new GenericType<List<SeatDTO>>() {
                });

        assertEquals(0, bookedSeats.size());

    }

    /**
     * Tests that a 201 response is returned when making a valid authorized booking, and that the requested seats now
     * are correctly reported as being booked.
     */
    @Test
    public void testMakeSuccessfulBooking() {

        // Log in
        login(client, "testuser", "pa55word");

        // Attempt booking
        Response response = attemptBooking(client, 1,
                LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                "C5", "C6");

        // Make sure it worked
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getLocation());

        // Make sure two seats were booked
        List<SeatDTO> bookedSeats = client.target(WEB_SERVICE_URI + "/seats/2020-02-15T20:00:00?status=Booked")
                .request().get(new GenericType<List<SeatDTO>>() {
                });

        assertEquals(2, bookedSeats.size());

    }

    /**
     * Tests that a 201 response is returned when making a valid authorized booking, and that the link returned
     * allows the user to correctly navigate to the new booking.
     */
    @Test
    public void testGetOwnBookingById() {

        // Log in
        login(client, "testuser", "pa55word");

        // Make booking
        Response bookingResponse = attemptBooking(client, 1,
                LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                "C5", "C6");

        // Get the booking
        BookingDTO booking = client.target(bookingResponse.getLocation()).request().get(BookingDTO.class);

        // Check details
        assertEquals(1L, booking.getConcertId());
        assertEquals(LocalDateTime.of(2020, 2, 15, 20, 0, 0), booking.getDate());
        assertEquals(2, booking.getSeats().size());
        booking.getSeats().sort(Comparator.comparing(SeatDTO::getLabel));
        assertEquals("C5", booking.getSeats().get(0).getLabel());
        assertEquals("C6", booking.getSeats().get(1).getLabel());

    }

    /**
     * Tests that a 403 error is returned when trying to access a booking of another user,
     * even if the correct id is known.
     */
    @Test
    public void testAttemptGetOthersBookingById() {

        // Log in
        login(client, "testuser", "pa55word");

        // Make booking
        Response bookingResponse = attemptBooking(client, 1,
                LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                "C5", "C6");

        // Get the booking
        Response response = client.target(bookingResponse.getLocation())
                .request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Log in as someone else
        login(client, "testuser2", "pa55word");

        // Attempt to get the booking - should fail
        response = client.target(bookingResponse.getLocation())
                .request().get();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());

    }

    /**
     * Test that multiple users are each able to access all of their own bookings. No user should be able to see
     * the bookings of any other user.
     */
    @Test
    public void testGetAllBookingsForUser() {

        // Log in as user 1
        login(client, "testuser", "pa55word");

        // Make bookings for user 1
        Response response = attemptBooking(client, 1,
                LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                "C5", "C6");
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        response = attemptBooking(client, 2,
                LocalDateTime.of(2019, 9, 14, 20, 0, 0),
                "A1", "A2");
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Log in as user 2
        Client user2Client = ClientBuilder.newClient();
        try {
            login(user2Client, "testuser2", "pa55word");

            // Make bookings for user 2
            response = attemptBooking(user2Client, 3,
                    LocalDateTime.of(2020, 1, 23, 20, 0, 0),
                    "C7", "C8");
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

            // Get user 1's bookings
            List<BookingDTO> user1Bookings = client.target(WEB_SERVICE_URI + "/bookings")
                    .request().get(new GenericType<List<BookingDTO>>() {
                    });

            // Make sure they're actually user 1's bookings.
            assertEquals(2, user1Bookings.size());

            user1Bookings.sort(Comparator.comparing(BookingDTO::getConcertId));
            assertEquals(LocalDateTime.of(2020, 2, 15, 20, 0, 0), user1Bookings.get(0).getDate());
            assertEquals(LocalDateTime.of(2019, 9, 14, 20, 0, 0), user1Bookings.get(1).getDate());

            // Get user 2's bookings
            List<BookingDTO> user2Bookings = user2Client.target(WEB_SERVICE_URI + "/bookings")
                    .request().get(new GenericType<List<BookingDTO>>() {
                    });

            // Make sure they're actually user 2's bookings.
            assertEquals(1, user2Bookings.size());
            assertEquals(LocalDateTime.of(2020, 1, 23, 20, 0, 0), user2Bookings.get(0).getDate());
        } finally {
            user2Client.close();
        }
    }

    /**
     * Tests that a 401 error is returned when trying to access any booking information while not authenticated.
     */
    @Test
    public void testAttemptGetAllBookingsWhenNotAuthenticated() {
        Response response = client.target(WEB_SERVICE_URI + "/bookings").request().get();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    /**
     * Tests that a 400 error is returned when trying to book seats for a date on which a given concert is not scheduled.
     */
    @Test
    public void testAttemptBookingWrongDate() {
        // Log in
        login(client, "testuser", "pa55word");

        // Attempt booking - should fail with bad request
        Response response = attemptBooking(client, 1,
                LocalDateTime.of(2020, 3, 15, 20, 0, 0),
                "C5", "C6");
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    /**
     * Tests that a 400 error is returned when trying to book seats for a nonexistent concert.
     */
    @Test
    public void testAttemptBookingIncorrectConcertId() {
        // Log in
        login(client, "testuser", "pa55word");

        // Attempt booking - should fail with bad request
        Response response = attemptBooking(client, 100,
                LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                "C5", "C6");
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    /**
     * Tests that a 403 error is returned when trying to book a set of seats, all of which have already been booked.
     * Also makes sure that the original booker of those seats retains those seats, and the new user does not.
     */
    @Test
    public void testAttemptDoubleBooking_SameSeats() {
        // Log in as user 1
        login(client, "testuser", "pa55word");

        // Make bookings for user 1
        Response response = attemptBooking(client, 1,
                LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                "C5", "C6");
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Log in as user 2
        Client user2Client = ClientBuilder.newClient();
        try {
            login(user2Client, "testuser2", "pa55word");

            // Try to make the same booking for user 2 - it should fail.
            response = attemptBooking(user2Client, 1,
                    LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                    "C5", "C6");
            assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
            assertNull(response.getLocation());

            // Make sure user 1 still has the booking, and user 2 does not.

            // Get user 1's bookings
            List<BookingDTO> user1Bookings = client.target(WEB_SERVICE_URI + "/bookings")
                    .request().get(new GenericType<List<BookingDTO>>() {
                    });
            assertEquals(1, user1Bookings.size());


            // Get user 2's bookings
            List<BookingDTO> user2Bookings = user2Client.target(WEB_SERVICE_URI + "/bookings")
                    .request().get(new GenericType<List<BookingDTO>>() {
                    });
            assertEquals(0, user2Bookings.size());
        } finally {
            user2Client.close();
        }
    }

    /**
     * Tests that a 403 error is returned when trying to book a set of seats, some of which have already been booked.
     * Also makes sure that the original user retains their booking, and that the second booking is not partially
     * completed (i.e. NO seats from the second booking request should be booked, even if some of them are available).
     */
    @Test
    public void testAttemptDoubleBooking_OverlappingSeats() {
        // Log in as user 1
        login(client, "testuser", "pa55word");

        // Make bookings for user 1
        Response response = attemptBooking(client, 1,
                LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                "C5", "C6");
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Log in as user 2
        Client user2Client = ClientBuilder.newClient();
        try {
            login(user2Client, "testuser2", "pa55word");

            // Try to make the same booking for user 2 - it should fail.
            response = attemptBooking(user2Client, 1,
                    LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                    "C6", "C7");
            assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
            assertNull(response.getLocation());

            // Make sure user 1 still has the booking, and user 2 does not.

            // Get user 1's bookings
            List<BookingDTO> user1Bookings = client.target(WEB_SERVICE_URI + "/bookings")
                    .request().get(new GenericType<List<BookingDTO>>() {
                    });
            assertEquals(1, user1Bookings.size());


            // Get user 2's bookings
            List<BookingDTO> user2Bookings = user2Client.target(WEB_SERVICE_URI + "/bookings")
                    .request().get(new GenericType<List<BookingDTO>>() {
                    });
            assertEquals(0, user2Bookings.size());
        } finally {
            user2Client.close();
        }

        // Make sure only seats C5 and C6 are booked. C7 shouldn't be booked.
        List<SeatDTO> bookedSeats = client.target(WEB_SERVICE_URI + "/seats/2020-02-15T20:00:00?status=Booked")
                .request().get(new GenericType<List<SeatDTO>>() {
                });

        assertEquals(2, bookedSeats.size());
        bookedSeats.sort(Comparator.comparing(SeatDTO::getLabel));
        assertEquals("C5", bookedSeats.get(0).getLabel());
        assertEquals("C6", bookedSeats.get(1).getLabel());

    }

    /**
     * Tests that the booked seats for a particular concert on a particular date can be queried.
     */
    @Test
    public void testGetBookedSeatsForDate() {
        // Log in
        login(client, "testuser", "pa55word");

        // Book some seats
        Response response = attemptBooking(client, 1,
                LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                "C5", "C6");

        // Get booked seats - should be C5 and C6
        List<SeatDTO> bookedSeats = client.target(WEB_SERVICE_URI + "/seats/2020-02-15T20:00:00?status=Booked")
                .request().get(new GenericType<List<SeatDTO>>() {
                });

        assertEquals(2, bookedSeats.size());
        bookedSeats.sort(Comparator.comparing(SeatDTO::getLabel));
        assertEquals("C5", bookedSeats.get(0).getLabel());
        assertEquals("C6", bookedSeats.get(1).getLabel());
    }

    /**
     * Tests that the unbooked seats for a particular concert on a particular date can be queried.
     */
    @Test
    public void testGetUnbookedSeatsForDate() {
        // Log in
        login(client, "testuser", "pa55word");

        // Book some seats
        Response response = attemptBooking(client, 1,
                LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                "C5", "C6");

        // Get unbooked seats - should be everything except C5 and C6
        List<SeatDTO> unbookedSeats = client.target(WEB_SERVICE_URI + "/seats/2020-02-15T20:00:00?status=Unbooked")
                .request().get(new GenericType<List<SeatDTO>>() {
                });

        assertEquals(118, unbookedSeats.size());
        unbookedSeats.sort(Comparator.comparing(SeatDTO::getLabel));
        for (SeatDTO seat : unbookedSeats) {
            if (seat.getLabel().equals("C5") || seat.getLabel().equals("C6")) {
                fail("Shouldn't have seen C5 or C6.");
            }
        }
    }

    /**
     * Tests that all seats for a particular concert on a particular date can be queried.
     */
    @Test
    public void testGetAllSeatsForDate() {
        // Log in
        login(client, "testuser", "pa55word");

        // Book some seats
        Response response = attemptBooking(client, 1,
                LocalDateTime.of(2020, 2, 15, 20, 0, 0),
                "C5", "C6");

        // Get hopefully all seats
        List<SeatDTO> seats = client.target(WEB_SERVICE_URI + "/seats/2020-02-15T20:00:00?status=Any")
                .request().get(new GenericType<List<SeatDTO>>() {
                });

        assertEquals(120, seats.size());
        List<String> labels = seats.stream().map(SeatDTO::getLabel).collect(Collectors.toList());
        for (char rowLabel = 'A'; rowLabel <= 'J'; rowLabel++) {
            for (int num = 1; num <= 12; num++) {
                String label = "" + rowLabel + num;
                assertTrue(labels.contains(label));
            }
        }
    }

    // Tests for publish / subscribe functions - uncomment when ready.
    // --------------------------------------------------------------------

//    /**
//     * Tests that a 401 error is returned when trying to make a subscription while not authenticated.
//     */
//    @Test
//    public void testUnauthorizedSubscription() throws InterruptedException, ExecutionException, TimeoutException {
//        // Attempt to subscribe
//        LocalDateTime date = LocalDateTime.of(2020, 2, 15, 20, 0, 0);
//        ConcertInfoSubscriptionDTO subInfo = new ConcertInfoSubscriptionDTO(1, date, 50);
//        Future<Response> future = client.target(WEB_SERVICE_URI + "/subscribe/concertInfo")
//                .request().async().post(Entity.json(subInfo));
//
//        // Wait for at most 1 second - the failure should be near-instant.
//        Response response = future.get(1, TimeUnit.SECONDS);
//
//        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
//    }
//
//    /**
//     * Tests that a 400 error is returned when trying to make a subscription for a nonexistent concert.
//     */
//    @Test
//    public void testBadSubscription_NonexistentConcert() throws InterruptedException, ExecutionException, TimeoutException {
//
//        testBadSubscription(100, LocalDateTime.of(2020, 2, 15, 20, 0, 0));
//    }
//
//    /**
//     * Tests that a 400 error is returned when trying to make a subscription for a nonexistent date.
//     */
//    @Test
//    public void testBadSubscription_NonexistentDate() throws InterruptedException, ExecutionException, TimeoutException {
//
//        testBadSubscription(1, LocalDateTime.of(2030, 2, 15, 20, 0, 0));
//    }
//
//    private void testBadSubscription(long concertId, LocalDateTime date) throws InterruptedException, ExecutionException, TimeoutException {
//        // Log in
//        login(client, "testuser", "pa55word");
//
//        // Attempt to subscribe
//        ConcertInfoSubscriptionDTO subInfo = new ConcertInfoSubscriptionDTO(concertId, date, 50);
//        Future<Response> future = client.target(WEB_SERVICE_URI + "/subscribe/concertInfo")
//                .request().async().post(Entity.json(subInfo));
//
//        // Wait for at most 1 second - the failure should be near-instant.
//        Response response = future.get(1, TimeUnit.SECONDS);
//
//        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
//    }
//
//    /**
//     * Tests that, when authenticated, a subscription can be made to /subscribe/concertInfo, and that subscribers are
//     * then notified when the conditions of their subscription are met. The subscribers won't be notified beforehand
//     * by mistake.
//     */
//    @Test
//    public void testSubscription() throws ExecutionException, InterruptedException {
//
//        // Log in
//        login(client, "testuser", "pa55word");
//
//        // Subscribe
//        LocalDateTime date = LocalDateTime.of(2020, 2, 15, 20, 0, 0);
//        ConcertInfoSubscriptionDTO subInfo = new ConcertInfoSubscriptionDTO(1, date, 50);
//        Future<ConcertInfoNotificationDTO> future = client.target(WEB_SERVICE_URI + "/subscribe/concertInfo")
//                .request().async().post(Entity.json(subInfo), ConcertInfoNotificationDTO.class);
//
//        Client user2Client = ClientBuilder.newClient();
//        try {
//            // Now, in the meantime, book all of rows A through D
//            login(user2Client, "testuser2", "pa55word");
//            attemptBooking(user2Client, 1, date, 'A', 'D');
//
//            // Now, wait two seconds to see if we received a sub response - we shouldn't have!
//            try {
//                future.get(2, TimeUnit.SECONDS);
//                fail(); // Shouldn't have worked.
//            } catch (TimeoutException e) {
//                // Good!
//            }
//
//            // Now, in the meantime, book all of rows E through G
//            attemptBooking(user2Client, 1, date, 'E', 'G');
//
//            // Now, wait to see if we've received a sub response - we SHOULD have!
//            try {
//                ConcertInfoNotificationDTO subResponse = future.get(2, TimeUnit.SECONDS);
//                // Should be 36 seats remaining.
//                assertEquals(36, subResponse.getNumSeatsRemaining());
//            } catch (TimeoutException e) {
//                fail("Future took too long to return - probable error.");
//            }
//
//        } finally {
//            user2Client.close();
//        }
//
//    }
//
//    /**
//     * Tests that, if subscribed to notifications about a particular concert / date, a user won't receive notifications
//     * about unrelated concerts / dates.
//     */
//    @Test
//    public void testSubscriptionForDifferentConcert() throws ExecutionException, InterruptedException {
//
//        // Log in
//        login(client, "testuser", "pa55word");
//
//        // Subscribe
//        LocalDateTime date = LocalDateTime.of(2020, 2, 15, 20, 0, 0);
//        ConcertInfoSubscriptionDTO subInfo = new ConcertInfoSubscriptionDTO(1, date, 50);
//        Future<ConcertInfoNotificationDTO> future = client.target(WEB_SERVICE_URI + "/subscribe/concertInfo")
//                .request().async().post(Entity.json(subInfo), ConcertInfoNotificationDTO.class);
//
//        // User 2 books out a whole theatre - but for a different concert.
//        LocalDateTime user2Date = LocalDateTime.of(2019, 9, 12, 20, 0, 0);
//        Client user2Client = ClientBuilder.newClient();
//        try {
//            // Make the booking
//            login(user2Client, "testuser2", "pa55word");
//            attemptBooking(user2Client, 2, user2Date, 'A', 'J');
//
//            // Now, wait two seconds to see if user 1 received a sub response - we shouldn't have (different concert / date).
//            try {
//                future.get(2, TimeUnit.SECONDS);
//                fail(); // Shouldn't have worked.
//            } catch (TimeoutException e) {
//                // Good!
//            }
//
//        } finally {
//            user2Client.close();
//        }
//
//    }

    // Helper methods
    // --------------------------------------------------------------------

    /**
     * Helper method to log us in.
     */
    private static Response login(Client client, String username, String password) {
        UserDTO creds = new UserDTO(username, password);
        return client.target(WEB_SERVICE_URI + "/login")
                .request().post(Entity.json(creds));
    }

    /**
     * Helper method - tries to book entire rows.
     */
    private static Response attemptBooking(Client client, long concertId, LocalDateTime date, char minRow, char maxRow) {
        List<String> toBook = new ArrayList<>();
        for (char row = minRow; row <= maxRow; row++) {
            for (int num = 1; num <= 12; num++) {
                toBook.add("" + row + num);
            }
        }
        return attemptBooking(client, concertId, date, toBook.toArray(new String[0]));
    }

    /**
     * Attempts a booking with the given details, and returns the server's response. Should already be logged in.
     */
    private static Response attemptBooking(Client client, long concertId, LocalDateTime date, String... seatLabels) {

        BookingRequestDTO bReq = new BookingRequestDTO(concertId, date, Arrays.asList(seatLabels));

        // Make booking
        return client.target(WEB_SERVICE_URI + "/bookings").request().post(Entity.json(bReq));
    }

}
