package hu.psprog.leaflet.recaptcha.api.client.impl;

import hu.psprog.leaflet.bridge.adapter.impl.ReCaptchaRequestBodyAdapter;
import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.recaptcha.api.client.ReCaptchaClient;
import hu.psprog.leaflet.recaptcha.api.client.config.ReCaptchaServicePath;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of {@link ReCaptchaClient}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "grc")
public class ReCaptchaClientImpl implements ReCaptchaClient {

    private final BridgeClient bridgeClient;
    private final ReCaptchaRequestBodyAdapter reCaptchaRequestBodyAdapter;

    @Autowired
    public ReCaptchaClientImpl(BridgeClient bridgeClient, ReCaptchaRequestBodyAdapter reCaptchaRequestBodyAdapter) {
        this.bridgeClient = bridgeClient;
        this.reCaptchaRequestBodyAdapter = reCaptchaRequestBodyAdapter;
    }

    @Override
    public ReCaptchaResponse validate(ReCaptchaRequest reCaptchaRequest) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.POST)
                .path(ReCaptchaServicePath.VERIFY)
                .requestBody(reCaptchaRequest)
                .adapter(reCaptchaRequestBodyAdapter)
                .multipart()
                .build();

        return bridgeClient.call(restRequest, ReCaptchaResponse.class);
    }
}
