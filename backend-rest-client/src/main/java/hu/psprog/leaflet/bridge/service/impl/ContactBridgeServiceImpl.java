package hu.psprog.leaflet.bridge.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
@DefaultProperties(groupKey = "leaflet.user")
public class ContactBridgeServiceImpl extends HystrixDefaultConfiguration implements ContactBridgeService {

    private BridgeClient bridgeClient;

    @Autowired
    public ContactBridgeServiceImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    @HystrixCommand
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
