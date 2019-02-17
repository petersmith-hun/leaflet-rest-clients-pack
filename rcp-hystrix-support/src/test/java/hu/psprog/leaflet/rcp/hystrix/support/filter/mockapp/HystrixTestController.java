package hu.psprog.leaflet.rcp.hystrix.support.filter.mockapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static hu.psprog.leaflet.rcp.hystrix.support.filter.AbstractHystrixContextFilterBaseTest.CREDENTIAL_ATTRIBUTE;
import static hu.psprog.leaflet.rcp.hystrix.support.filter.AbstractHystrixContextFilterBaseTest.PASSED_REQUEST_PARAMETER_VALUE;
import static hu.psprog.leaflet.rcp.hystrix.support.filter.AbstractHystrixContextFilterBaseTest.QUERY_ATTRIBUTE;
import static hu.psprog.leaflet.rcp.hystrix.support.filter.AbstractHystrixContextFilterBaseTest.REQUEST_PARAMETER_NAME;
import static hu.psprog.leaflet.rcp.hystrix.support.filter.AbstractHystrixContextFilterBaseTest.RESPONSE_BODY_PATTERN;

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
        request.setAttribute(REQUEST_PARAMETER_NAME, PASSED_REQUEST_PARAMETER_VALUE);

        ResponseEntity<String> response = hystrixTestClient.hystrixWrappedCall();

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/hystrix/target")
    public ResponseEntity<String> testHystrixRequestContextTarget(HttpServletRequest request) {
        LOGGER.info("Hystrix target test endpoint called on thread [{}]", Thread.currentThread().getName());
        String responseBody = String.format(RESPONSE_BODY_PATTERN, request.getParameter(QUERY_ATTRIBUTE), request.getParameter(CREDENTIAL_ATTRIBUTE));
        return ResponseEntity.ok(responseBody);
    }
}
