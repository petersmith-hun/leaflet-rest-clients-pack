package hu.psprog.leaflet.recaptcha.api.client.config;

import hu.psprog.leaflet.bridge.client.request.Path;

/**
 * ReCaptcha service path.
 *
 * @author Peter Smith
 */
public enum ReCaptchaServicePath implements Path {

    VERIFY("/siteverify");

    private final String uri;

    ReCaptchaServicePath(String uri) {
        this.uri = uri;
    }

    @Override
    public String getURI() {
        return uri;
    }
}
