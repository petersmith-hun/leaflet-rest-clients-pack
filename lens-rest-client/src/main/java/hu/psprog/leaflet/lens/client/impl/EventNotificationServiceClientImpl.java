package hu.psprog.leaflet.lens.client.impl;

import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.Path;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.lens.api.domain.MailContent;
import hu.psprog.leaflet.lens.api.domain.MailRequestWrapper;
import hu.psprog.leaflet.lens.client.EventNotificationServiceClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of {@link EventNotificationServiceClient}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "lens")
public class EventNotificationServiceClientImpl implements EventNotificationServiceClient {

    private static final Path MAIL_PATH = () -> "/mail/{mailType}";
    private static final String PATH_PARAMETER_MAIL_TYPE = "mailType";

    private final BridgeClient bridgeClient;

    @Autowired
    public EventNotificationServiceClientImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    public void requestMailNotification(MailRequestWrapper<? extends MailContent> mailRequestWrapper) throws CommunicationFailureException {

        RESTRequest request = RESTRequest.getBuilder()
                .method(RequestMethod.POST)
                .path(MAIL_PATH)
                .addPathParameter(PATH_PARAMETER_MAIL_TYPE, mailRequestWrapper.getContent().getMailContentType())
                .requestBody(mailRequestWrapper)
                .authenticated()
                .build();

        bridgeClient.call(request);
    }
}
