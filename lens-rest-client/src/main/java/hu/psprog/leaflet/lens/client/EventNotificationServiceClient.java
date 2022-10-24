package hu.psprog.leaflet.lens.client;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.lens.api.domain.MailContent;
import hu.psprog.leaflet.lens.api.domain.MailRequestWrapper;

/**
 * Event Notification service HTTP client interface.
 *
 * @author Peter Smith
 */
public interface EventNotificationServiceClient {

    /**
     * Requests submitting the defined mail notification via LENS.
     *
     * @param mailRequestWrapper {@link MailRequestWrapper} object containing the mail's base information along with its content
     * @throws CommunicationFailureException on service communication error
     */
    void requestMailNotification(MailRequestWrapper<? extends MailContent> mailRequestWrapper) throws CommunicationFailureException;
}
