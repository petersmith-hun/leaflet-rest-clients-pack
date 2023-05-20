package hu.psprog.leaflet.bridge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.api.rest.request.entry.EntryCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.entry.EntryInitialStatus;
import hu.psprog.leaflet.api.rest.request.entry.EntrySearchParameters;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntrySearchResultDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.it.config.BridgeITSuite;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
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
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link EntryBridgeServiceImpl}.
 *
 * @author Peter Smith
 */
@BridgeITSuite
public class EntryBridgeServiceImplIT extends WireMockBaseTest {

    @Autowired
    private EntryBridgeService entryBridgeService;

    @Test
    public void shouldGetAllEntries() throws CommunicationFailureException {

        // given
        EntryListDataModel entryListDataModel = prepareEntryListDataModel();
        givenThat(get(LeafletPath.ENTRIES.getURI())
                .willReturn(ResponseDefinitionBuilder.okForJson(entryListDataModel)));

        // when
        EntryListDataModel result = entryBridgeService.getAllEntries();

        // then
        assertThat(result, equalTo(entryListDataModel));
        verify(getRequestedFor(urlEqualTo(LeafletPath.ENTRIES.getURI()))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldGetPageOfPublicEntries() throws CommunicationFailureException {

        // given
        EntryListDataModel entryListDataModel = prepareEntryListDataModel();
        WrapperBodyDataModel<EntryListDataModel> wrappedEntryListDataModel = prepareWrappedListDataModel(entryListDataModel);
        int page = 1;
        int limit = 10;
        OrderBy.Entry orderBy = OrderBy.Entry.CREATED;
        OrderDirection orderDirection = OrderDirection.ASC;
        String uri = prepareURI(LeafletPath.ENTRIES_PAGE.getURI(), page);
        givenThat(get(urlPathEqualTo(uri))
                .willReturn(ResponseDefinitionBuilder.okForJson(wrappedEntryListDataModel)));

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfPublicEntries(page, limit, orderBy, orderDirection);

        // then
        assertThat(result, equalTo(wrappedEntryListDataModel));
        verify(getRequestedFor(urlPathEqualTo(uri))
                .withQueryParam(LIMIT, WireMock.equalTo(String.valueOf(limit)))
                .withQueryParam(ORDER_BY, WireMock.equalTo(orderBy.name()))
                .withQueryParam(ORDER_DIRECTION, WireMock.equalTo(String.valueOf(orderDirection))));
    }

    @Test
    public void shouldGetPageOfEntries() throws CommunicationFailureException {

        // given
        EntryListDataModel entryListDataModel = prepareEntryListDataModel();
        WrapperBodyDataModel<EntryListDataModel> wrappedEntryListDataModel = prepareWrappedListDataModel(entryListDataModel);
        int page = 1;
        int limit = 10;
        OrderBy.Entry orderBy = OrderBy.Entry.CREATED;
        OrderDirection orderDirection = OrderDirection.ASC;
        String uri = prepareURI(LeafletPath.ENTRIES_PAGE_ALL.getURI(), page);
        givenThat(get(urlPathEqualTo(uri))
                .willReturn(ResponseDefinitionBuilder.okForJson(wrappedEntryListDataModel)));

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfEntries(page, limit, orderBy, orderDirection);

        // then
        assertThat(result, equalTo(wrappedEntryListDataModel));
        verify(getRequestedFor(urlPathEqualTo(uri))
                .withQueryParam(LIMIT, WireMock.equalTo(String.valueOf(limit)))
                .withQueryParam(ORDER_BY, WireMock.equalTo(orderBy.name()))
                .withQueryParam(ORDER_DIRECTION, WireMock.equalTo(String.valueOf(orderDirection)))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldGetPageOfPublicEntriesByCategory() throws CommunicationFailureException {

        // given
        EntryListDataModel entryListDataModel = prepareEntryListDataModel();
        WrapperBodyDataModel<EntryListDataModel> wrappedEntryListDataModel = prepareWrappedListDataModel(entryListDataModel);
        Long categoryID = 1L;
        int page = 1;
        int limit = 10;
        OrderBy.Entry orderBy = OrderBy.Entry.CREATED;
        OrderDirection orderDirection = OrderDirection.ASC;
        String uri = prepareURI(LeafletPath.ENTRIES_CATEGORY_PAGE.getURI(), categoryID, page);
        givenThat(get(urlPathEqualTo(uri))
                .willReturn(ResponseDefinitionBuilder.okForJson(wrappedEntryListDataModel)));

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfPublicEntriesByCategory(categoryID, page, limit, orderBy, orderDirection);

        // then
        assertThat(result, equalTo(wrappedEntryListDataModel));
        verify(getRequestedFor(urlPathEqualTo(uri))
                .withQueryParam(LIMIT, WireMock.equalTo(String.valueOf(limit)))
                .withQueryParam(ORDER_BY, WireMock.equalTo(orderBy.name()))
                .withQueryParam(ORDER_DIRECTION, WireMock.equalTo(String.valueOf(orderDirection))));
    }

    @Test
    public void shouldGetPageOfPublicEntriesByTag() throws CommunicationFailureException {

        // given
        EntryListDataModel entryListDataModel = prepareEntryListDataModel();
        WrapperBodyDataModel<EntryListDataModel> wrappedEntryListDataModel = prepareWrappedListDataModel(entryListDataModel);
        Long tagID = 1L;
        int page = 1;
        int limit = 10;
        OrderBy.Entry orderBy = OrderBy.Entry.CREATED;
        OrderDirection orderDirection = OrderDirection.ASC;
        String uri = prepareURI(LeafletPath.ENTRIES_TAG_PAGE.getURI(), tagID, page);
        givenThat(get(urlPathEqualTo(uri))
                .willReturn(ResponseDefinitionBuilder.okForJson(wrappedEntryListDataModel)));

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfPublicEntriesByTag(tagID, page, limit, orderBy, orderDirection);

        // then
        assertThat(result, equalTo(wrappedEntryListDataModel));
        verify(getRequestedFor(urlPathEqualTo(uri))
                .withQueryParam(LIMIT, WireMock.equalTo(String.valueOf(limit)))
                .withQueryParam(ORDER_BY, WireMock.equalTo(orderBy.name()))
                .withQueryParam(ORDER_DIRECTION, WireMock.equalTo(String.valueOf(orderDirection))));
    }

    @Test
    public void shouldGetPageOfPublicEntriesByContent() throws CommunicationFailureException {

        // given
        EntryListDataModel entryListDataModel = prepareEntryListDataModel();
        WrapperBodyDataModel<EntryListDataModel> wrappedEntryListDataModel = prepareWrappedListDataModel(entryListDataModel);
        String content = "content";
        int page = 1;
        int limit = 10;
        OrderBy.Entry orderBy = OrderBy.Entry.CREATED;
        OrderDirection orderDirection = OrderDirection.ASC;
        String uri = prepareURI(LeafletPath.ENTRIES_CONTENT_PAGE.getURI(), page);
        givenThat(get(urlPathEqualTo(uri))
                .willReturn(ResponseDefinitionBuilder.okForJson(wrappedEntryListDataModel)));

        // when
        WrapperBodyDataModel<EntryListDataModel> result = entryBridgeService.getPageOfPublicEntriesByContent(content, page, limit, orderBy, orderDirection);

        // then
        assertThat(result, equalTo(wrappedEntryListDataModel));
        verify(getRequestedFor(urlPathEqualTo(uri))
                .withQueryParam(CONTENT, WireMock.equalTo(content))
                .withQueryParam(LIMIT, WireMock.equalTo(String.valueOf(limit)))
                .withQueryParam(ORDER_BY, WireMock.equalTo(orderBy.name()))
                .withQueryParam(ORDER_DIRECTION, WireMock.equalTo(String.valueOf(orderDirection))));
    }

    @Test
    public void shouldSearchEntriesWithEmptySearchRequest() throws CommunicationFailureException {

        // given
        EntrySearchResultDataModel entrySearchResultDataModel = prepareEntrySearchResultDataModel();
        WrapperBodyDataModel<EntrySearchResultDataModel> wrappedEntrySearchResultDataModel = prepareWrappedListDataModel(entrySearchResultDataModel);
        EntrySearchParameters entrySearchParameters = new EntrySearchParameters();

        givenThat(get(urlPathEqualTo(LeafletPath.ENTRIES_SEARCH.getURI()))
                .willReturn(ResponseDefinitionBuilder.okForJson(wrappedEntrySearchResultDataModel)));

        // when
        WrapperBodyDataModel<EntrySearchResultDataModel> result = entryBridgeService.searchEntries(entrySearchParameters);

        // then
        assertThat(result, equalTo(wrappedEntrySearchResultDataModel));
        verify(getRequestedFor(urlPathEqualTo(LeafletPath.ENTRIES_SEARCH.getURI()))
                .withQueryParam("page", WireMock.equalTo("1")));
    }

    @Test
    public void shouldSearchEntriesWithCompleteSearchRequest() throws CommunicationFailureException {

        // given
        EntrySearchResultDataModel entrySearchResultDataModel = prepareEntrySearchResultDataModel();
        WrapperBodyDataModel<EntrySearchResultDataModel> wrappedEntrySearchResultDataModel = prepareWrappedListDataModel(entrySearchResultDataModel);
        EntrySearchParameters entrySearchParameters = new EntrySearchParameters();
        entrySearchParameters.setPage(3);
        entrySearchParameters.setContent("content1");
        entrySearchParameters.setEnabled(true);
        entrySearchParameters.setLimit(30);
        entrySearchParameters.setStatus(EntryInitialStatus.REVIEW);
        entrySearchParameters.setCategoryID(2L);
        entrySearchParameters.setOrderBy(hu.psprog.leaflet.api.rest.request.common.OrderBy.Entry.TITLE);
        entrySearchParameters.setOrderDirection(hu.psprog.leaflet.api.rest.request.common.OrderDirection.ASC);

        givenThat(get(urlPathEqualTo(LeafletPath.ENTRIES_SEARCH.getURI()))
                .willReturn(ResponseDefinitionBuilder.okForJson(wrappedEntrySearchResultDataModel)));

        // when
        WrapperBodyDataModel<EntrySearchResultDataModel> result = entryBridgeService.searchEntries(entrySearchParameters);

        // then
        assertThat(result, equalTo(wrappedEntrySearchResultDataModel));
        verify(getRequestedFor(urlPathEqualTo(LeafletPath.ENTRIES_SEARCH.getURI()))
                .withQueryParam("page", WireMock.equalTo("3"))
                .withQueryParam("content", WireMock.equalTo("content1"))
                .withQueryParam("enabled", WireMock.equalTo("true"))
                .withQueryParam("limit", WireMock.equalTo("30"))
                .withQueryParam("status", WireMock.equalTo("REVIEW"))
                .withQueryParam("categoryID", WireMock.equalTo("2"))
                .withQueryParam("orderBy", WireMock.equalTo("TITLE"))
                .withQueryParam("orderDirection", WireMock.equalTo("ASC")));
    }

    @Test
    public void shouldGetEntryByLink() throws CommunicationFailureException {

        // given
        ExtendedEntryDataModel extendedEntryDataModel = prepareExtendedEntryDataModel(1L);
        WrapperBodyDataModel<ExtendedEntryDataModel> wrappedEntryDataModel = prepareWrappedListDataModel(extendedEntryDataModel);
        String link = "entry-1";
        String uri = prepareURI(LeafletPath.ENTRIES_BY_LINK.getURI(), link);
        givenThat(get(uri)
                .willReturn(ResponseDefinitionBuilder.okForJson(wrappedEntryDataModel)));

        // when
        WrapperBodyDataModel<ExtendedEntryDataModel> result = entryBridgeService.getEntryByLink(link);

        // then
        assertThat(result, equalTo(wrappedEntryDataModel));
        verify(getRequestedFor(urlEqualTo(uri)));
    }

    @Test
    public void shouldGetEntryByID() throws CommunicationFailureException {

        // given
        EditEntryDataModel extendedEntryDataModel = prepareEditEntryDataModel(1L);
        WrapperBodyDataModel<EditEntryDataModel> wrappedEntryDataModel = prepareWrappedListDataModel(extendedEntryDataModel);
        Long entryID = 1L;
        String uri = prepareURI(LeafletPath.ENTRIES_BY_ID.getURI(), entryID);
        givenThat(get(uri)
                .willReturn(ResponseDefinitionBuilder.okForJson(wrappedEntryDataModel)));

        // when
        WrapperBodyDataModel<EditEntryDataModel> result = entryBridgeService.getEntryByID(entryID);

        // then
        assertThat(result, equalTo(wrappedEntryDataModel));
        verify(getRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldCreateEntry() throws JsonProcessingException, CommunicationFailureException {

        // given
        EntryCreateRequestModel entryCreateRequestModel = prepareEntryCreateRequestModel();
        EditEntryDataModel extendedEntryDataModel = prepareEditEntryDataModel(1L);
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(entryCreateRequestModel));
        givenThat(post(LeafletPath.ENTRIES.getURI())
                .withRequestBody(requestBody)
                .willReturn(ResponseDefinitionBuilder.okForJson(extendedEntryDataModel)));

        // when
        EditEntryDataModel result = entryBridgeService.createEntry(entryCreateRequestModel);

        // then
        assertThat(result, equalTo(extendedEntryDataModel));
        verify(postRequestedFor(urlEqualTo(LeafletPath.ENTRIES.getURI()))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldUpdateEntry() throws JsonProcessingException, CommunicationFailureException {

        // given
        EntryCreateRequestModel entryCreateRequestModel = prepareEntryCreateRequestModel();
        EditEntryDataModel extendedEntryDataModel = prepareEditEntryDataModel(1L);
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(entryCreateRequestModel));
        Long entryID = 1L;
        String uri = prepareURI(LeafletPath.ENTRIES_BY_ID.getURI(), entryID);
        givenThat(put(uri)
                .withRequestBody(requestBody)
                .willReturn(ResponseDefinitionBuilder.okForJson(extendedEntryDataModel)));

        // when
        EditEntryDataModel result = entryBridgeService.updateEntry(entryID, entryCreateRequestModel);

        // then
        assertThat(result, equalTo(extendedEntryDataModel));
        verify(putRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldChangeStatus() throws CommunicationFailureException {

        // given
        EditEntryDataModel extendedEntryDataModel = prepareEditEntryDataModel(1L);
        Long entryID = 1L;
        String uri = prepareURI(LeafletPath.ENTRIES_STATUS.getURI(), entryID);
        givenThat(put(uri)
                .willReturn(ResponseDefinitionBuilder.okForJson(extendedEntryDataModel)));

        // when
        EditEntryDataModel result = entryBridgeService.changeStatus(entryID);

        // then
        assertThat(result, equalTo(extendedEntryDataModel));
        verify(putRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldChangePublicationStatus() throws CommunicationFailureException {

        // given
        EditEntryDataModel extendedEntryDataModel = prepareEditEntryDataModel(1L);
        Long entryID = 1L;
        EntryInitialStatus newStatus = EntryInitialStatus.PUBLIC;
        String uri = prepareURI(LeafletPath.ENTRIES_PUBLICATION_STATUS.getURI(), entryID, newStatus);

        givenThat(put(uri)
                .willReturn(ResponseDefinitionBuilder.okForJson(extendedEntryDataModel)));

        // when
        EditEntryDataModel result = entryBridgeService.changePublicationStatus(entryID, newStatus);

        // then
        assertThat(result, equalTo(extendedEntryDataModel));
        verify(putRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldDeleteEntry() throws CommunicationFailureException {

        // given
        Long entryID = 1L;
        String uri = prepareURI(LeafletPath.ENTRIES_BY_ID.getURI(), entryID);
        givenThat(delete(uri));

        // when
        entryBridgeService.deleteEntry(entryID);

        // then
        verify(deleteRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    private EntryCreateRequestModel prepareEntryCreateRequestModel() {
        EntryCreateRequestModel entryCreateRequestModel = new EntryCreateRequestModel();
        entryCreateRequestModel.setRawContent("entry");
        return entryCreateRequestModel;
    }

    private EntryListDataModel prepareEntryListDataModel() {
        return EntryListDataModel.getBuilder()
                .withEntries(List.of(
                        prepareEntryDataModel(1L),
                        prepareEntryDataModel(2L)
                ))
                .build();
    }

    private EntrySearchResultDataModel prepareEntrySearchResultDataModel() {

        return EntrySearchResultDataModel.getBuilder()
                .withEntries(List.of(prepareEditEntryDataModel(3L)))
                .build();
    }

    private EntryDataModel prepareEntryDataModel(Long entryID) {
        return EntryDataModel.getBuilder()
                .withId(entryID)
                .withLink("entry-" + entryID)
                .withTitle("Entry #" + entryID)
                .build();
    }

    private ExtendedEntryDataModel prepareExtendedEntryDataModel(Long entryID) {
        return ExtendedEntryDataModel.getBuilder()
                .withId(entryID)
                .withLink("entry-" + entryID)
                .withTitle("Entry #" + entryID)
                .build();
    }

    private EditEntryDataModel prepareEditEntryDataModel(Long entryID) {
        return EditEntryDataModel.getBuilder()
                .withId(entryID)
                .withLink("entry-" + entryID)
                .withTitle("Entry #" + entryID)
                .withRawContent("raw-content" + entryID)
                .build();
    }
}
