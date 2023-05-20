package hu.psprog.leaflet.failover.api.client.config;

import hu.psprog.leaflet.bridge.client.request.Path;

/**
 * Enumeration of available CBFS paths.
 *
 * @author Peter Smith
 */
public enum FailoverPath implements Path {

    STATUS("/status");

    private final String uri;

    FailoverPath(String uri) {
        this.uri = uri;
    }

    @Override
    public String getURI() {
        return uri;
    }
}
