package hu.psprog.leaflet.tlp.api.client;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.tlp.api.domain.LogEventPage;
import hu.psprog.leaflet.tlp.api.domain.LogRequest;

/**
 * Jersey REST client for Tiny Log Processor application.
 *
 * @author Peter Smith
 */
public interface TLPClient {

    /**
     * Makes an HTTP request for TLP logs retrieval.
     *
     * @param logRequest log retrieval paging and filtering settings as {@link LogRequest} object
     * @return response of TLP logs endpoint as {@link LogEventPage}
     * @throws CommunicationFailureException when the client fails to reach TLP application
     */
    LogEventPage getLogs(LogRequest logRequest) throws CommunicationFailureException;

    /**
     * Makes an HTTP request for TLP logs retrieval via TLPv2 API.
     *
     * @param logRequest TLQL log query string
     * @return response of TLP logs endpoint as {@link LogEventPage}
     * @throws CommunicationFailureException when the client fails to reach TLP application
     */
    LogEventPage getLogs(String logRequest) throws CommunicationFailureException;
}
