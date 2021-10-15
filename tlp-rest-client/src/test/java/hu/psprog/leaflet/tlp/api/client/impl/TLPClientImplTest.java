package hu.psprog.leaflet.tlp.api.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import hu.psprog.leaflet.tlp.api.client.TLPClient;
import hu.psprog.leaflet.tlp.api.client.config.TLPPath;
import hu.psprog.leaflet.tlp.api.domain.LogEventPage;
import hu.psprog.leaflet.tlp.api.domain.LogRequest;
import hu.psprog.leaflet.tlp.api.domain.LoggingEvent;
import hu.psprog.leaflet.tlp.api.domain.OrderBy;
import hu.psprog.leaflet.tlp.api.domain.OrderDirection;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static hu.psprog.leaflet.tlp.api.client.impl.TLPClientImplTest.TLPClientTestConfiguration.TLP_CLIENT_INTEGRATION_TEST_PROFILE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link TLPClientImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        classes = TLPClientImplTest.TLPClientTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@WireMockTest(httpPort = 9999)
@ActiveProfiles(TLP_CLIENT_INTEGRATION_TEST_PROFILE)
public class TLPClientImplTest {

    private static final String QUERY_PARAM_PAGE = "page";
    private static final String QUERY_PARAM_LIMIT = "limit";
    private static final String QUERY_PARAM_ORDER_BY = "orderBy";
    private static final String QUERY_PARAM_ORDER_DIRECTION = "orderDirection";
    private static final String QUERY_PARAM_SOURCE = "source";
    private static final String QUERY_PARAM_LEVEL = "level";
    private static final String QUERY_PARAM_FROM = "from";
    private static final String QUERY_PARAM_TO = "to";
    private static final String QUERY_PARAM_CONTENT = "content";
    private static final String EXPECTED_FROM_DATE = "2018-04-24";
    private static final String EXPECTED_TO_DATE = "2018-04-26";

    private static final LogEventPage LOG_EVENT_PAGE = LogEventPage.getBuilder()
            .withEntitiesOnPage(Collections.singletonList(LoggingEvent.getBuilder()
                    .withLevel("INFO")
                    .build()))
            .withEntityCount(1L)
            .build();

    private static final LogRequest LOG_REQUEST = new LogRequest();
    private static final String TLQL_LOG_REQUEST = "search with conditions source='leaflet'";

    static {
        LOG_REQUEST.setContent("content");
        LOG_REQUEST.setFrom(prepareDate(24));
        LOG_REQUEST.setLevel("level");
        LOG_REQUEST.setLimit(10);
        LOG_REQUEST.setOrderBy(OrderBy.CONTENT);
        LOG_REQUEST.setOrderDirection(OrderDirection.ASC);
        LOG_REQUEST.setPage(2);
        LOG_REQUEST.setSource("source");
        LOG_REQUEST.setTo(prepareDate(26));
    }

    @Autowired
    private TLPClient tlpClient;

    @Test
    public void shouldGetLogs() throws CommunicationFailureException {

        // given
        givenThat(get(urlPathEqualTo(TLPPath.LOGS.getURI()))
                .willReturn(ResponseDefinitionBuilder.okForJson(LOG_EVENT_PAGE)));

        // when
        LogEventPage result = tlpClient.getLogs(LOG_REQUEST);

        // then
        assertThat(result, equalTo(LOG_EVENT_PAGE));
        verify(getRequestedFor(urlPathEqualTo(TLPPath.LOGS.getURI()))
                .withQueryParam(QUERY_PARAM_PAGE, new EqualToPattern(String.valueOf(LOG_REQUEST.getPage())))
                .withQueryParam(QUERY_PARAM_LIMIT, new EqualToPattern(String.valueOf(LOG_REQUEST.getLimit())))
                .withQueryParam(QUERY_PARAM_ORDER_BY, new EqualToPattern(LOG_REQUEST.getOrderBy().name()))
                .withQueryParam(QUERY_PARAM_ORDER_DIRECTION, new EqualToPattern(LOG_REQUEST.getOrderDirection().name()))
                .withQueryParam(QUERY_PARAM_SOURCE, new EqualToPattern(LOG_REQUEST.getSource()))
                .withQueryParam(QUERY_PARAM_LEVEL, new EqualToPattern(LOG_REQUEST.getLevel()))
                .withQueryParam(QUERY_PARAM_FROM, new EqualToPattern(EXPECTED_FROM_DATE))
                .withQueryParam(QUERY_PARAM_TO, new EqualToPattern(EXPECTED_TO_DATE))
                .withQueryParam(QUERY_PARAM_CONTENT, new EqualToPattern(LOG_REQUEST.getContent())));
    }

    @Test
    public void shouldGetLogsV2() throws CommunicationFailureException {

        // given
        givenThat(post(urlPathEqualTo(TLPPath.LOGS_V2.getURI()))
                .willReturn(ResponseDefinitionBuilder.okForJson(LOG_EVENT_PAGE)));

        // when
        LogEventPage result = tlpClient.getLogs(TLQL_LOG_REQUEST);

        // then
        assertThat(result, equalTo(LOG_EVENT_PAGE));
        verify(postRequestedFor(urlPathEqualTo(TLPPath.LOGS_V2.getURI()))
                .withRequestBody(new EqualToPattern(TLQL_LOG_REQUEST)));
    }

    @Profile(TLP_CLIENT_INTEGRATION_TEST_PROFILE)
    @Configuration
    @EnableConfigurationProperties
    @ComponentScan(basePackages = {
            "hu.psprog.leaflet.tlp.api.client.impl",
            "hu.psprog.leaflet.bridge"})
    public static class TLPClientTestConfiguration {

        static final String TLP_CLIENT_INTEGRATION_TEST_PROFILE = "it";

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

    private static Date prepareDate(int day) {
        return new Calendar.Builder()
                .setDate(2018, 3, day)
                .build()
                .getTime();
    }
}