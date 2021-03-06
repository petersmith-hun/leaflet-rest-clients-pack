package hu.psprog.leaflet.bridge.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
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
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.GenericType;

/**
 * Implementation of {@link CommentBridgeService}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "leaflet")
@DefaultProperties(groupKey = "leaflet.comments")
public class CommentBridgeServiceImpl extends HystrixDefaultConfiguration implements CommentBridgeService {

    private static final String PAGE = "page";
    private static final String LIMIT = "limit";
    private static final String ORDER_BY = "orderBy";
    private static final String ORDER_DIRECTION = "orderDirection";
    private static final String ID = "id";
    private static final String LINK = "link";

    private BridgeClient bridgeClient;

    @Autowired
    public CommentBridgeServiceImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    @HystrixCommand
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
    @HystrixCommand
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
