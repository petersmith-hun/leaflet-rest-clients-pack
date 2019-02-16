package hu.psprog.leaflet.rcp.hystrix.support.filter.mockapp;

import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RequestContext somehow remains empty during integration tests with SpringBootTest.
 * This filter should forcibly populate the RequestContext.
 *
 * @author Peter Smith
 */
public class HystrixTestSupportFilter extends OncePerRequestFilter implements Ordered {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        RequestContextHolder.setRequestAttributes(new ServletWebRequest(request));
        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
