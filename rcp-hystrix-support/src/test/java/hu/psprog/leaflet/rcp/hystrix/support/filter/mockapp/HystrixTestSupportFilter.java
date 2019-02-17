package hu.psprog.leaflet.rcp.hystrix.support.filter.mockapp;

import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static hu.psprog.leaflet.rcp.hystrix.support.filter.AbstractHystrixContextFilterBaseTest.HYSTRIX_PASSWORD;
import static hu.psprog.leaflet.rcp.hystrix.support.filter.AbstractHystrixContextFilterBaseTest.HYSTRIX_USER;

/**
 * RequestContext somehow remains empty during integration tests with SpringBootTest.
 * This filter should forcibly populate the RequestContext.
 *
 * @author Peter Smith
 */
public class HystrixTestSupportFilter extends OncePerRequestFilter implements Ordered {

    protected static final String PATH_HYSTRIX_SOURCE = "/hystrix/source";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (PATH_HYSTRIX_SOURCE.equals(request.getServletPath())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(HYSTRIX_USER, HYSTRIX_PASSWORD);
            SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
        }

        RequestContextHolder.setRequestAttributes(new ServletWebRequest(request));
        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
