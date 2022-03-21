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

public class ConcertSubscriptionIT {

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

    // Tests for publish / subscribe functions - uncomment when ready.
    // --------------------------------------------------------------------

    /**
     * Tests that a 401 error is returned when trying to make a subscription while not authenticated.
     */
    @Test
    public void testUnauthorizedSubscription() throws InterruptedException, ExecutionException, TimeoutException {
        // Attempt to subscribe
        LocalDateTime date = LocalDateTime.of(2020, 2, 15, 20, 0, 0);
        ConcertInfoSubscriptionDTO subInfo = new ConcertInfoSubscriptionDTO(1, date, 50);
        Future<Response> future = client.target(WEB_SERVICE_URI + "/subscribe/concertInfo")
                .request().async().post(Entity.json(subInfo));

        // Wait for at most 1 second - the failure should be near-instant.
        Response response = future.get(1, TimeUnit.SECONDS);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    /**
     * Tests that a 400 error is returned when trying to make a subscription for a nonexistent concert.
     */
    @Test
    public void testBadSubscription_NonexistentConcert() throws InterruptedException, ExecutionException, TimeoutException {

        testBadSubscription(100, LocalDateTime.of(2020, 2, 15, 20, 0, 0));
    }

    /**
     * Tests that a 400 error is returned when trying to make a subscription for a nonexistent date.
     */
    @Test
    public void testBadSubscription_NonexistentDate() throws InterruptedException, ExecutionException, TimeoutException {

        testBadSubscription(1, LocalDateTime.of(2030, 2, 15, 20, 0, 0));
    }

    private void testBadSubscription(long concertId, LocalDateTime date) throws InterruptedException, ExecutionException, TimeoutException {
        // Log in
        login(client, "testuser", "pa55word");

        // Attempt to subscribe
        ConcertInfoSubscriptionDTO subInfo = new ConcertInfoSubscriptionDTO(concertId, date, 50);
        Future<Response> future = client.target(WEB_SERVICE_URI + "/subscribe/concertInfo")
                .request().async().post(Entity.json(subInfo));

        // Wait for at most 1 second - the failure should be near-instant.
        Response response = future.get(1, TimeUnit.SECONDS);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    /**
     * Tests that, when authenticated, a subscription can be made to /subscribe/concertInfo, and that subscribers are
     * then notified when the conditions of their subscription are met. The subscribers won't be notified beforehand
     * by mistake.
     */
    @Test
    public void testSubscription() throws ExecutionException, InterruptedException {

        // Log in
        login(client, "testuser", "pa55word");

        // Subscribe
        LocalDateTime date = LocalDateTime.of(2020, 2, 15, 20, 0, 0);
        ConcertInfoSubscriptionDTO subInfo = new ConcertInfoSubscriptionDTO(1, date, 50);
        Future<ConcertInfoNotificationDTO> future = client.target(WEB_SERVICE_URI + "/subscribe/concertInfo")
                .request().async().post(Entity.json(subInfo), ConcertInfoNotificationDTO.class);

        Client user2Client = ClientBuilder.newClient();
        try {
            // Now, in the meantime, book all of rows A through D
            login(user2Client, "testuser2", "pa55word");
            attemptBooking(user2Client, 1, date, 'A', 'D');

            // Now, wait two seconds to see if we received a sub response - we shouldn't have!
            try {
                future.get(2, TimeUnit.SECONDS);
                fail(); // Shouldn't have worked.
            } catch (TimeoutException e) {
                // Good!
            }

            // Now, in the meantime, book all of rows E through G
            attemptBooking(user2Client, 1, date, 'E', 'G');

            // Now, wait to see if we've received a sub response - we SHOULD have!
            try {
                ConcertInfoNotificationDTO subResponse = future.get(2, TimeUnit.SECONDS);
                // Should be 36 seats remaining.
                assertEquals(36, subResponse.getNumSeatsRemaining());
            } catch (TimeoutException e) {
                fail("Future took too long to return - probable error.");
            }

        } finally {
            user2Client.close();
        }

    }

    /**
     * Tests that, if subscribed to notifications about a particular concert / date, a user won't receive notifications
     * about unrelated concerts / dates.
     */
    @Test
    public void testSubscriptionForDifferentConcert() throws ExecutionException, InterruptedException {

        // Log in
        login(client, "testuser", "pa55word");

        // Subscribe
        LocalDateTime date = LocalDateTime.of(2020, 2, 15, 20, 0, 0);
        ConcertInfoSubscriptionDTO subInfo = new ConcertInfoSubscriptionDTO(1, date, 50);
        Future<ConcertInfoNotificationDTO> future = client.target(WEB_SERVICE_URI + "/subscribe/concertInfo")
                .request().async().post(Entity.json(subInfo), ConcertInfoNotificationDTO.class);

        // User 2 books out a whole theatre - but for a different concert.
        LocalDateTime user2Date = LocalDateTime.of(2019, 9, 12, 20, 0, 0);
        Client user2Client = ClientBuilder.newClient();
        try {
            // Make the booking
            login(user2Client, "testuser2", "pa55word");
            attemptBooking(user2Client, 2, user2Date, 'A', 'J');

            // Now, wait two seconds to see if user 1 received a sub response - we shouldn't have (different concert / date).
            try {
                future.get(2, TimeUnit.SECONDS);
                fail(); // Shouldn't have worked.
            } catch (TimeoutException e) {
                // Good!
            }

        } finally {
            user2Client.close();
        }

    }

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
