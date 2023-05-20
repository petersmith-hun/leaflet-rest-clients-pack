package hu.psprog.leaflet.recaptcha.api.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * Possible ReCaptcha service error response codes.
 *
 * @author Peter Smith
 */
public enum ReCaptchaErrorCode {

    MISSING_INPUT_SECRET("missing-input-secret"),
    INVALID_INPUT_SECRET("invalid-input-secret"),
    MISSING_INPUT_RESPONSE("missing-input-response"),
    INVALID_INPUT_RESPONSE("invalid-input-response"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate"),
    BAD_REQUEST("bad-request");

    private final String rawErrorCode;

    ReCaptchaErrorCode(String rawErrorCode) {
        this.rawErrorCode = rawErrorCode;
    }

    public String getRawErrorCode() {
        return rawErrorCode;
    }

    @JsonCreator
    public static ReCaptchaErrorCode getByRawErrorCode(String rawErrorCode) {

        return Arrays.stream(ReCaptchaErrorCode.values())
                .filter(errorCode -> errorCode.getRawErrorCode().equals(rawErrorCode))
                .findFirst()
                .orElse(null);
    }
}
