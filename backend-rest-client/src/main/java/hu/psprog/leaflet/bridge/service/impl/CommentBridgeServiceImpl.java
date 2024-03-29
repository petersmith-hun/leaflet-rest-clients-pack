package hu.psprog.leaflet.bridge.service.impl;

import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.comment.CommentSearchParameters;
import hu.psprog.leaflet.api.rest.request.comment.CommentUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.service.CommentBridgeService;
import jakarta.ws.rs.core.GenericType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of {@link CommentBridgeService}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "leaflet")
public class CommentBridgeServiceImpl implements CommentBridgeService {

    private static final String PAGE = "page";
    private static final String LIMIT = "limit";
    private static final String ORDER_BY = "orderBy";
    private static final String ORDER_DIRECTION = "orderDirection";
    private static final String ID = "id";
    private static final String LINK = "link";
    private static final String ENABLED = "enabled";
    private static final String DELETED = "deleted";
    private static final String CONTENT = "content";

    private final BridgeClient bridgeClient;

    @Autowired
    public CommentBridgeServiceImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    public WrapperBodyDataModel<CommentListDataModel> getPageOfPublicCommentsForEntry(String entryLink, int page, int limit, OrderBy.Comment orderBy, OrderDirection orderDirection)
            throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.COMMENTS_PUBLIC_PAGE_BY_ENTRY)
                .addPathParameter(LINK, entryLink)
                .addPathParameter(PAGE, String.valueOf(page))
                .addRequestParameters(LIMIT, String.valueOf(limit))
                .addRequestParameters(ORDER_BY, orderBy.name())
                .addRequestParameters(ORDER_DIRECTION, orderDirection.name())
                .build();

        return bridgeClient.call(restRequest, new GenericType<WrapperBodyDataModel<CommentListDataModel>>() {});
    }

    @Override
    public WrapperBodyDataModel<CommentListDataModel> getPageOfCommentsForEntry(Long entryID, int page, int limit, OrderBy.Comment orderBy, OrderDirection orderDirection)
            throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.COMMENTS_ALL_PAGE_BY_ENTRY)
                .addPathParameter(ID, String.valueOf(entryID))
                .addPathParameter(PAGE, String.valueOf(page))
                .addRequestParameters(LIMIT, String.valueOf(limit))
                .addRequestParameters(ORDER_BY, orderBy.name())
                .addRequestParameters(ORDER_DIRECTION, orderDirection.name())
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, new GenericType<WrapperBodyDataModel<CommentListDataModel>>() {});
    }

    @Override
    public WrapperBodyDataModel<ExtendedCommentListDataModel> getPageOfCommentsForUser(Long userID, int page, int limit, OrderBy.Comment orderBy, OrderDirection orderDirection)
            throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.COMMENTS_ALL_PAGE_BY_USER)
                .addPathParameter(ID, String.valueOf(userID))
                .addPathParameter(PAGE, String.valueOf(page))
                .addRequestParameters(LIMIT, String.valueOf(limit))
                .addRequestParameters(ORDER_BY, orderBy.name())
                .addRequestParameters(ORDER_DIRECTION, orderDirection.name())
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, new GenericType<WrapperBodyDataModel<ExtendedCommentListDataModel>>() {});
    }

    @Override
    public WrapperBodyDataModel<ExtendedCommentListDataModel> searchComments(CommentSearchParameters commentSearchParameters) throws CommunicationFailureException {

        RESTRequest.RESTRequestBuilder restRequestBuilder = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.COMMENTS_SEARCH)
                .authenticated()
                .addRequestParameters(PAGE, String.valueOf(commentSearchParameters.getPage()));

        commentSearchParameters.getOrderBy()
                .ifPresent(orderBy -> restRequestBuilder.addRequestParameters(ORDER_BY, orderBy.name()));
        commentSearchParameters.getOrderDirection()
                .ifPresent(orderDirection -> restRequestBuilder.addRequestParameters(ORDER_DIRECTION, orderDirection.name()));
        commentSearchParameters.getLimit()
                .ifPresent(limit -> restRequestBuilder.addRequestParameters(LIMIT, String.valueOf(limit)));
        commentSearchParameters.getEnabled()
                .ifPresent(enabled -> restRequestBuilder.addRequestParameters(ENABLED, String.valueOf(enabled)));
        commentSearchParameters.getDeleted()
                .ifPresent(deleted -> restRequestBuilder.addRequestParameters(DELETED, String.valueOf(deleted)));
        commentSearchParameters.getContent()
                .ifPresent(content -> restRequestBuilder.addRequestParameters(CONTENT, content));

        return bridgeClient.call(restRequestBuilder.build(), new GenericType<WrapperBodyDataModel<ExtendedCommentListDataModel>>() {});
    }

    @Override
    public ExtendedCommentDataModel getComment(Long commentID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.COMMENTS_BY_ID)
                .addPathParameter(ID, String.valueOf(commentID))
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, ExtendedCommentDataModel.class);
    }

    @Override
    public CommentDataModel createComment(CommentCreateRequestModel commentCreateRequestModel, String recaptchaToken) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.POST)
                .path(LeafletPath.COMMENTS)
                .requestBody(commentCreateRequestModel)
                .recaptchaResponse(recaptchaToken)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, CommentDataModel.class);
    }

    @Override
    public CommentDataModel updateComment(Long commentID, CommentUpdateRequestModel commentUpdateRequestModel) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.PUT)
                .path(LeafletPath.COMMENTS_BY_ID)
                .addPathParameter(ID, String.valueOf(commentID))
                .requestBody(commentUpdateRequestModel)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, CommentDataModel.class);
    }

    @Override
    public ExtendedCommentDataModel changeStatus(Long commentID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.PUT)
                .path(LeafletPath.COMMENTS_STATUS)
                .addPathParameter(ID, String.valueOf(commentID))
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, ExtendedCommentDataModel.class);
    }

    @Override
    public void deleteCommentLogically(Long commentID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.DELETE)
                .path(LeafletPath.COMMENTS_BY_ID)
                .addPathParameter(ID, String.valueOf(commentID))
                .authenticated()
                .build();

        bridgeClient.call(restRequest);
    }

    @Override
    public void deleteCommentPermanently(Long commentID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.DELETE)
                .path(LeafletPath.COMMENTS_DELETE_PERMANENT)
                .addPathParameter(ID, String.valueOf(commentID))
                .authenticated()
                .build();

        bridgeClient.call(restRequest);
    }
}
