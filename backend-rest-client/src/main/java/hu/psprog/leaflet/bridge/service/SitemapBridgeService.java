package hu.psprog.leaflet.bridge.service;

import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;

/**
 * BridgeClient interface for sitemap related calls.
 *
 * @author Peter Smith
 */
public interface SitemapBridgeService {

    /**
     * Retrieves generated sitemap.
     *
     * @return generated sitemap as {@link Sitemap}
     * @throws CommunicationFailureException if client fails to reach backend application
     */
    Sitemap getSitemap() throws CommunicationFailureException;
}
