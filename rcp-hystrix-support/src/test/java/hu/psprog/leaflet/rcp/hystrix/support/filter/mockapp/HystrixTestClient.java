package hu.psprog.leaflet.rcp.hystrix.support.filter.mockapp;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import hu.psprog.leaflet.rcp.hystrix.support.filter.AbstractHystrixContextFilterBaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * Test client for making Hystrix-wrapped REST calls.
 *
 * @author Peter Smith
 */
@Component
public class HystrixTestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixTestClient.class);

    private HttpServletRequest request;
    private RestTemplate restTemplate;

    @Autowired
    public HystrixTestClient(HttpServletRequest request, RestTemplate restTemplate) {
        this.request = request;
        this.restTemplate = restTemplate;
    }

    /**
     * Returns passed parameter value extracted from {@link HttpServletRequest}.
     * Hystrix being correctly configured should allow accessing the current {@link HttpServletRequest} instance.
     *
     * @return passed parameter value extracted from {@link HttpServletRequest}
     */
    @HystrixCommand
    public ResponseEntity<String> hystrixWrappedCall() {

        LOGGER.info("Calling target test endpoint on thread [{}]", Thread.currentThread().getName());
        String passedValue = String.valueOf(request.getAttribute(AbstractHystrixContextFilterBaseTest.REQUEST_PARAMETER_NAME));
        String url = String.format(AbstractHystrixContextFilterBaseTest.URL_TARGET_TEMPLATE, passedValue);

        return restTemplate.getForEntity(url, String.class);
    }
}
