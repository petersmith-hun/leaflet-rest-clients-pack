package hu.psprog.leaflet.bridge.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import hu.psprog.leaflet.api.rest.request.document.DocumentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.document.DocumentUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentListDataModel;
import hu.psprog.leaflet.api.rest.response.document.EditDocumentDataModel;
import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.bridge.service.DocumentBridgeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.GenericType;

/**
 * Implementation of {@link DocumentBridgeService}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "leaflet")
@DefaultProperties(groupKey = "leaflet.content")
public class DocumentBridgeServiceImpl extends HystrixDefaultConfiguration implements DocumentBridgeService {

    private static final String ID = "id";
    private static final String LINK = "link";

    private BridgeClient bridgeClient;

    @Autowired
    public DocumentBridgeServiceImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    public DocumentListDataModel getAllDocuments() throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.DOCUMENTS)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, DocumentListDataModel.class);
    }

    @Override
    @HystrixCommand
    public DocumentListDataModel getPublicDocuments() throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.DOCUMENTS_PUBLIC)
                .build();

        return bridgeClient.call(restRequest, DocumentListDataModel.class);
    }

    @Override
    public WrapperBodyDataModel<EditDocumentDataModel> getDocumentByID(Long documentID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.DOCUMENTS_BY_ID)
                .addPathParameter(ID, String.valueOf(documentID))
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, new GenericType<WrapperBodyDataModel<EditDocumentDataModel>>() {});
    }

    @Override
    @HystrixCommand
    public WrapperBodyDataModel<DocumentDataModel> getDocumentByLink(String link) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.DOCUMENTS_BY_LINK)
                .addPathParameter(LINK, link)
                .build();

        return bridgeClient.call(restRequest, new GenericType<WrapperBodyDataModel<DocumentDataModel>>() {});
    }

    @Override
    public EditDocumentDataModel createDocument(DocumentCreateRequestModel documentCreateRequestModel) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.POST)
                .path(LeafletPath.DOCUMENTS)
                .requestBody(documentCreateRequestModel)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, EditDocumentDataModel.class);
    }

    @Override
    public EditDocumentDataModel updateDocument(Long documentID, DocumentUpdateRequestModel documentUpdateRequestModel) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.PUT)
                .path(LeafletPath.DOCUMENTS_BY_ID)
                .addPathParameter(ID, String.valueOf(documentID))
                .requestBody(documentUpdateRequestModel)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, EditDocumentDataModel.class);
    }

    @Override
    public EditDocumentDataModel changeStatus(Long documentID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.PUT)
                .path(LeafletPath.DOCUMENTS_STATUS)
                .addPathParameter(ID, String.valueOf(documentID))
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, EditDocumentDataModel.class);
    }

    @Override
    public void deleteDocument(Long documentID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.DELETE)
                .path(LeafletPath.DOCUMENTS_BY_ID)
                .addPathParameter(ID, String.valueOf(documentID))
                .authenticated()
                .build();

        bridgeClient.call(restRequest);
    }
}
