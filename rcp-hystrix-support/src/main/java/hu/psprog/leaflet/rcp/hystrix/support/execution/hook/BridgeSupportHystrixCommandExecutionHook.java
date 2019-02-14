package hu.psprog.leaflet.rcp.hystrix.support.execution.hook;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import hu.psprog.leaflet.rcp.hystrix.support.domain.BridgeSupportHystrixRequestVariable;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Custom {@link HystrixCommandExecutionHook} setting and resetting request context attributes on Hystrix execution threads.
 *
 * @author Peter Smith
 */
public class BridgeSupportHystrixCommandExecutionHook extends HystrixCommandExecutionHook {

    @Override
    public <T> void onExecutionStart(HystrixInvokable<T> commandInstance) {
        RequestContextHolder.setRequestAttributes(BridgeSupportHystrixRequestVariable.getInstance().get());
    }

    @Override
    public <T> T onEmit(HystrixInvokable<T> commandInstance, T value) {
        RequestContextHolder.resetRequestAttributes();
        return value;
    }

    @Override
    public <T> Exception onError(HystrixInvokable<T> commandInstance, HystrixRuntimeException.FailureType failureType, Exception e) {
        RequestContextHolder.resetRequestAttributes();
        return e;
    }
}
