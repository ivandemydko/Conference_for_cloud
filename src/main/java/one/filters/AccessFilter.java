package one.filters;

import one.persistence.entity.User;
//import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AccessFilter implements Filter {
   // private Logger logger = Logger.getLogger(AccessFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        httpRequest.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher = servletRequest.getServletContext()
                .getRequestDispatcher("/welcome/loginPage");
        User user = (User) httpRequest.getSession().getAttribute("user");
        String uri = httpRequest.getRequestURI();
        String query = httpRequest.getQueryString();
        Matcher matcher;
        Pattern pattern;

        if (user == null) {
            pattern = Pattern.compile("^/admin|^/moderator|^/speaker|^/user");
            matcher = pattern.matcher(uri);
            if (matcher.find()) {
//                logger.warn("Unknown tried to access to protected resources:" + (uri + "?" + query)
//                        + " Remote host: " + httpRequest.getRemoteHost()
//                        + " Remote address: " + httpRequest.getRemoteAddr());
                httpRequest.getSession().invalidate();
                dispatcher.forward(servletRequest, servletResponse);
                return;
            }
        } else {
            boolean isAccessViolated = false;
            switch (user.getPosition().getPosition()) {
                case "User":
                    pattern = Pattern.compile("^/admin|^/moderator|^/speaker");
                    matcher = pattern.matcher(uri);
                    if (matcher.find())
                        isAccessViolated = true;
                    break;
                case "Speaker":
                    pattern = Pattern.compile("^/admin|^/moderator|^/user");
                    matcher = pattern.matcher(uri);
                    if (matcher.find())
                        isAccessViolated = true;
                    break;
                case "Moderator":
                    pattern = Pattern.compile("^/admin|^/speaker|^/user");
                    matcher = pattern.matcher(uri);
                    if (matcher.find())
                        isAccessViolated = true;
                    break;
                case "Admin":
                    pattern = Pattern.compile("^/moderator|^/speaker|^/user");
                    matcher = pattern.matcher(uri);
                    if (matcher.find())
                        isAccessViolated = true;
                    break;
            }
            if (isAccessViolated) {
//                logger.warn("user " + user.getEmail() + " tried to access to protected resources:" + (uri + "?" + query)
//                        + " Remote host: " + httpRequest.getRemoteHost()
//                        + " Remote address: " + httpRequest.getRemoteAddr());
                httpRequest.getSession().invalidate();
                dispatcher.forward(servletRequest, servletResponse);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
