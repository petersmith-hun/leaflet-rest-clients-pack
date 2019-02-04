package hu.psprog.leaflet.bridge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.it.config.BridgeITSuite;
import hu.psprog.leaflet.bridge.service.ContactBridgeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static hu.psprog.leaflet.bridge.client.domain.BridgeConstants.X_CAPTCHA_RESPONSE;

/**
 * Integration tests for {@link ContactBridgeServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@BridgeITSuite
public class ContactBridgeServiceImplIT extends WireMockBaseTest {

    private static final String RECAPTCHA_TOKEN = "recaptcha-token";

    @Autowired
    private ContactBridgeService contactBridgeService;

    private StringValuePattern requestBody;
    private ContactRequestModel contactRequestModel;

    @Before
    public void setup() throws JsonProcessingException {
        contactRequestModel = new ContactRequestModel();
        contactRequestModel.setName("Contact IT");
        contactRequestModel.setEmail("contact_it@dev.local");
        contactRequestModel.setMessage("Contact message");
        requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(contactRequestModel));
    }

    @Test
    public void shouldSendContactRequest() throws CommunicationFailureException {

        // given
        givenThat(post(LeafletPath.CONTACT.getURI())
                .withRequestBody(requestBody)
                .willReturn(aResponse().withStatus(201)));

        // when
        contactBridgeService.sendContactRequest(contactRequestModel, RECAPTCHA_TOKEN);

        // then
        verify(postRequestedFor(urlEqualTo(LeafletPath.CONTACT.getURI()))
                .withRequestBody(requestBody)
                .withHeader(X_CAPTCHA_RESPONSE, equalTo(RECAPTCHA_TOKEN)));
    }
}
