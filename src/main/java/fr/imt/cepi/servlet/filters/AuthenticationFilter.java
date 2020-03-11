package fr.imt.cepi.servlet.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {

    private final Logger logger = Logger.getLogger(AuthenticationFilter.class);

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        logger.info("Initialisation du filtre d'Authentification");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        logger.info("URI : " + uri);

        HttpSession session = req.getSession(false);

        if (session == null && !(uri.endsWith("json") || uri.endsWith("png") || uri.endsWith("html") || uri.endsWith("Login") || uri.endsWith("Register"))) {
            logger.error("Unauthorized access request");
            res.sendRedirect("login.html");
        } else {
            // poursuit par le prochain filtre
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
    }

}
