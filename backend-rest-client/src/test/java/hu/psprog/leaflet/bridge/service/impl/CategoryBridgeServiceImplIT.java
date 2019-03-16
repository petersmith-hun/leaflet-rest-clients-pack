package hu.psprog.leaflet.bridge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.api.rest.request.category.CategoryCreateRequestModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.it.config.BridgeITSuite;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.ZonedDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link CategoryBridgeServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@BridgeITSuite
public class CategoryBridgeServiceImplIT extends WireMockBaseTest {

    @Autowired
    private CategoryBridgeService categoryBridgeService;

    @Test
    public void shouldGetAllCategories() throws CommunicationFailureException, JsonProcessingException {

        // given
        CategoryListDataModel categoryListDataModel = prepareCategoryListDataModel();
        givenThat(get(LeafletPath.CATEGORIES.getURI())
                .willReturn(jsonResponse(categoryListDataModel)));

        // when
        CategoryListDataModel result = categoryBridgeService.getAllCategories();

        // then
        verify(getRequestedFor(urlEqualTo(LeafletPath.CATEGORIES.getURI()))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
        assertThat(result, equalTo(categoryListDataModel));

    }

    @Test
    public void shouldGetPublicCategories() throws CommunicationFailureException, JsonProcessingException {

        // given
        CategoryListDataModel categoryListDataModel = prepareCategoryListDataModel();
        givenThat(get(LeafletPath.CATEGORIES_PUBLIC.getURI())
                .willReturn(jsonResponse(categoryListDataModel)));

        // when
        CategoryListDataModel result = categoryBridgeService.getPublicCategories();

        // then
        verify(getRequestedFor(urlEqualTo(LeafletPath.CATEGORIES_PUBLIC.getURI())));
        assertThat(result, equalTo(categoryListDataModel));

    }

    @Test
    public void shouldGetCategory() throws CommunicationFailureException, JsonProcessingException {

        // given
        Long categoryID = 1L;
        CategoryDataModel categoryDataModel = prepareCategoryDataModel(categoryID);
        String uri = prepareURI(LeafletPath.CATEGORIES_BY_ID.getURI(), categoryID);
        givenThat(get(uri)
                .willReturn(jsonResponse(categoryDataModel)));

        // when
        CategoryDataModel result = categoryBridgeService.getCategory(categoryID);

        // then
        verify(getRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
        assertThat(result, equalTo(categoryDataModel));
    }

    @Test
    public void shouldCreateCategory() throws CommunicationFailureException, JsonProcessingException {

        // given
        CategoryDataModel categoryDataModel = prepareCategoryDataModel(1L);
        CategoryCreateRequestModel categoryCreateRequestModel = prepareCategoryCreateRequestModel();
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(categoryCreateRequestModel));
        givenThat(post(LeafletPath.CATEGORIES.getURI())
                .withRequestBody(requestBody)
                .willReturn(jsonResponse(categoryDataModel, 201)));

        // when
        CategoryDataModel result = categoryBridgeService.createCategory(categoryCreateRequestModel);

        // then
        verify(postRequestedFor(urlEqualTo(LeafletPath.CATEGORIES.getURI()))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
        assertThat(result, equalTo(categoryDataModel));
    }

    @Test
    public void shouldUpdateCategory() throws JsonProcessingException, CommunicationFailureException {

        // given
        Long categoryID = 1L;
        CategoryDataModel categoryDataModel = prepareCategoryDataModel(1L);
        CategoryCreateRequestModel categoryCreateRequestModel = prepareCategoryCreateRequestModel();
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(categoryCreateRequestModel));
        String uri = prepareURI(LeafletPath.CATEGORIES_BY_ID.getURI(), categoryID);
        givenThat(put(uri)
                .withRequestBody(requestBody)
                .willReturn(jsonResponse(categoryDataModel, 201)));

        // when
        CategoryDataModel result = categoryBridgeService.updateCategory(categoryID, categoryCreateRequestModel);

        // then
        verify(putRequestedFor(urlEqualTo(uri))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
        assertThat(result, equalTo(categoryDataModel));
    }

    @Test
    public void shouldChangeStatus() throws CommunicationFailureException, JsonProcessingException {

        // given
        Long categoryID = 1L;
        String uri = prepareURI(LeafletPath.CATEGORIES_STATUS.getURI(), categoryID);
        CategoryDataModel categoryDataModel = prepareCategoryDataModel(1L);
        givenThat(put(uri)
                .willReturn(jsonResponse(categoryDataModel, 201)));

        // when
        CategoryDataModel result = categoryBridgeService.changeStatus(categoryID);

        // then
        verify(putRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
        assertThat(result, equalTo(categoryDataModel));
    }

    @Test
    public void shouldDeleteCategory() throws CommunicationFailureException {

        // given
        Long categoryID = 1L;
        String uri = prepareURI(LeafletPath.CATEGORIES_BY_ID.getURI(), categoryID);
        givenThat(delete(uri).willReturn(ok()));

        // then
        categoryBridgeService.deleteCategory(categoryID);

        // then
        verify(deleteRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    private CategoryCreateRequestModel prepareCategoryCreateRequestModel() {
        CategoryCreateRequestModel categoryCreateRequestModel = new CategoryCreateRequestModel();
        categoryCreateRequestModel.setTitle("Category #1");
        categoryCreateRequestModel.setDescription("Description for category");
        return categoryCreateRequestModel;
    }

    private CategoryListDataModel prepareCategoryListDataModel() {
        return CategoryListDataModel.getBuilder()
                .withItem(prepareCategoryDataModel(1L))
                .withItem(prepareCategoryDataModel(2L))
                .build();
    }

    private CategoryDataModel prepareCategoryDataModel(Long categoryID) {
        return CategoryDataModel.getBuilder()
                .withID(categoryID)
                .withTitle("Category #" + categoryID.toString())
                .withDescription("Description for category")
                .withCreated(ZonedDateTime.now(ZONE_ID))
                .withLastModified(ZonedDateTime.now(ZONE_ID))
                .build();
    }
}
