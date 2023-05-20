package hu.psprog.leaflet.bridge.service.impl;

import hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel;
import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.service.ContactBridgeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of {@link ContactBridgeService}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "leaflet")
public class ContactBridgeServiceImpl implements ContactBridgeService {

    private final BridgeClient bridgeClient;

    @Autowired
    public ContactBridgeServiceImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    public void sendContactRequest(ContactRequestModel contactRequestModel, String recaptchaToken) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.POST)
                .path(LeafletPath.CONTACT)
                .requestBody(contactRequestModel)
                .recaptchaResponse(recaptchaToken)
                .build();

        bridgeClient.call(restRequest);
    }
}
