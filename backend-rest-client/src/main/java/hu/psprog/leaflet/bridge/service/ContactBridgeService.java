package hu.psprog.leaflet.bridge.service;

import hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;

/**
 * BridgeClient interface for contact request related operations.
 *
 * @author Peter Smith
 */
public interface ContactBridgeService {

    /**
     * Sends the given contact request.
     *
     * @param contactRequestModel {@link ContactRequestModel} object
     * @param recaptchaToken ReCaptcha response token
     * @throws CommunicationFailureException if client fails to reach backend application
     */
    void sendContactRequest(ContactRequestModel contactRequestModel, String recaptchaToken) throws CommunicationFailureException;
}
