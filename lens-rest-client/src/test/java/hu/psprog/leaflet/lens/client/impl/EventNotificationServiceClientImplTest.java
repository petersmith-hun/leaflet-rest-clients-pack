package hu.psprog.leaflet.lens.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.client.exception.ValidationFailureException;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import hu.psprog.leaflet.lens.api.domain.MailContent;
import hu.psprog.leaflet.lens.api.domain.MailRequestWrapper;
import hu.psprog.leaflet.lens.client.EventNotificationServiceClient;
import hu.psprog.leaflet.lens.client.impl.testdata.LENSClientScenario;
import hu.psprog.leaflet.lens.client.impl.testdata.Scenario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static hu.psprog.leaflet.lens.client.impl.EventNotificationServiceClientImplTest.LENSClientTestConfiguration.LENS_CLIENT_IT_TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Integration tests for {@link EventNotificationServiceClientImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        classes = EventNotificationServiceClientImplTest.LENSClientTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@WireMockTest(httpPort = 9999)
@ActiveProfiles(LENS_CLIENT_IT_TEST_PROFILE)
class EventNotificationServiceClientImplTest {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer token";
    private static final StringValuePattern VALUE_PATTERN_BEARER_TOKEN = WireMock.equalTo(BEARER_TOKEN);

    @Autowired
    private EventNotificationServiceClient eventNotificationServiceClient;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @EnumSource(LENSClientScenario.class)
    public void shouldRequestMailNotification(Scenario scenario) throws CommunicationFailureException {

        // given
        MailRequestWrapper<? extends MailContent> requestWrapper = scenario.request();

        givenThat(post(urlPathEqualTo(scenario.expectedPath()))
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withStatus(202)));

        // when
        eventNotificationServiceClient.requestMailNotification(requestWrapper);

        // then
        verify(postRequestedFor(urlPathEqualTo(scenario.expectedPath()))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN)
                .withRequestBody(createPattern(requestWrapper)));
    }

    @Test
    public void shouldRequestMailNotificationThrowValidationFailureExceptionFor400Status() {

        // given
        Scenario scenario = LENSClientScenario.SYSTEM_STARTUP;

        givenThat(post(urlPathEqualTo(scenario.expectedPath()))
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withStatus(400)));

        // when
        assertThrows(ValidationFailureException.class,
                () -> eventNotificationServiceClient.requestMailNotification(scenario.request()));

        // then
        // exception expected
    }

    @Test
    public void shouldRequestMailNotificationThrowResourceNotFoundFor404Status() {

        // given
        Scenario scenario = LENSClientScenario.SYSTEM_STARTUP;

        givenThat(post(urlPathEqualTo(scenario.expectedPath()))
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withStatus(404)));

        // when
        assertThrows(ResourceNotFoundException.class,
                () -> eventNotificationServiceClient.requestMailNotification(scenario.request()));

        // then
        // exception expected
    }

    private EqualToJsonPattern createPattern(MailRequestWrapper<? extends MailContent> mailRequestWrapper) {

        try {
            String json = objectMapper.writeValueAsString(mailRequestWrapper);
            return new EqualToJsonPattern(json, false, false);
        } catch (JsonProcessingException e) {
            fail();
            return null;
        }
    }

    @Profile(LENS_CLIENT_IT_TEST_PROFILE)
    @Configuration
    @EnableAutoConfiguration
    @ComponentScan(basePackages = {
            "hu.psprog.leaflet.lens.client.impl",
            "hu.psprog.leaflet.bridge"
    })
    static class LENSClientTestConfiguration {

        static final String LENS_CLIENT_IT_TEST_PROFILE = "it";

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public RequestAuthentication requestAuthenticationStub() {
            return () -> {
                Map<String, String> auth = new HashMap<>();
                auth.put(AUTHORIZATION_HEADER, BEARER_TOKEN);
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
