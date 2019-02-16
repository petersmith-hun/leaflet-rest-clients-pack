package hu.psprog.leaflet.rcp.hystrix.support.filter;

import hu.psprog.leaflet.rcp.hystrix.support.filter.testconfig.HystrixSupportTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

/**
 * Integration test testing disabled Hystrix support.
 * REST client won't be wrapped by Hystrix, therefore it should access {@link HttpServletRequest} instance in the same thread.
 *
 * @author Peter Smith
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = HystrixSupportTestConfiguration.class)
@ActiveProfiles("hystrix-disabled")
@DirtiesContext
public class HystrixDisabledHystrixContextFilterTest extends AbstractHystrixContextFilterBaseTest {

    @Test
    public void shouldRequestContextBePassedOnSameThread() {
        runTest(true);
    }
}
