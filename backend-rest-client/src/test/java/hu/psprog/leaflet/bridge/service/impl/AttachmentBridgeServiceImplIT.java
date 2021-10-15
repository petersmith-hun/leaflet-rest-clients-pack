package hu.psprog.leaflet.bridge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.api.rest.request.attachment.AttachmentRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.it.config.BridgeITSuite;
import hu.psprog.leaflet.bridge.service.AttachmentBridgeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

/**
 * Integration tests for {@link AttachmentBridgeServiceImpl}.
 *
 * @author Peter Smith
 */
@BridgeITSuite
public class AttachmentBridgeServiceImplIT extends WireMockBaseTest {

    @Autowired
    private AttachmentBridgeService attachmentBridgeService;

    private StringValuePattern requestBody;
    private AttachmentRequestModel attachmentRequestModel;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        attachmentRequestModel = new AttachmentRequestModel();
        attachmentRequestModel.setEntryID(10L);
        attachmentRequestModel.setPathUUID(UUID.nameUUIDFromBytes("uuid".getBytes()));
        requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(attachmentRequestModel));
    }

    @Test
    public void shouldAttach() throws CommunicationFailureException {

        // given
        givenThat(post(LeafletPath.ATTACHMENTS.getURI())
                .withRequestBody(requestBody)
                .willReturn(aResponse().withStatus(201)));

        // when
        attachmentBridgeService.attach(attachmentRequestModel);

        // then
        verify(postRequestedFor(urlEqualTo(LeafletPath.ATTACHMENTS.getURI()))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldDetach() throws CommunicationFailureException {

        // given
        givenThat(put(LeafletPath.ATTACHMENTS.getURI())
                .withRequestBody(requestBody)
                .willReturn(aResponse().withStatus(204)));

        // when
        attachmentBridgeService.detach(attachmentRequestModel);

        // then
        verify(putRequestedFor(urlEqualTo(LeafletPath.ATTACHMENTS.getURI()))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }
}
