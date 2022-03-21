package proj.concert.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proj.concert.common.dto.UserDTO;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        LOGGER.info("LoginServlet: From user : " + username + ", " + password);

        UserDTO user = new UserDTO(username, password);

        Client wsClient = ClientBuilder.newClient();
        try {
            Response wsResponse = wsClient.target(Config.WEB_SERVICE_URI + "/login")
                    .request().post(Entity.json(user));

            LOGGER.info("LoginServlet: Status from web service : " + wsResponse.getStatus());

            // Essentially forward the cookie onto the browser.
            if (wsResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                NewCookie newCookie = wsResponse.getCookies().get("auth");

                Cookie browserCookie = new Cookie("auth", newCookie.getValue());
                browserCookie.setPath("/");
                resp.addCookie(browserCookie);
            }

            resp.sendRedirect("./Concerts");
        }
        finally {
            wsClient.close();
        }

    }
}
