package hu.psprog.leaflet.bridge.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
@DefaultProperties(groupKey = "leaflet.sitemap")
public class SitemapBridgeServiceImpl extends HystrixDefaultConfiguration implements SitemapBridgeService {

    private static final String HTTP_HEADER_ACCEPT = "Accept";
    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    private BridgeClient bridgeClient;

    @Autowired
    public SitemapBridgeServiceImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    @HystrixCommand
    public Sitemap getSitemap() throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LeafletPath.SITEMAP)
                .addHeaderParameter(HTTP_HEADER_ACCEPT, CONTENT_TYPE_APPLICATION_JSON)
                .build();

        return bridgeClient.call(restRequest, Sitemap.class);
    }
}
