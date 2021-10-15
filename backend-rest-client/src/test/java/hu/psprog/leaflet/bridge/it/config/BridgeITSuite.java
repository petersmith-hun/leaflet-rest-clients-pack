package hu.psprog.leaflet.bridge.it.config;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for Bridge integration test suites.
 *
 * @author Peter Smith
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
        classes = LeafletBridgeITContextConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@WireMockTest(httpPort = 9999)
@ActiveProfiles(LeafletBridgeITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public @interface BridgeITSuite {
}
