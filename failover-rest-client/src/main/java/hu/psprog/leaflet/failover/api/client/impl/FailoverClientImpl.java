package hu.psprog.leaflet.failover.api.client.impl;

import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.failover.api.client.FailoverClient;
import hu.psprog.leaflet.failover.api.client.config.FailoverPath;
import hu.psprog.leaflet.failover.api.domain.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of {@link FailoverClient}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "cbfs")
public class FailoverClientImpl implements FailoverClient {

    private final BridgeClient bridgeClient;

    @Autowired
    public FailoverClientImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    public StatusResponse getFailoverStatus() throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(FailoverPath.STATUS)
                .build();

        return bridgeClient.call(restRequest, StatusResponse.class);
    }
}
