package hu.psprog.leaflet.rcp.hystrix.support.config;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import com.netflix.hystrix.contrib.servopublisher.HystrixServoMetricsPublisher;
import com.netflix.hystrix.strategy.HystrixPlugins;
import hu.psprog.leaflet.rcp.hystrix.support.execution.hook.BridgeSupportHystrixCommandExecutionHook;
import hu.psprog.leaflet.rcp.hystrix.support.filter.HystrixContextFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hystrix support configuration for RCP/Bridge based REST clients.
 * Hystrix support can be enabled/disabled by changing value of {@code bridge.hystrix.enabled} switch.
 *
 * @author Peter Smith
 */
@Configuration
@ConditionalOnProperty(value = "bridge.hystrix.enabled", havingValue = "true")
public class RCPHystrixSupportConfiguration implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RCPHystrixSupportConfiguration.class);

    @Bean
    public HystrixContextFilter hystrixContextFilter() {
        LOGGER.info("Hystrix request context filter initialized");
        return new HystrixContextFilter();
    }

    @Bean
    public HystrixCommandAspect rcpHystrixCommandAspect() {
        LOGGER.info("Hystrix command aspect initialized - Hystrix wrapping will be available for requests via Bridge");
        return new HystrixCommandAspect();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initHystrix();
    }

    private void initHystrix() {
        HystrixPlugins.getInstance().registerCommandExecutionHook(new BridgeSupportHystrixCommandExecutionHook());
        HystrixPlugins.getInstance().registerMetricsPublisher(HystrixServoMetricsPublisher.getInstance());
    }
}
