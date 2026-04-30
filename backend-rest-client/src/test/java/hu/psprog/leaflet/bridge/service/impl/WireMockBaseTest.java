package hu.psprog.leaflet.bridge.service.impl;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.PaginationDataModel;
import hu.psprog.leaflet.api.rest.response.common.SEODataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.time.ZoneId;
import java.time.ZoneOffset;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;

/**
 * Wiremock configuration base class for service tests.
 *
 * @author Peter Smith
 */
public abstract class WireMockBaseTest {

    static final String AUTHORIZATION_HEADER = "Authorization";
    static final StringValuePattern VALUE_PATTERN_BEARER_TOKEN = equalTo("Bearer token");
    static final String CONTENT = "content";
    static final String LIMIT = "limit";
    static final String ORDER_BY = "orderBy";
    static final String ORDER_DIRECTION = "orderDirection";
    static final ZoneId ZONE_OFFSET = ZoneOffset.UTC;

    static final JsonMapper JSON_MAPPER = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();

    ResponseDefinitionBuilder jsonResponse(Object responseObject) {
        return jsonResponse(responseObject, 200);
    }

    ResponseDefinitionBuilder jsonResponse(Object responseObject, int httpStatus) {
        return ResponseDefinitionBuilder.responseDefinition()
                .withBody(JSON_MAPPER.writeValueAsString(responseObject))
                .withHeader("Content-Type", "application/json")
                .withStatus(httpStatus);
    }

    String prepareURI(String template, Object ... values) {

        String preparedString = template;
        for (Object value : values) {
            preparedString = preparedString.replaceFirst("(\\{[a-zA-Z]+})", String.valueOf(value));
        }

        return preparedString;
    }

    <T extends BaseBodyDataModel> WrapperBodyDataModel<T> prepareWrappedListDataModel(T body) {
        return WrapperBodyDataModel.<T>getBuilder()
                .withBody(body)
                .withSeo(SEODataModel.getBuilder()
                        .withMetaDescription("SEO Desc")
                        .withMetaKeywords("SEO Keywords")
                        .withMetaTitle("SEO Title")
                        .withPageTitle("Page title")
                        .build())
                .withPagination(PaginationDataModel.getBuilder()
                        .withEntityCount(10)
                        .withEntityCountOnPage(5)
                        .withHasNext(true)
                        .withHasPrevious(false)
                        .withFirst(true)
                        .withLast(false)
                        .withPageCount(2)
                        .withPageNumber(1)
                        .build())
                .build();
    }
}
