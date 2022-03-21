package proj.concert.webapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proj.concert.webapp.LoginServlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class AuthUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUtil.class);

    public static Cookie getAuthCookie(HttpServletRequest req) {
        if (req.getCookies() != null) {
            Optional<Cookie> maybeCookie = Arrays.asList(req.getCookies()).stream().filter(c -> c.getName().equals("auth")).findFirst();
            if (maybeCookie.isPresent()) {
                return maybeCookie.get();
            }
        }
        return null;
    }

    public static void setSignedInStatus(HttpServletRequest req) {
        Cookie authCookie = getAuthCookie(req);
        if (authCookie == null) {
            LOGGER.info("setSignedInStatus(): No auth cookie from browser :(");
            req.setAttribute("signedIn", false);
        }
        else {
            LOGGER.info("setSignedInStatus(): Auth cookie from browser: " + authCookie.getValue());
            req.setAttribute("signedIn", true);
        }
    }

}
