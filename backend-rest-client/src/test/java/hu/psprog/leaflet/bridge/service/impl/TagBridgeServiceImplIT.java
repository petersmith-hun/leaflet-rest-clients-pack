package hu.psprog.leaflet.bridge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.api.rest.request.tag.TagAssignmentRequestModel;
import hu.psprog.leaflet.api.rest.request.tag.TagCreateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.it.config.BridgeITSuite;
import hu.psprog.leaflet.bridge.service.TagBridgeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link TagBridgeServiceImpl}.
 *
 * @author Peter Smith
 */
@BridgeITSuite
public class TagBridgeServiceImplIT extends WireMockBaseTest {

    @Autowired
    private TagBridgeService tagBridgeService;

    @Test
    public void shouldGetAllTags() throws CommunicationFailureException {

        // given
        TagListDataModel tagListDataModel = prepareTagListDataModel();
        givenThat(get(LeafletPath.TAGS.getURI())
                .willReturn(ResponseDefinitionBuilder.okForJson(tagListDataModel)));

        // when
        TagListDataModel result = tagBridgeService.getAllTags();

        // then
        assertThat(result, equalTo(tagListDataModel));
        verify(getRequestedFor(urlEqualTo(LeafletPath.TAGS.getURI()))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldGetPublicTags() throws CommunicationFailureException {

        // given
        TagListDataModel tagListDataModel = prepareTagListDataModel();
        WrapperBodyDataModel<TagListDataModel> wrappedTagListDataModel = prepareWrappedListDataModel(tagListDataModel);
        givenThat(get(LeafletPath.TAGS_PUBLIC.getURI())
                .willReturn(ResponseDefinitionBuilder.okForJson(wrappedTagListDataModel)));

        // when
        WrapperBodyDataModel<TagListDataModel> result = tagBridgeService.getAllPublicTags();

        // then
        assertThat(result, equalTo(wrappedTagListDataModel));
        verify(getRequestedFor(urlEqualTo(LeafletPath.TAGS_PUBLIC.getURI())));
    }

    @Test
    public void shouldGetTag() throws CommunicationFailureException {

        // given
        TagDataModel tagDataModel = prepareTagDataModel(1L);
        Long tagID = 1L;
        String uri = prepareURI(LeafletPath.TAGS_BY_ID.getURI(), tagID);
        givenThat(get(uri)
                .willReturn(ResponseDefinitionBuilder.okForJson(tagDataModel)));

        // when
        TagDataModel result = tagBridgeService.getTag(tagID);

        // then
        assertThat(result, equalTo(tagDataModel));
        verify(getRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldCreateTag() throws JsonProcessingException, CommunicationFailureException {

        // given
        TagCreateRequestModel tagCreateRequestModel = prepareTagCreateRequestModel();
        TagDataModel tagDataModel = prepareTagDataModel(1L);
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(tagCreateRequestModel));
        givenThat(post(LeafletPath.TAGS.getURI())
                .withRequestBody(requestBody)
                .willReturn(ResponseDefinitionBuilder.okForJson(tagDataModel)));

        // when
        TagDataModel result = tagBridgeService.createTag(tagCreateRequestModel);

        // then
        assertThat(result, equalTo(tagDataModel));
        verify(postRequestedFor(urlEqualTo(LeafletPath.TAGS.getURI()))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldUpdateTag() throws JsonProcessingException, CommunicationFailureException {

        // given
        TagCreateRequestModel tagCreateRequestModel = prepareTagCreateRequestModel();
        TagDataModel tagDataModel = prepareTagDataModel(1L);
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(tagCreateRequestModel));
        Long tagID = 1L;
        String uri = prepareURI(LeafletPath.TAGS_BY_ID.getURI(), tagID);
        givenThat(put(uri)
                .withRequestBody(requestBody)
                .willReturn(ResponseDefinitionBuilder.okForJson(tagDataModel)));

        // when
        TagDataModel result = tagBridgeService.updateTag(tagID, tagCreateRequestModel);

        // then
        assertThat(result, equalTo(tagDataModel));
        verify(putRequestedFor(urlEqualTo(uri))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldDeleteTag() throws CommunicationFailureException {

        // given
        Long tagID = 1L;
        String uri = prepareURI(LeafletPath.TAGS_BY_ID.getURI(), tagID);
        givenThat(delete(uri));

        // when
        tagBridgeService.deleteTag(tagID);

        // then
        verify(deleteRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldChangeStatus() throws CommunicationFailureException {

        // given
        TagDataModel tagDataModel = prepareTagDataModel(1L);
        Long tagID = 1L;
        String uri = prepareURI(LeafletPath.TAGS_STATUS.getURI(), tagID);
        givenThat(put(uri)
                .willReturn(ResponseDefinitionBuilder.okForJson(tagDataModel)));

        // when
        TagDataModel result = tagBridgeService.changeStatus(tagID);

        // then
        assertThat(result, equalTo(tagDataModel));
        verify(putRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldAttachTag() throws JsonProcessingException, CommunicationFailureException {

        // given
        TagAssignmentRequestModel tagAssignmentRequestModel = prepareTagAssignmentRequestModel();
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(tagAssignmentRequestModel));
        givenThat(post(LeafletPath.TAGS_ASSIGN.getURI())
                .withRequestBody(requestBody));

        // when
        tagBridgeService.attachTag(tagAssignmentRequestModel);

        // then
        verify(postRequestedFor(urlEqualTo(LeafletPath.TAGS_ASSIGN.getURI()))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldDetachTag() throws JsonProcessingException, CommunicationFailureException {

        // given
        TagAssignmentRequestModel tagAssignmentRequestModel = prepareTagAssignmentRequestModel();
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(tagAssignmentRequestModel));
        givenThat(put(LeafletPath.TAGS_ASSIGN.getURI())
                .withRequestBody(requestBody));

        // when
        tagBridgeService.detachTag(tagAssignmentRequestModel);

        // then
        verify(putRequestedFor(urlEqualTo(LeafletPath.TAGS_ASSIGN.getURI()))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    private TagAssignmentRequestModel prepareTagAssignmentRequestModel() {
        TagAssignmentRequestModel tagAssignmentRequestModel = new TagAssignmentRequestModel();
        tagAssignmentRequestModel.setEntryID(10L);
        tagAssignmentRequestModel.setTagID(20L);
        return tagAssignmentRequestModel;
    }

    private TagCreateRequestModel prepareTagCreateRequestModel() {
        TagCreateRequestModel tagCreateRequestModel = new TagCreateRequestModel();
        tagCreateRequestModel.setName("tag");
        return tagCreateRequestModel;
    }

    private TagListDataModel prepareTagListDataModel() {
        return TagListDataModel.getBuilder()
                .withTags(List.of(
                        prepareTagDataModel(1L),
                        prepareTagDataModel(2L)
                ))
                .build();
    }

    private TagDataModel prepareTagDataModel(Long tagID) {
        return TagDataModel.getBuilder()
                .withId(tagID)
                .withName("tag #" + tagID)
                .build();
    }
}
