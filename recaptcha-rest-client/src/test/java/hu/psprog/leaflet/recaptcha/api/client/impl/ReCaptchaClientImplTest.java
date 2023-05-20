package hu.psprog.leaflet.recaptcha.api.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.MultipartValuePattern;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import hu.psprog.leaflet.recaptcha.api.client.ReCaptchaClient;
import hu.psprog.leaflet.recaptcha.api.client.config.ReCaptchaServicePath;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaErrorCode;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static hu.psprog.leaflet.recaptcha.api.client.impl.ReCaptchaClientImplTest.ReCaptchaClientTestConfiguration.RE_CAPTCHA_CLIENT_INTEGRATION_TEST_PROFILE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link ReCaptchaClientImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        classes = ReCaptchaClientImplTest.ReCaptchaClientTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@WireMockTest(httpPort = 9999)
@ActiveProfiles(RE_CAPTCHA_CLIENT_INTEGRATION_TEST_PROFILE)
public class ReCaptchaClientImplTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private static final String FIELD_SECRET = "secret";
    private static final String FIELD_RESPONSE = "response";
    private static final String FIELD_REMOTE_IP = "remoteip";

    private static final String VALUE_SECRET = "secret-value";
    private static final String VALUE_RESPONSE = "response-value";
    private static final String VALUE_REMOTE_IP = "127.0.0.1";

    private static final ReCaptchaRequest RE_CAPTCHA_REQUEST = ReCaptchaRequest.getBuilder()
            .withSecret(VALUE_SECRET)
            .withResponse(VALUE_RESPONSE)
            .withRemoteIp(VALUE_REMOTE_IP)
            .build();
    private static final ReCaptchaResponse RE_CAPTCHA_RESPONSE_SUCCESSFUL = ReCaptchaResponse.getBuilder()
            .withSuccessful(true)
            .withHostname("hostname")
            .withChallengeTimeStamp(new Date())
            .build();
    private static final ReCaptchaResponse RE_CAPTCHA_RESPONSE_FAILED = ReCaptchaResponse.getBuilder()
            .withSuccessful(false)
            .withErrorCodes(Collections.singletonList(ReCaptchaErrorCode.TIMEOUT_OR_DUPLICATE))
            .build();

    @Autowired
    private ReCaptchaClient reCaptchaClient;

    @Test
    public void shouldValidateWithSuccessResponse() throws CommunicationFailureException, JsonProcessingException {

        // given
        givenThat(post(urlPathEqualTo(ReCaptchaServicePath.VERIFY.getURI()))
                .willReturn(prepareResponseDefinition(RE_CAPTCHA_RESPONSE_SUCCESSFUL)));

        // when
        ReCaptchaResponse result = reCaptchaClient.validate(RE_CAPTCHA_REQUEST);

        // then
        assertThat(result, equalTo(RE_CAPTCHA_RESPONSE_SUCCESSFUL));
        verifyRequest();
    }

    @Test
    public void shouldValidateWithFailureResponse() throws CommunicationFailureException, JsonProcessingException {

        // given
        givenThat(post(urlPathEqualTo(ReCaptchaServicePath.VERIFY.getURI()))
                .willReturn(prepareResponseDefinition(RE_CAPTCHA_RESPONSE_FAILED)));

        // when
        ReCaptchaResponse result = reCaptchaClient.validate(RE_CAPTCHA_REQUEST);

        // then
        assertThat(result, equalTo(RE_CAPTCHA_RESPONSE_FAILED));
        verifyRequest();
    }

    private ResponseDefinitionBuilder prepareResponseDefinition(ReCaptchaResponse response) throws JsonProcessingException {
        return ResponseDefinitionBuilder.responseDefinition()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                .withBody(OBJECT_MAPPER.writeValueAsString(prepareRawResponse(response)));
    }

    private void verifyRequest() {
        verify(postRequestedFor(urlPathEqualTo(ReCaptchaServicePath.VERIFY.getURI()))
                .withRequestBodyPart(prepareMultipartValue(FIELD_SECRET, VALUE_SECRET))
                .withRequestBodyPart(prepareMultipartValue(FIELD_RESPONSE, VALUE_RESPONSE))
                .withRequestBodyPart(prepareMultipartValue(FIELD_REMOTE_IP, VALUE_REMOTE_IP)));
    }

    private MultipartValuePattern prepareMultipartValue(String field, String value) {
        return aMultipart()
                .withName(field)
                .withBody(WireMock.equalTo(value))
                .build();
    }

    private Map<String, Object> prepareRawResponse(ReCaptchaResponse response) {

        Map<String, Object> rawResponse = new HashMap<>();
        rawResponse.put("success", response.successful());
        rawResponse.put("challenge_ts", response.challengeTimeStamp());
        rawResponse.put("hostname", response.hostname());
        rawResponse.put("error-codes", Optional.ofNullable(response.errorCodes())
                .map(reCaptchaErrorCodes -> reCaptchaErrorCodes.stream()
                        .map(ReCaptchaErrorCode::getRawErrorCode)
                        .collect(Collectors.toList()))
                .orElse(null));

        return rawResponse;
    }

    @Profile(RE_CAPTCHA_CLIENT_INTEGRATION_TEST_PROFILE)
    @Configuration
    @EnableConfigurationProperties
    @ComponentScan(basePackages = {
            "hu.psprog.leaflet.bridge",
            "hu.psprog.leaflet.recaptcha.api.client.impl"})
    public static class ReCaptchaClientTestConfiguration {

        static final String RE_CAPTCHA_CLIENT_INTEGRATION_TEST_PROFILE = "it";

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