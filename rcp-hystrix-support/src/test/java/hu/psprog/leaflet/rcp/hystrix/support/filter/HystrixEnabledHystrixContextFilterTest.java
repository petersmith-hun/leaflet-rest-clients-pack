package hu.psprog.leaflet.rcp.hystrix.support.filter;

import hu.psprog.leaflet.rcp.hystrix.support.filter.testconfig.HystrixSupportTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletRequest;

/**
 * Integration test testing enabled Hystrix support.
 * Hystrix-wrapped REST client should be able to access {@link HttpServletRequest} instance.
 *
 * @author Peter Smith
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = HystrixSupportTestConfiguration.class)
@ActiveProfiles("hystrix-enabled")
@DirtiesContext
public class HystrixEnabledHystrixContextFilterTest extends AbstractHystrixContextFilterBaseTest {

    @Test
    public void shouldFilterPassRequestContextToHystrixExecutionThread() {
        runTest(true);
    }
}
