package hu.psprog.leaflet.rcp.hystrix.support.domain;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import org.springframework.security.core.context.SecurityContext;

/**
 * {@link HystrixRequestVariableDefault} implementation holding {@link SecurityContext}.
 *
 * @author Peter Smith
 */
public final class SecurityContextHystrixRequestVariable {

    private static final HystrixRequestVariableDefault<SecurityContext> SECURITY_CONTEXT = new HystrixRequestVariableDefault<>();

    private SecurityContextHystrixRequestVariable() {
    }

    /**
     * Retrieve singleton instance of {@link HystrixRequestVariableDefault} wrapping {@link SecurityContext} on current thread.
     *
     * @return stored {@link SecurityContext} wrapped as {@link HystrixRequestVariableDefault}
     */
    public static HystrixRequestVariableDefault<SecurityContext> getInstance() {
        return SECURITY_CONTEXT;
    }
}
