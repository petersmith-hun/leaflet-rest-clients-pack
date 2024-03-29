package hu.psprog.leaflet.bridge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.comment.CommentSearchParameters;
import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.it.config.BridgeITSuite;
import hu.psprog.leaflet.bridge.service.CommentBridgeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
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
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static hu.psprog.leaflet.bridge.client.domain.BridgeConstants.X_CAPTCHA_RESPONSE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link CommentBridgeServiceImpl}.
 *
 * @author Peter Smith
 */
@BridgeITSuite
public class CommentBridgeServiceImplIT extends WireMockBaseTest {

    private static final String RECAPTCHA_TOKEN = "recaptcha-token";

    @Autowired
    private CommentBridgeService commentBridgeService;

    @Test
    public void shouldGetPageOfPublicCommentsForEntry() throws CommunicationFailureException, JsonProcessingException {

        // given
        String entryLink = "entry-link";
        int page = 1;
        int limit = 10;
        OrderBy.Comment orderBy = OrderBy.Comment.CREATED;
        OrderDirection orderDirection = OrderDirection.ASC;
        String uri = prepareURI(LeafletPath.COMMENTS_PUBLIC_PAGE_BY_ENTRY.getURI(), entryLink, page);
        WrapperBodyDataModel<CommentListDataModel> wrappedCommentListDataModel = prepareWrappedListDataModel(prepareCommentListDataModel());
        givenThat(get(urlPathEqualTo(uri))
                .willReturn(jsonResponse(wrappedCommentListDataModel)));

        // when
        WrapperBodyDataModel<CommentListDataModel> result = commentBridgeService.getPageOfPublicCommentsForEntry(entryLink, page, limit, orderBy, orderDirection);

        // then
        assertThat(result, equalTo(wrappedCommentListDataModel));
        verify(getRequestedFor(urlPathEqualTo(uri))
                .withQueryParam(LIMIT, WireMock.equalTo(String.valueOf(limit)))
                .withQueryParam(ORDER_BY, WireMock.equalTo(orderBy.name()))
                .withQueryParam(ORDER_DIRECTION, WireMock.equalTo(String.valueOf(orderDirection))));
    }

    @Test
    public void shouldGetPageOfCommentsForEntry() throws CommunicationFailureException, JsonProcessingException {

        // given
        Long entryID = 1L;
        int page = 1;
        int limit = 10;
        OrderBy.Comment orderBy = OrderBy.Comment.CREATED;
        OrderDirection orderDirection = OrderDirection.ASC;
        String uri = prepareURI(LeafletPath.COMMENTS_ALL_PAGE_BY_ENTRY.getURI(), entryID, page);
        WrapperBodyDataModel<CommentListDataModel> wrappedCommentListDataModel = prepareWrappedListDataModel(prepareCommentListDataModel());
        givenThat(get(urlPathEqualTo(uri))
                .willReturn(jsonResponse(wrappedCommentListDataModel)));

        // when
        WrapperBodyDataModel<CommentListDataModel> result = commentBridgeService.getPageOfCommentsForEntry(entryID, page, limit, orderBy, orderDirection);

        // then
        assertThat(result, equalTo(wrappedCommentListDataModel));
        verify(getRequestedFor(urlPathEqualTo(uri))
                .withQueryParam(LIMIT, WireMock.equalTo(String.valueOf(limit)))
                .withQueryParam(ORDER_BY, WireMock.equalTo(orderBy.name()))
                .withQueryParam(ORDER_DIRECTION, WireMock.equalTo(String.valueOf(orderDirection)))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldGetPageOfCommentsForUser() throws CommunicationFailureException, JsonProcessingException {

        // given
        Long userID = 1L;
        int page = 1;
        int limit = 10;
        OrderBy.Comment orderBy = OrderBy.Comment.CREATED;
        OrderDirection orderDirection = OrderDirection.ASC;
        String uri = prepareURI(LeafletPath.COMMENTS_ALL_PAGE_BY_USER.getURI(), userID, page);
        WrapperBodyDataModel<ExtendedCommentListDataModel> wrappedCommentListDataModel = prepareWrappedListDataModel(prepareExtendedCommentListDataModel());
        givenThat(get(urlPathEqualTo(uri))
                .willReturn(jsonResponse(wrappedCommentListDataModel)));

        // when
        WrapperBodyDataModel<ExtendedCommentListDataModel> result = commentBridgeService.getPageOfCommentsForUser(userID, page, limit, orderBy, orderDirection);

        // then
        assertThat(result, equalTo(wrappedCommentListDataModel));
        verify(getRequestedFor(urlPathEqualTo(uri))
                .withQueryParam(LIMIT, WireMock.equalTo(String.valueOf(limit)))
                .withQueryParam(ORDER_BY, WireMock.equalTo(orderBy.name()))
                .withQueryParam(ORDER_DIRECTION, WireMock.equalTo(String.valueOf(orderDirection)))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldSearchEntriesWithEmptySearchRequest() throws CommunicationFailureException, JsonProcessingException {

        // given
        ExtendedCommentListDataModel extendedCommentListDataModel = prepareExtendedCommentListDataModel();
        WrapperBodyDataModel<ExtendedCommentListDataModel> wrappedExtendedCommentListDataModelModel = prepareWrappedListDataModel(extendedCommentListDataModel);
        CommentSearchParameters commentSearchParameters = new CommentSearchParameters();

        givenThat(get(urlPathEqualTo(LeafletPath.COMMENTS_SEARCH.getURI()))
                .willReturn(jsonResponse(wrappedExtendedCommentListDataModelModel)));

        // when
        WrapperBodyDataModel<ExtendedCommentListDataModel> result = commentBridgeService.searchComments(commentSearchParameters);

        // then
        assertThat(result, equalTo(wrappedExtendedCommentListDataModelModel));
        verify(getRequestedFor(urlPathEqualTo(LeafletPath.COMMENTS_SEARCH.getURI()))
                .withQueryParam("page", WireMock.equalTo("1")));
    }

    @Test
    public void shouldSearchEntriesWithCompleteSearchRequest() throws CommunicationFailureException, JsonProcessingException {

        // given
        ExtendedCommentListDataModel extendedCommentListDataModel = prepareExtendedCommentListDataModel();
        WrapperBodyDataModel<ExtendedCommentListDataModel> wrappedExtendedCommentListDataModelModel = prepareWrappedListDataModel(extendedCommentListDataModel);
        CommentSearchParameters commentSearchParameters = new CommentSearchParameters();
        commentSearchParameters.setPage(3);
        commentSearchParameters.setContent("content1");
        commentSearchParameters.setEnabled(true);
        commentSearchParameters.setLimit(30);
        commentSearchParameters.setOrderBy(hu.psprog.leaflet.api.rest.request.common.OrderBy.Comment.ID);
        commentSearchParameters.setOrderDirection(hu.psprog.leaflet.api.rest.request.common.OrderDirection.ASC);

        givenThat(get(urlPathEqualTo(LeafletPath.COMMENTS_SEARCH.getURI()))
                .willReturn(jsonResponse(wrappedExtendedCommentListDataModelModel)));

        // when
        WrapperBodyDataModel<ExtendedCommentListDataModel> result = commentBridgeService.searchComments(commentSearchParameters);

        // then
        assertThat(result, equalTo(wrappedExtendedCommentListDataModelModel));
        verify(getRequestedFor(urlPathEqualTo(LeafletPath.COMMENTS_SEARCH.getURI()))
                .withQueryParam("page", WireMock.equalTo("3"))
                .withQueryParam("content", WireMock.equalTo("content1"))
                .withQueryParam("enabled", WireMock.equalTo("true"))
                .withQueryParam("limit", WireMock.equalTo("30"))
                .withQueryParam("orderBy", WireMock.equalTo("ID"))
                .withQueryParam("orderDirection", WireMock.equalTo("ASC")));
    }
    
    @Test
    public void shouldGetComment() throws CommunicationFailureException, JsonProcessingException {

        // given
        Long commentID = 1L;
        String uri = prepareURI(LeafletPath.COMMENTS_BY_ID.getURI(), commentID);
        ExtendedCommentDataModel extendedCommentDataModel = prepareExtendedCommentDataModel(commentID);
        givenThat(get(uri)
                .willReturn(jsonResponse(extendedCommentDataModel)));

        // when
        ExtendedCommentDataModel result = commentBridgeService.getComment(commentID);

        // then
        assertThat(result, equalTo(extendedCommentDataModel));
        verify(getRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldCreateComment() throws JsonProcessingException, CommunicationFailureException {

        // given
        CommentCreateRequestModel commentCreateRequestModel = new CommentCreateRequestModel();
        commentCreateRequestModel.setContent("comment");
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(commentCreateRequestModel));
        CommentDataModel commentDataModel = prepareCommentDataModel(1L);
        givenThat(post(LeafletPath.COMMENTS.getURI())
                .withRequestBody(requestBody)
                .willReturn(jsonResponse(commentDataModel)));

        // when
        CommentDataModel result = commentBridgeService.createComment(commentCreateRequestModel, RECAPTCHA_TOKEN);

        // then
        assertThat(result, equalTo(commentDataModel));
        verify(postRequestedFor(urlEqualTo(LeafletPath.COMMENTS.getURI()))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN)
                .withHeader(X_CAPTCHA_RESPONSE, WireMock.equalTo(RECAPTCHA_TOKEN)));
    }

    @Test
    public void shouldUpdateComment() throws CommunicationFailureException, JsonProcessingException {

        // given
        Long commentID = 1L;
        CommentCreateRequestModel commentCreateRequestModel = new CommentCreateRequestModel();
        commentCreateRequestModel.setContent("comment");
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(commentCreateRequestModel));
        CommentDataModel commentDataModel = prepareCommentDataModel(1L);
        String uri = prepareURI(LeafletPath.COMMENTS_BY_ID.getURI(), commentID);
        givenThat(put(uri)
                .withRequestBody(requestBody)
                .willReturn(jsonResponse(commentDataModel)));

        // when
        CommentDataModel result = commentBridgeService.updateComment(commentID, commentCreateRequestModel);

        // then
        assertThat(result, equalTo(commentDataModel));
        verify(putRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldChangeStatus() throws CommunicationFailureException, JsonProcessingException {

        // given
        Long commentID = 1L;
        String uri = prepareURI(LeafletPath.COMMENTS_STATUS.getURI(), commentID);
        ExtendedCommentDataModel extendedCommentDataModel = prepareExtendedCommentDataModel(commentID);
        givenThat(put(uri)
                .willReturn(jsonResponse(extendedCommentDataModel)));

        // when
        ExtendedCommentDataModel result = commentBridgeService.changeStatus(commentID);

        // then
        assertThat(result, equalTo(extendedCommentDataModel));
        verify(putRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldDeleteLogically() throws CommunicationFailureException {

        // given
        Long commentID = 1L;
        String uri = prepareURI(LeafletPath.COMMENTS_BY_ID.getURI(), commentID);
        givenThat(delete(uri));

        // when
        commentBridgeService.deleteCommentLogically(commentID);

        // then
        verify(deleteRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldDeletePermanently() throws CommunicationFailureException {

        // given
        Long commentID = 1L;
        String uri = prepareURI(LeafletPath.COMMENTS_DELETE_PERMANENT.getURI(), commentID);
        givenThat(delete(uri));

        // when
        commentBridgeService.deleteCommentPermanently(commentID);

        // then
        verify(deleteRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    private CommentListDataModel prepareCommentListDataModel() {
        return CommentListDataModel.getBuilder()
                .withComments(List.of(
                        prepareCommentDataModel(1L),
                        prepareCommentDataModel(2L)
                ))
                .build();
    }

    private ExtendedCommentListDataModel prepareExtendedCommentListDataModel() {
        return ExtendedCommentListDataModel.getBuilder()
                .withComments(List.of(
                        prepareExtendedCommentDataModel(1L),
                        prepareExtendedCommentDataModel(2L)
                ))
                .build();
    }

    private ExtendedCommentDataModel prepareExtendedCommentDataModel(Long commentID) {
        return ExtendedCommentDataModel.getBuilder()
                .withId(commentID)
                .withContent("Comment #" + commentID.toString())
                .withCreated(ZonedDateTime.now(ZONE_OFFSET))
                .withLastModified(ZonedDateTime.now(ZONE_OFFSET))
                .withDeleted(false)
                .withOwner(UserDataModel.getBuilder()
                        .withId(1L)
                        .withUsername("User")
                        .build())
                .withEnabled(true)
                .withAssociatedEntry(null)
                .build();
    }

    private CommentDataModel prepareCommentDataModel(Long commentID) {
        return CommentDataModel.getBuilder()
                .withId(commentID)
                .withContent("Comment #" + commentID.toString())
                .withCreated(ZonedDateTime.now(ZONE_OFFSET))
                .withLastModified(ZonedDateTime.now(ZONE_OFFSET))
                .withDeleted(false)
                .withOwner(UserDataModel.getBuilder()
                        .withId(1L)
                        .withUsername("User")
                        .build())
                .build();
    }
}
