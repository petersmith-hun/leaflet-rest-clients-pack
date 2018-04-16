package hu.psprog.leaflet.tlp.api.client.impl;

import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.tlp.api.client.TLPClient;
import hu.psprog.leaflet.tlp.api.client.config.TLPPath;
import hu.psprog.leaflet.tlp.api.domain.LogEventPage;
import hu.psprog.leaflet.tlp.api.domain.LogRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of {@link TLPClient}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "tlp")
public class TLPClientImpl implements TLPClient {

    private static final String QUERY_PARAM_PAGE = "page";
    private static final String QUERY_PARAM_LIMIT = "limit";
    private static final String QUERY_PARAM_ORDER_BY = "orderBy";
    private static final String QUERY_PARAM_ORDER_DIRECTION = "orderDirection";
    private static final String QUERY_PARAM_SOURCE = "source";
    private static final String QUERY_PARAM_LEVEL = "level";
    private static final String QUERY_PARAM_FROM = "from";
    private static final String QUERY_PARAM_TO = "to";
    private static final String QUERY_PARAM_CONTENT = "content";
    
    private BridgeClient bridgeClient;

    @Autowired
    public TLPClientImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    public LogEventPage getLogs(LogRequest logRequest) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(TLPPath.LOGS)
                .addRequestParameters(QUERY_PARAM_PAGE, String.valueOf(logRequest.getPage()))
                .addRequestParameters(QUERY_PARAM_LIMIT, String.valueOf(logRequest.getLimit()))
                .addRequestParameters(QUERY_PARAM_ORDER_BY, logRequest.getOrderBy().name())
                .addRequestParameters(QUERY_PARAM_ORDER_DIRECTION, logRequest.getOrderDirection().name())
                .addRequestParameters(QUERY_PARAM_SOURCE, logRequest.getSource())
                .addRequestParameters(QUERY_PARAM_LEVEL, logRequest.getLevel())
                .addRequestParameters(QUERY_PARAM_FROM, logRequest.getFrom())
                .addRequestParameters(QUERY_PARAM_TO, logRequest.getTo())
                .addRequestParameters(QUERY_PARAM_CONTENT, logRequest.getContent())
                .build();

        return bridgeClient.call(restRequest, LogEventPage.class);
    }
}
