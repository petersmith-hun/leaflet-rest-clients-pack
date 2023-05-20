package hu.psprog.leaflet.bridge.service.impl;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.service.SitemapBridgeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of {@link SitemapBridgeService}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "leaflet")
public class SitemapBridgeServiceImpl implements SitemapBridgeService {

    private static final String HTTP_HEADER_ACCEPT = "Accept";
    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    private final BridgeClient bridgeClient;

    @Autowired
    public SitemapBridgeServiceImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    public Sitemap getSitemap() throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.SITEMAP)
                .addHeaderParameter(HTTP_HEADER_ACCEPT, CONTENT_TYPE_APPLICATION_JSON)
                .build();

        return bridgeClient.call(restRequest, Sitemap.class);
    }
}
