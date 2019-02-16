package hu.psprog.leaflet.bridge.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import hu.psprog.leaflet.api.rest.request.tag.TagAssignmentRequestModel;
import hu.psprog.leaflet.api.rest.request.tag.TagCreateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.bridge.service.TagBridgeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.GenericType;

/**
 * Implementation of {@link TagBridgeService}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "leaflet")
@DefaultProperties(groupKey = "leaflet.content")
public class TagBridgeServiceImpl extends HystrixDefaultConfiguration implements TagBridgeService {

    private static final String ID = "id";

    private BridgeClient bridgeClient;

    @Autowired
    public TagBridgeServiceImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    public TagListDataModel getAllTags() throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.TAGS)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, TagListDataModel.class);
    }

    @Override
    @HystrixCommand
    public WrapperBodyDataModel<TagListDataModel> getAllPublicTags() throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.TAGS_PUBLIC)
                .build();

        return bridgeClient.call(restRequest, new GenericType<WrapperBodyDataModel<TagListDataModel>>() {});
    }

    @Override
    public TagDataModel getTag(Long tagID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.TAGS_BY_ID)
                .addPathParameter(ID, String.valueOf(tagID))
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, TagDataModel.class);
    }

    @Override
    public TagDataModel createTag(TagCreateRequestModel tagCreateRequestModel) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.POST)
                .path(LeafletPath.TAGS)
                .requestBody(tagCreateRequestModel)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, TagDataModel.class);
    }

    @Override
    public TagDataModel updateTag(Long tagID, TagCreateRequestModel tagCreateRequestModel) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.PUT)
                .path(LeafletPath.TAGS_BY_ID)
                .requestBody(tagCreateRequestModel)
                .addPathParameter(ID, String.valueOf(tagID))
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, TagDataModel.class);
    }

    @Override
    public void deleteTag(Long tagID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.DELETE)
                .path(LeafletPath.TAGS_BY_ID)
                .addPathParameter(ID, String.valueOf(tagID))
                .authenticated()
                .build();

        bridgeClient.call(restRequest);
    }

    @Override
    public TagDataModel changeStatus(Long tagID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.PUT)
                .path(LeafletPath.TAGS_STATUS)
                .addPathParameter(ID, String.valueOf(tagID))
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, TagDataModel.class);
    }

    @Override
    public void attachTag(TagAssignmentRequestModel tagAssignmentRequestModel) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.POST)
                .path(LeafletPath.TAGS_ASSIGN)
                .requestBody(tagAssignmentRequestModel)
                .authenticated()
                .build();

        bridgeClient.call(restRequest);
    }

    @Override
    public void detachTag(TagAssignmentRequestModel tagAssignmentRequestModel) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.PUT)
                .path(LeafletPath.TAGS_ASSIGN)
                .requestBody(tagAssignmentRequestModel)
                .authenticated()
                .build();

        bridgeClient.call(restRequest);
    }
}
