package hu.psprog.leaflet.tlp.api.client.config;

import hu.psprog.leaflet.bridge.client.request.Path;

/**
 * Enumeration of available TLP paths.
 *
 * @author Peter Smith
 */
public enum TLPPath implements Path {

    LOGS("/logs"),
    LOGS_V2("/v2/logs");

    private final String uri;

    TLPPath(String uri) {
        this.uri = uri;
    }

    @Override
    public String getURI() {
        return uri;
    }
}
