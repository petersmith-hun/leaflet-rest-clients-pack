package hu.psprog.leaflet.rcp.hystrix.support.filter;

import com.netflix.hystrix.Hystrix;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Base test for Hystrix support integration tests.
 * Test case triggers the simulation by calling "source" endpoint.
 *
 * @author Peter Smith
 */
public abstract class AbstractHystrixContextFilterBaseTest {

    public static final String REQUEST_PARAMETER_NAME = "request-parameter-to-be-passed";
    public static final String PASSED_REQUEST_PARAMETER_VALUE = "passed-value";
    public static final String QUERY_ATTRIBUTE = "pass";
    public static final String CREDENTIAL_ATTRIBUTE = "credential";
    public static final String URL_SOURCE = "http://localhost:9998/hystrix/source";
    public static final String URL_TARGET_TEMPLATE = "http://localhost:9998/hystrix/target?" + QUERY_ATTRIBUTE + "=%s&" + CREDENTIAL_ATTRIBUTE + "=%s";
    public static final String HYSTRIX_USER = "hystrix-user";
    public static final String HYSTRIX_PASSWORD = "hystrix-password";
    public static final String RESPONSE_BODY_PATTERN = "%s/%s";
    private static final String EXPECTED_RESPONSE = String.format(RESPONSE_BODY_PATTERN, PASSED_REQUEST_PARAMETER_VALUE, HYSTRIX_PASSWORD);

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void setup() {
        Hystrix.reset();
    }

    void runTest(boolean assertSuccess) {

        // when
        ResponseEntity<String> result = restTemplate.getForEntity(URL_SOURCE, String.class);

        // then
        if (assertSuccess) {
            assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
            assertThat(result.getBody(), equalTo(EXPECTED_RESPONSE));
        } else {
            assertThat(result.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}