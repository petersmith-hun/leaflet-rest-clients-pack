package hu.psprog.leaflet.failover.api.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import hu.psprog.leaflet.failover.api.client.FailoverClient;
import hu.psprog.leaflet.failover.api.client.config.FailoverPath;
import hu.psprog.leaflet.failover.api.domain.FailoverStatus;
import hu.psprog.leaflet.failover.api.domain.StatusResponse;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static hu.psprog.leaflet.failover.api.client.impl.FailoverClientImplTest.FailoverClientTestConfiguration.FAILOVER_CLIENT_INTEGRATION_TEST_PROFILE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link FailoverClientImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(FAILOVER_CLIENT_INTEGRATION_TEST_PROFILE)
@ContextConfiguration(
        classes = FailoverClientImplTest.FailoverClientTestConfiguration.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public class FailoverClientImplTest {

    private static final StatusResponse STATUS_RESPONSE = StatusResponse.getBuilder()
            .withStatus(FailoverStatus.STANDBY)
            .build();

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(options().port(9999));

    @Rule
    public WireMockClassRule wireMockInstanceRule = wireMockRule;

    @Autowired
    private FailoverClient failoverClient;

    @Test
    public void shouldGetFailoverStatus() throws CommunicationFailureException {

        // given
        givenThat(get(FailoverPath.STATUS.getURI())
                .willReturn(ResponseDefinitionBuilder.okForJson(STATUS_RESPONSE)));

        // when
        StatusResponse result = failoverClient.getFailoverStatus();

        // then
        assertThat(result, equalTo(STATUS_RESPONSE));
        verify(getRequestedFor(urlPathEqualTo(FailoverPath.STATUS.getURI())));
    }

    @Profile(FAILOVER_CLIENT_INTEGRATION_TEST_PROFILE)
    @Configuration
    @EnableConfigurationProperties
    @ComponentScan(basePackages = {
            "hu.psprog.leaflet.failover.api.client.impl",
            "hu.psprog.leaflet.bridge"})
    public static class FailoverClientTestConfiguration {

        static final String FAILOVER_CLIENT_INTEGRATION_TEST_PROFILE = "it";

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public RequestAuthentication requestAuthenticationStub() {
            return () -> {
                Map<String, String> auth = new HashMap<>();
                auth.put("Authorization", "Bearer token");
                return auth;
            };
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
}