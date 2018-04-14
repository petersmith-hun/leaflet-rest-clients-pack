package hu.psprog.leaflet.bridge.service.impl;

import hu.psprog.leaflet.api.rest.request.entry.EntryCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.entry.EntryUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.GenericType;

/**
 * Implementation of {@link EntryBridgeService}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "leaflet")
class EntryBridgeServiceImpl implements EntryBridgeService {

    private static final String PAGE = "page";
    private static final String LIMIT = "limit";
    private static final String ORDER_BY = "orderBy";
    private static final String ORDER_DIRECTION = "orderDirection";
    private static final String ID = "id";
    private static final String LINK = "link";

    private BridgeClient bridgeClient;

    @Autowired
    public EntryBridgeServiceImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    public EntryListDataModel getAllEntries() throws CommunicationFailureException {

        RESTRequest request = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.ENTRIES)
                .authenticated()
                .build();

        return bridgeClient.call(request, EntryListDataModel.class);
    }

    @Override
    public WrapperBodyDataModel<EntryListDataModel> getPageOfPublicEntries(int page, int limit, OrderBy.Entry orderBy, OrderDirection orderDirection)
            throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.ENTRIES_PAGE)
                .addPathParameter(PAGE, String.valueOf(page))
                .addRequestParameters(LIMIT, String.valueOf(limit))
                .addRequestParameters(ORDER_BY, orderBy.name())
                .addRequestParameters(ORDER_DIRECTION, orderDirection.name())
                .build();

        return bridgeClient.call(restRequest, new GenericType<WrapperBodyDataModel<EntryListDataModel>>() {});
    }

    @Override
    public WrapperBodyDataModel<EntryListDataModel> getPageOfEntries(int page, int limit, OrderBy.Entry orderBy, OrderDirection orderDirection)
            throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.ENTRIES_PAGE_ALL)
                .addPathParameter(PAGE, String.valueOf(page))
                .addRequestParameters(LIMIT, String.valueOf(limit))
                .addRequestParameters(ORDER_BY, orderBy.name())
                .addRequestParameters(ORDER_DIRECTION, orderDirection.name())
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, new GenericType<WrapperBodyDataModel<EntryListDataModel>>() {});
    }

    @Override
    public WrapperBodyDataModel<EntryListDataModel> getPageOfPublicEntriesByCategory(Long categoryID, int page, int limit, OrderBy.Entry orderBy, OrderDirection orderDirection)
            throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.ENTRIES_CATEGORY_PAGE)
                .addPathParameter(ID, String.valueOf(categoryID))
                .addPathParameter(PAGE, String.valueOf(page))
                .addRequestParameters(LIMIT, String.valueOf(limit))
                .addRequestParameters(ORDER_BY, orderBy.name())
                .addRequestParameters(ORDER_DIRECTION, orderDirection.name())
                .build();

        return bridgeClient.call(restRequest, new GenericType<WrapperBodyDataModel<EntryListDataModel>>() {});
    }

    @Override
    public WrapperBodyDataModel<ExtendedEntryDataModel> getEntryByLink(String link) throws CommunicationFailureException {

        RESTRequest request = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.ENTRIES_BY_LINK)
                .addPathParameter(LINK, link)
                .build();

        return bridgeClient.call(request, new GenericType<WrapperBodyDataModel<ExtendedEntryDataModel>>() {});
    }

    @Override
    public WrapperBodyDataModel<EditEntryDataModel> getEntryByID(Long entryID) throws CommunicationFailureException {

        RESTRequest request = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.ENTRIES_BY_ID)
                .addPathParameter(ID, String.valueOf(entryID))
                .authenticated()
                .build();

        return bridgeClient.call(request, new GenericType<WrapperBodyDataModel<EditEntryDataModel>>() {});
    }

    @Override
    public EditEntryDataModel createEntry(EntryCreateRequestModel entryCreateRequestModel) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.POST)
                .path(LeafletPath.ENTRIES)
                .requestBody(entryCreateRequestModel)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, EditEntryDataModel.class);
    }

    @Override
    public EditEntryDataModel updateEntry(Long entryID, EntryUpdateRequestModel entryUpdateRequestModel) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.PUT)
                .path(LeafletPath.ENTRIES_BY_ID)
                .addPathParameter(ID, String.valueOf(entryID))
                .requestBody(entryUpdateRequestModel)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, EditEntryDataModel.class);
    }

    @Override
    public EditEntryDataModel changeStatus(Long entryID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.PUT)
                .path(LeafletPath.ENTRIES_STATUS)
                .addPathParameter(ID, String.valueOf(entryID))
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, EditEntryDataModel.class);
    }

    @Override
    public void deleteEntry(Long entryID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.DELETE)
                .path(LeafletPath.ENTRIES_BY_ID)
                .addPathParameter(ID, String.valueOf(entryID))
                .authenticated()
                .build();

        bridgeClient.call(restRequest);
    }
}
