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
 * Integration test testing misconfigured Hystrix support.
 * Hystrix-wrapped REST client will fail to access {@link HttpServletRequest} instance.
 *
 * @author Peter Smith
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = HystrixSupportTestConfiguration.class)
@ActiveProfiles("hystrix-broken")
@DirtiesContext
public class HystrixBrokenHystrixContextFilterTest extends AbstractHystrixContextFilterBaseTest {

    @Test
    public void shouldClientThrowExceptionWhenAccessingRequest() {
        runTest(false);
    }
}
