package hu.psprog.leaflet.translation.client.config;

import hu.psprog.leaflet.bridge.client.request.Path;

/**
 * Enumeration of available translation management service paths.
 *
 * @author Peter Smith
 */
public enum TMSPath implements Path {
    
    TRANSLATIONS("/translations"),
    TRANSLATIONS_BY_ID("/translations/{packID}"),
    TRANSLATIONS_STATUS("/translations/{packID}/status");

    private String uri;

    TMSPath(String uri) {
        this.uri = uri;
    }

    @Override
    public String getURI() {
        return uri;
    }
}
