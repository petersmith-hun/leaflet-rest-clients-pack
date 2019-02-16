package hu.psprog.leaflet.rcp.hystrix.support.domain;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import org.springframework.web.context.request.RequestAttributes;

/**
 * {@link HystrixRequestVariableDefault} implementation holding {@link RequestAttributes}.
 *
 * @author Peter Smith
 */
public final class BridgeSupportHystrixRequestVariable {

    private static final HystrixRequestVariableDefault<RequestAttributes> REQUEST_ATTRIBUTES = new HystrixRequestVariableDefault<>();

    private BridgeSupportHystrixRequestVariable() {
    }

    /**
     * Retrieve singleton instance of {@link HystrixRequestVariableDefault} wrapping {@link RequestAttributes} on current thread.
     *
     * @return stored {@link RequestAttributes} wrapped as {@link HystrixRequestVariableDefault}
     */
    public static HystrixRequestVariableDefault<RequestAttributes> getInstance() {
        return REQUEST_ATTRIBUTES;
    }
}
