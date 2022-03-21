package proj.concert.webapp;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Delete any "auth" cookies from the client, by overwriting it with an expired cookie.
        Cookie eatenCookie = new Cookie("auth", "");
        eatenCookie.setPath("/");
        eatenCookie.setMaxAge(0);
        resp.addCookie(eatenCookie);

        resp.sendRedirect("./Concerts");
    }
}
