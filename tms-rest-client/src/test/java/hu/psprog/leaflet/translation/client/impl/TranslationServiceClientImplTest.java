package hu.psprog.leaflet.translation.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ConflictingRequestException;
import hu.psprog.leaflet.bridge.client.exception.RequestProcessingFailureException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.client.exception.ValidationFailureException;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.api.domain.TranslationPackCreationRequest;
import hu.psprog.leaflet.translation.api.domain.TranslationPackMetaInfo;
import hu.psprog.leaflet.translation.client.MessageSourceClient;
import hu.psprog.leaflet.translation.client.TranslationServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static hu.psprog.leaflet.translation.client.impl.TranslationServiceClientImplTest.TranslationServiceClientTestConfiguration.TMS_CLIENT_INTEGRATION_TEST_PROFILE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link TranslationServiceClientImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        classes = TranslationServiceClientImplTest.TranslationServiceClientTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@WireMockTest(httpPort = 9999)
@ActiveProfiles(TMS_CLIENT_INTEGRATION_TEST_PROFILE)
public class TranslationServiceClientImplTest {

    private static final UUID PACK_ID = UUID.randomUUID();
    private static final String PACK_NAME = "pack1";
    private static final TranslationPackCreationRequest TRANSLATION_PACK_CREATION_REQUEST = TranslationPackCreationRequest.getBuilder().build();
    private static final TranslationPack TRANSLATION_PACK = TranslationPack.getBuilder()
            .withId(PACK_ID)
            .withPackName(PACK_NAME)
            .build();
    private static final TranslationPackMetaInfo TRANSLATION_PACK_META_INFO = TranslationPackMetaInfo.getBuilder()
            .withId(PACK_ID)
            .withPackName(PACK_NAME)
            .build();
    private static final EqualToPattern PACKS_QUERY_PARAMETER_VALUE_PATTERN = new EqualToPattern(PACK_NAME);
    private static final String QUERY_PARAMETER_PACKS = "packs";
    private static final String PATH_TRANSLATIONS = "/translations";
    private static final String PATH_TRANSLATIONS_ID = "/translations/" + PACK_ID;
    private static final String PATH_TRANSLATIONS_STATUS = "/translations/" + PACK_ID + "/status";
    private static final Map<String, String> ERROR_MESSAGE_BODY = prepareErrorMessage();
    private static final Map<String, Object> VALIDATION_ERROR_MESSAGE_BODY = prepareValidationErrorMessage();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String MEDIA_TYPE_APPLICATION_JSON = "application/json";
    private static final int HTTP_STATUS_CREATED = 201;
    private static final int HTTP_STATUS_NO_CONTENT = 204;
    private static final int HTTP_STATUS_BAD_REQUEST = 400;
    private static final int HTTP_STATUS_NOT_FOUND = 404;
    private static final int HTTP_STATUS_CONFLICT = 409;
    private static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer token";
    private static final StringValuePattern VALUE_PATTERN_BEARER_TOKEN = WireMock.equalTo(BEARER_TOKEN);

    static {
        TRANSLATION_PACK_CREATION_REQUEST.setPackName(PACK_NAME);
        TRANSLATION_PACK_CREATION_REQUEST.setLocale(Locale.ENGLISH);
        TRANSLATION_PACK_CREATION_REQUEST.setDefinitions(prepareErrorMessage());
    }

    @Autowired
    private TranslationServiceClient translationServiceClient;

    @Autowired
    private MessageSourceClient messageSourceClient;

    @Test
    public void shouldRetrievePacks() throws CommunicationFailureException {

        // given
        givenThat(get(urlPathEqualTo(PATH_TRANSLATIONS))
                .withQueryParam(QUERY_PARAMETER_PACKS, PACKS_QUERY_PARAMETER_VALUE_PATTERN)
                .willReturn(ResponseDefinitionBuilder.okForJson(Collections.singleton(TRANSLATION_PACK))));

        // when
        Set<TranslationPack> result = messageSourceClient.retrievePacks(Collections.singletonList(PACK_NAME));

        // then
        verify(getRequestedFor(urlPathEqualTo(PATH_TRANSLATIONS))
                .withQueryParam(QUERY_PARAMETER_PACKS, PACKS_QUERY_PARAMETER_VALUE_PATTERN));
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.contains(TRANSLATION_PACK), is(true));
    }

    @Test
    public void shouldListStoredPacks() throws CommunicationFailureException {

        // given
        givenThat(get(PATH_TRANSLATIONS)
                .willReturn(ResponseDefinitionBuilder.okForJson(Collections.singletonList(TRANSLATION_PACK_META_INFO))));

        // when
        List<TranslationPackMetaInfo> result = translationServiceClient.listStoredPacks();

        // then
        verify(getRequestedFor(urlPathEqualTo(PATH_TRANSLATIONS))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.contains(TRANSLATION_PACK_META_INFO), is(true));
    }

    @Test
    public void shouldGetPackByID() throws CommunicationFailureException {

        // given
        givenThat(get(PATH_TRANSLATIONS_ID)
                .willReturn(ResponseDefinitionBuilder.okForJson(TRANSLATION_PACK)));

        // when
        TranslationPack result = translationServiceClient.getPackByID(PACK_ID);

        // then
        verify(getRequestedFor(urlPathEqualTo(PATH_TRANSLATIONS_ID)));
        assertThat(result, notNullValue());
        assertThat(result, equalTo(TRANSLATION_PACK));
    }

    @Test
    public void shouldGetPackByIDThrowException() throws JsonProcessingException {

        // given
        givenThat(get(PATH_TRANSLATIONS_ID)
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON)
                        .withStatus(HTTP_STATUS_NOT_FOUND)
                        .withBody(OBJECT_MAPPER.writeValueAsString(ERROR_MESSAGE_BODY))));

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> translationServiceClient.getPackByID(PACK_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldCreateTranslationPack() throws JsonProcessingException, CommunicationFailureException {

        // given
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(TRANSLATION_PACK_CREATION_REQUEST));
        givenThat(post(PATH_TRANSLATIONS)
                .withRequestBody(requestBody)
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON)
                        .withStatus(HTTP_STATUS_CREATED)
                        .withBody(OBJECT_MAPPER.writeValueAsString(TRANSLATION_PACK))));

        // when
        TranslationPack result = translationServiceClient.createTranslationPack(TRANSLATION_PACK_CREATION_REQUEST);

        // then
        verify(postRequestedFor(urlPathEqualTo(PATH_TRANSLATIONS))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
        assertThat(result, equalTo(TRANSLATION_PACK));
    }

    @Test
    public void shouldCreateTranslationPackWithValidationError() throws JsonProcessingException {

        // given
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(TRANSLATION_PACK_CREATION_REQUEST));
        givenThat(post(PATH_TRANSLATIONS)
                .withRequestBody(requestBody)
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON)
                        .withStatus(HTTP_STATUS_BAD_REQUEST)
                        .withBody(OBJECT_MAPPER.writeValueAsString(VALIDATION_ERROR_MESSAGE_BODY))));

        // when
        Assertions.assertThrows(ValidationFailureException.class, () -> translationServiceClient.createTranslationPack(TRANSLATION_PACK_CREATION_REQUEST));

        // then
        // exception expected
    }

    @Test
    public void shouldCreateTranslationPackWithCreationError() throws JsonProcessingException {

        // given
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(TRANSLATION_PACK_CREATION_REQUEST));
        givenThat(post(PATH_TRANSLATIONS)
                .withRequestBody(requestBody)
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON)
                        .withStatus(HTTP_STATUS_CONFLICT)
                        .withBody(OBJECT_MAPPER.writeValueAsString(ERROR_MESSAGE_BODY))));

        // when
        Assertions.assertThrows(ConflictingRequestException.class, () -> translationServiceClient.createTranslationPack(TRANSLATION_PACK_CREATION_REQUEST));

        // then
        // exception expected
    }

    @Test
    public void shouldCreateTranslationPackWithUnknownError() throws JsonProcessingException {

        // given
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(TRANSLATION_PACK_CREATION_REQUEST));
        givenThat(post(PATH_TRANSLATIONS)
                .withRequestBody(requestBody)
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON)
                        .withStatus(HTTP_STATUS_INTERNAL_SERVER_ERROR)
                        .withBody(OBJECT_MAPPER.writeValueAsString(ERROR_MESSAGE_BODY))));

        // when
        Assertions.assertThrows(RequestProcessingFailureException.class, () -> translationServiceClient.createTranslationPack(TRANSLATION_PACK_CREATION_REQUEST));

        // then
        // exception expected
    }

    @Test
    public void shouldChangePackStatus() throws JsonProcessingException, CommunicationFailureException {

        // given
        givenThat(put(PATH_TRANSLATIONS_STATUS)
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON)
                        .withStatus(HTTP_STATUS_CREATED)
                        .withBody(OBJECT_MAPPER.writeValueAsString(TRANSLATION_PACK))));

        // when
        TranslationPack result = translationServiceClient.changePackStatus(PACK_ID);

        // then
        verify(putRequestedFor(urlPathEqualTo(PATH_TRANSLATIONS_STATUS))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
        assertThat(result, equalTo(TRANSLATION_PACK));
    }

    @Test
    public void shouldDeletePack() throws CommunicationFailureException {

        // given
        givenThat(delete(PATH_TRANSLATIONS_ID)
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON)
                        .withStatus(HTTP_STATUS_NO_CONTENT)));

        // when
        translationServiceClient.deleteTranslationPack(PACK_ID);

        // then
        verify(deleteRequestedFor(urlPathEqualTo(PATH_TRANSLATIONS_ID)));
    }

    private static Map<String, String> prepareErrorMessage() {

        Map<String, String> content = new HashMap<>();
        content.put("message", "error occurred");

        return content;
    }

    private static Map<String, Object> prepareValidationErrorMessage() {

        Map<String, String> validation = new HashMap<>();
        validation.put("field", "field1");
        validation.put("message", "violation message");

        Map<String, Object> content = new HashMap<>();
        content.put("validation", Collections.singletonList(validation));

        return content;
    }

    @Profile(TMS_CLIENT_INTEGRATION_TEST_PROFILE)
    @Configuration
    @EnableConfigurationProperties
    @ComponentScan(basePackages = {
            "hu.psprog.leaflet.translation.client.impl",
            "hu.psprog.leaflet.bridge"})
    public static class TranslationServiceClientTestConfiguration {

        static final String TMS_CLIENT_INTEGRATION_TEST_PROFILE = "it";

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