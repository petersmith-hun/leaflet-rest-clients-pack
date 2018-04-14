package hu.psprog.leaflet.bridge.it.config;

import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import org.mockito.Mockito;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static hu.psprog.leaflet.bridge.it.config.LeafletBridgeITContextConfig.COMPONENT_SCAN_PACKAGE;
import static hu.psprog.leaflet.bridge.it.config.LeafletBridgeITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE;

/**
 * Context configuration for integration tests.
 *
 * @author Peter Smith
 */
@Profile(INTEGRATION_TEST_CONFIG_PROFILE)
@Configuration
@ComponentScan(basePackages = COMPONENT_SCAN_PACKAGE)
@EnableConfigurationProperties
public class LeafletBridgeITContextConfig {

    public static final String INTEGRATION_TEST_CONFIG_PROFILE = "it";
    static final String COMPONENT_SCAN_PACKAGE = "hu.psprog.leaflet.bridge";
    private static final String WIRE_MOCK_SERVER_ADDRESS = "http://localhost:9999";

    private RequestAuthentication requestAuthentication = () -> {
        Map<String, String> auth = new HashMap<>();
        auth.put("Authorization", "Bearer token");
        return auth;
    };

    @Bean
    public RequestAuthentication requestAuthenticationStub() {
        return requestAuthentication;
    }

    @Bean
    public HttpServletRequest httpServletRequestStub() {
        return Mockito.mock(HttpServletRequest.class);
    }

    @Bean
    public HttpServletResponse httpServletResponseStub() {
        return Mockito.mock(HttpServletResponse.class);
    }
}
