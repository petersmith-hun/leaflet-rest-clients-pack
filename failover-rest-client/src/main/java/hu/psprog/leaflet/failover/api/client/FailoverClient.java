package hu.psprog.leaflet.failover.api.client;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.failover.api.domain.StatusResponse;

/**
 * Jersey REST client for CBFS failover application.
 *
 * @author Peter Smith
 */
public interface FailoverClient {

    /**
     * Makes an HTTP request for failover status retrieval.
     *
     * @return response of failover status endpoint as {@link StatusResponse}
     * @throws CommunicationFailureException when the client fails to reach failover application
     */
    StatusResponse getFailoverStatus() throws CommunicationFailureException;
}
