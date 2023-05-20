package hu.psprog.leaflet.recaptcha.api.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * ReCaptcha service validation request model.
 *
 * @author Peter Smith
 */
@Data
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
public class ReCaptchaRequest implements Serializable {

    private String secret;
    private String response;
    private String remoteIp;

}
