package hu.psprog.leaflet.lsrs.client.config;

import hu.psprog.leaflet.bridge.client.request.Path;

/**
 * Enumeration of available LSRS service paths.
 *
 * @author Peter Smith
 */
public enum LSRSPath implements Path {

    FILES("/files"),
    FILES_ONLY_UUID("/files/{fileIdentifier}"),
    FILES_BY_ID("/files/{fileIdentifier}/{storedFilename}"),
    FILES_DIRECTORIES("/files/directories");

    private final String uri;

    LSRSPath(String uri) {
        this.uri = uri;
    }

    @Override
    public String getURI() {
        return uri;
    }
}
