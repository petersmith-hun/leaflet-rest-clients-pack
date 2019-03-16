package hu.psprog.leaflet.bridge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.api.rest.request.document.DocumentCreateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentListDataModel;
import hu.psprog.leaflet.api.rest.response.document.EditDocumentDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.it.config.BridgeITSuite;
import hu.psprog.leaflet.bridge.service.DocumentBridgeService;
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
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link DocumentBridgeServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@BridgeITSuite
public class DocumentBridgeServiceImplIT extends WireMockBaseTest {

    @Autowired
    private DocumentBridgeService documentBridgeService;

    @Test
    public void shouldGetAllDocuments() throws CommunicationFailureException, JsonProcessingException {

        // given
        DocumentListDataModel documentListDataModel = prepareDocumentListDataModel();
        givenThat(get(LeafletPath.DOCUMENTS.getURI())
                .willReturn(jsonResponse(documentListDataModel)));

        // when
        DocumentListDataModel result = documentBridgeService.getAllDocuments();

        // then
        assertThat(result, equalTo(documentListDataModel));
        verify(getRequestedFor(urlEqualTo(LeafletPath.DOCUMENTS.getURI()))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldGetPublicDocuments() throws CommunicationFailureException, JsonProcessingException {

        // given
        DocumentListDataModel documentListDataModel = prepareDocumentListDataModel();
        givenThat(get(LeafletPath.DOCUMENTS_PUBLIC.getURI())
                .willReturn(jsonResponse(documentListDataModel)));

        // when
        DocumentListDataModel result = documentBridgeService.getPublicDocuments();

        // then
        assertThat(result, equalTo(documentListDataModel));
        verify(getRequestedFor(urlEqualTo(LeafletPath.DOCUMENTS_PUBLIC.getURI())));
    }

    @Test
    public void shouldGetDocumentByID() throws CommunicationFailureException, JsonProcessingException {

        // given
        EditDocumentDataModel editDocumentDataModel = prepareEditDocumentDataModel(1L);
        WrapperBodyDataModel<EditDocumentDataModel> wrappedEditDocumentDataModel = prepareWrappedListDataModel(editDocumentDataModel);
        Long documentID = 1L;
        String uri = prepareURI(LeafletPath.DOCUMENTS_BY_ID.getURI(), documentID);
        givenThat(get(uri)
                .willReturn(jsonResponse(wrappedEditDocumentDataModel)));

        // when
        WrapperBodyDataModel<EditDocumentDataModel> result = documentBridgeService.getDocumentByID(documentID);

        // then
        assertThat(result, equalTo(wrappedEditDocumentDataModel));
        verify(getRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldGetDocumentByLink() throws CommunicationFailureException, JsonProcessingException {

        // given
        DocumentDataModel documentDataModel = prepareDocumentDataModel(1L);
        WrapperBodyDataModel<DocumentDataModel> wrappedDocumentDataModel = prepareWrappedListDataModel(documentDataModel);
        String link = "document-1";
        String uri = prepareURI(LeafletPath.DOCUMENTS_BY_LINK.getURI(), link);
        givenThat(get(uri)
                .willReturn(jsonResponse(wrappedDocumentDataModel)));

        // when
        WrapperBodyDataModel<DocumentDataModel> result = documentBridgeService.getDocumentByLink(link);

        // then
        assertThat(result, equalTo(wrappedDocumentDataModel));
        verify(getRequestedFor(urlEqualTo(uri)));
    }

    @Test
    public void shouldCreateDocument() throws CommunicationFailureException, JsonProcessingException {

        // given
        DocumentCreateRequestModel documentCreateRequestModel = prepareDocumentCreateRequestModel();
        EditDocumentDataModel editDocumentDataModel = prepareEditDocumentDataModel(1L);
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(documentCreateRequestModel));
        givenThat(post(LeafletPath.DOCUMENTS.getURI())
                .withRequestBody(requestBody)
                .willReturn(jsonResponse(editDocumentDataModel)));

        // when
        EditDocumentDataModel result = documentBridgeService.createDocument(documentCreateRequestModel);

        // then
        assertThat(result, equalTo(editDocumentDataModel));
        verify(postRequestedFor(urlEqualTo(LeafletPath.DOCUMENTS.getURI()))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldUpdateDocument() throws CommunicationFailureException, JsonProcessingException {

        // given
        DocumentCreateRequestModel documentCreateRequestModel = prepareDocumentCreateRequestModel();
        EditDocumentDataModel editDocumentDataModel = prepareEditDocumentDataModel(1L);
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(documentCreateRequestModel));
        Long documentID = 1L;
        String uri = prepareURI(LeafletPath.DOCUMENTS_BY_ID.getURI(), documentID);
        givenThat(put(uri)
                .withRequestBody(requestBody)
                .willReturn(jsonResponse(editDocumentDataModel)));

        // when
        EditDocumentDataModel result = documentBridgeService.updateDocument(documentID, documentCreateRequestModel);

        // then
        assertThat(result, equalTo(editDocumentDataModel));
        verify(putRequestedFor(urlEqualTo(uri))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldChangeDocumentStatus() throws CommunicationFailureException, JsonProcessingException {

        // given
        Long documentID = 1L;
        String uri = prepareURI(LeafletPath.DOCUMENTS_STATUS.getURI(), documentID);
        EditDocumentDataModel editDocumentDataModel = prepareEditDocumentDataModel(1L);
        givenThat(put(uri)
                .willReturn(jsonResponse(editDocumentDataModel)));

        // when
        EditDocumentDataModel result = documentBridgeService.changeStatus(documentID);

        // then
        assertThat(result, equalTo(editDocumentDataModel));
        verify(putRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldDeleteDocument() throws CommunicationFailureException {

        // given
        Long documentID = 1L;
        String uri = prepareURI(LeafletPath.DOCUMENTS_BY_ID.getURI(), documentID);
        givenThat(delete(uri));

        // when
        documentBridgeService.deleteDocument(documentID);

        // then
        verify(deleteRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    private DocumentCreateRequestModel prepareDocumentCreateRequestModel() {
        DocumentCreateRequestModel documentCreateRequestModel = new DocumentCreateRequestModel();
        documentCreateRequestModel.setRawContent("content");
        return documentCreateRequestModel;
    }

    private DocumentListDataModel prepareDocumentListDataModel() {
        return DocumentListDataModel.getBuilder()
                .withItem(prepareEditDocumentDataModel(1L))
                .withItem(prepareEditDocumentDataModel(2L))
                .build();
    }

    private EditDocumentDataModel prepareEditDocumentDataModel(Long documentID) {
        return EditDocumentDataModel.getExtendedBuilder()
                .withId(documentID)
                .withCreated(ZonedDateTime.now(ZONE_ID))
                .withLink("document-" + documentID.toString())
                .withTitle("Document #" + documentID)
                .withUser(null)
                .withEnabled(true)
                .withLastModified(ZonedDateTime.now(ZONE_ID))
                .withRawContent("Raw content")
                .build();
    }

    private DocumentDataModel prepareDocumentDataModel(Long documentID) {
        return DocumentDataModel.getBuilder()
                .withId(documentID)
                .withRawContent("Content")
                .withCreated(ZonedDateTime.now(ZONE_ID))
                .withLink("document-" + documentID.toString())
                .withTitle("Document #" + documentID)
                .withUser(null)
                .build();
    }
}
