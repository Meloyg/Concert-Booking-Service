package proj.concert.webapp;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proj.concert.webapp.util.AuthUtil;

import java.io.IOException;

public class ConcertsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest reqt, HttpServletResponse resp) throws ServletException, IOException {

        AuthUtil.setSignedInStatus(reqt);

        reqt.getRequestDispatcher("/WEB-INF/jsp/concerts.jsp").forward(reqt, resp);

    }
}
