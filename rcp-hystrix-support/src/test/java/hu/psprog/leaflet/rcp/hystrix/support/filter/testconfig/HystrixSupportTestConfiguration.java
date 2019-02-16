package hu.psprog.leaflet.rcp.hystrix.support.filter.testconfig;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import hu.psprog.leaflet.rcp.hystrix.support.config.RCPHystrixSupportConfiguration;
import hu.psprog.leaflet.rcp.hystrix.support.filter.mockapp.HystrixTestSupportFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

/**
 * Hystrix support integration test configuration.
 *
 * @author Peter Smith
 */
@TestConfiguration
@Import(RCPHystrixSupportConfiguration.class)
public class HystrixSupportTestConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixSupportTestConfiguration.class);

    @Bean
    public HystrixTestSupportFilter hystrixTestSupportFilter() {
        return new HystrixTestSupportFilter();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Profile("hystrix-broken")
    public HystrixCommandAspect hystrixCommandAspect() {
        LOGGER.info("Initialized Hystrix command aspect without context support filter");
        return new HystrixCommandAspect();
    }
}
