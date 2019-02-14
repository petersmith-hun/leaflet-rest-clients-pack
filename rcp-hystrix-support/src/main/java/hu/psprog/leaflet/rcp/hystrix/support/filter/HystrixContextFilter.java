package hu.psprog.leaflet.rcp.hystrix.support.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import hu.psprog.leaflet.rcp.hystrix.support.domain.BridgeSupportHystrixRequestVariable;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter implementation to initialize {@link HystrixRequestContext} and copy request attributes to Hystrix context.
 * Used by Hystrix-wrapped Bridge clients for accessing {@link HttpServletRequest} attributes.
 *
 * @author Peter Smith
 */
public class HystrixContextFilter extends OncePerRequestFilter implements Ordered {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            BridgeSupportHystrixRequestVariable.getInstance().set(RequestContextHolder.getRequestAttributes());
            filterChain.doFilter(request, response);
        } finally {
            context.shutdown();
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 200;
    }
}
