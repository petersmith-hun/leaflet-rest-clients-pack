package hu.psprog.leaflet.recaptcha.api.client;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaResponse;

/**
 * Bridge client for Google ReCaptcha service.
 *
 * @author Peter Smith
 */
public interface ReCaptchaClient {

    /**
     * Calls ReCaptcha service for response token validation.
     *
     * @param reCaptchaRequest validation request model as {@link ReCaptchaRequest}
     * @return validation result as {@link ReCaptchaResponse}
     * @throws CommunicationFailureException if Bridge could not reach ReCaptcha validation service
     */
    ReCaptchaResponse validate(ReCaptchaRequest reCaptchaRequest) throws CommunicationFailureException;
}
