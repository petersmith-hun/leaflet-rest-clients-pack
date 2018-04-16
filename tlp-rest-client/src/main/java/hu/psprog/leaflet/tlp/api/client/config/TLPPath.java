package hu.psprog.leaflet.tlp.api.client.config;

import hu.psprog.leaflet.bridge.client.request.Path;

/**
 * Enumeration of available TLP paths.
 *
 * @author Peter Smith
 */
public enum TLPPath implements Path {

    LOGS("/logs");

    private String uri;

    TLPPath(String uri) {
        this.uri = uri;
    }

    @Override
    public String getURI() {
        return uri;
    }
}
