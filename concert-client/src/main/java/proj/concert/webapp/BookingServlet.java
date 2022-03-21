package proj.concert.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proj.concert.common.dto.ConcertDTO;
import proj.concert.common.dto.SeatDTO;
import proj.concert.webapp.util.AuthUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookingServlet extends HttpServlet {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // If not signed in, redirect back to Concerts.
        Cookie authCookie = AuthUtil.getAuthCookie(req);
        if (authCookie == null) {
            LOGGER.info("doGet(): Not logged in - redirecting to /Concerts");
            resp.sendRedirect("./Concerts"); // TODO Some ? parameter so that we display a message on page load?
            return;
        }

        // Figure out which concert and date we're trying to book
        long concertId = Long.parseLong(req.getParameter("concertId"));
        LocalDateTime date = LocalDateTime.parse(req.getParameter("date"), FORMATTER);

        Client wsClient = ClientBuilder.newClient();
        try {
            // Grab the concert
            ConcertDTO concert = wsClient.target(Config.WEB_SERVICE_URI + "/concerts/" + concertId)
                    .request().get(ConcertDTO.class);


            // If the date is invalid for that concert, get outta here.
            if (!concert.getDates().contains(date)) {
                LOGGER.warn("doGet(): Date invalid for concert - redirecting to /Concerts");
                resp.sendRedirect("./Concerts"); // TODO Some ? parameter so that we display a message on page load?
                return;
            }

            // Go get the already-booked seats from the web service, so we can mark them as such on the app.
            List<SeatDTO> bookedSeats = wsClient.target(Config.WEB_SERVICE_URI + "/seats/" + FORMATTER.format(date) + "?status=Booked")
                    .request().get(new GenericType<List<SeatDTO>>() {
                    });
            LOGGER.info("doGet(): bookedSeats size = " + bookedSeats.size());

//             TEST
//            bookedSeats = Arrays.asList(new SeatDTO("A1", null), new SeatDTO("G11", null), new SeatDTO("G12", null));

            List<String> bookedSeatLabels = bookedSeats.stream().map(seat -> "\"" + seat.getLabel() + "\"").collect(Collectors.toList());
            String seatLabelString = String.join(", ", bookedSeatLabels);
            LOGGER.info("doGet(): seatLabelString = " + seatLabelString);

            // Add data to req attrs to be displayed in JSP
            req.setAttribute("concert", concert);
            req.setAttribute("concertDateString", FORMATTER.format(date));
            req.setAttribute("bookedSeatLabels", seatLabelString);

            // Go to JSP
            req.getRequestDispatcher("/WEB-INF/jsp/booking.jsp").forward(req, resp);

        } finally {
            wsClient.close();
        }
    }
}
