package hu.psprog.leaflet.rcp.hystrix.support.filter.mockapp;

import hu.psprog.leaflet.rcp.hystrix.support.filter.AbstractHystrixContextFilterBaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Test controller for simulating Hystrix-controlled client-server communication.
 * Calls should be originated from the "source" endpoint, which calls the "target" endpoint via a Hystrix-wrapped test client.
 *
 * @author Peter Smith
 */
@RestController
public class HystrixTestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixTestController.class);

    private HystrixTestClient hystrixTestClient;

    @Autowired
    public HystrixTestController(HystrixTestClient hystrixTestClient) {
        this.hystrixTestClient = hystrixTestClient;
    }

    @GetMapping("/hystrix/source")
    public ResponseEntity<String> testHystrixRequestContextSource(HttpServletRequest request) {

        LOGGER.info("Hystrix source test endpoint called on thread [{}]", Thread.currentThread().getName());
        request.setAttribute(AbstractHystrixContextFilterBaseTest.REQUEST_PARAMETER_NAME, AbstractHystrixContextFilterBaseTest.PASSED_REQUEST_PARAMETER_VALUE);

        ResponseEntity<String> response = hystrixTestClient.hystrixWrappedCall();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/hystrix/target")
    public ResponseEntity<String> testHystrixRequestContextTarget(HttpServletRequest request) {
        LOGGER.info("Hystrix target test endpoint called on thread [{}]", Thread.currentThread().getName());
        return ResponseEntity.ok(request.getParameter(AbstractHystrixContextFilterBaseTest.QUERY_ATTRIBUTE));
    }
}
