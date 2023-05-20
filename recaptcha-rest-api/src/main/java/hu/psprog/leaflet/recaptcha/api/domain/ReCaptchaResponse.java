package hu.psprog.leaflet.recaptcha.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;

/**
 * ReCaptcha service validation response model.
 *
 * @author Peter Smith
 */
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public record ReCaptchaResponse(

        @JsonProperty("success")
        boolean successful,

        @JsonProperty("challenge_ts")
        Date challengeTimeStamp,

        String hostname,

        @JsonProperty("error-codes")
        List<ReCaptchaErrorCode> errorCodes
) { }
