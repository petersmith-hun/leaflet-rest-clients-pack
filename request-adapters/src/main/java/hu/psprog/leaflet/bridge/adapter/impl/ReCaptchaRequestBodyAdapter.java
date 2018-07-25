package hu.psprog.leaflet.bridge.adapter.impl;

import hu.psprog.leaflet.bridge.adapter.RequestBodyAdapter;
import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * {@link RequestBodyAdapter} implementation for ReCaptcha requests.
 * ReCaptcha service expects validation requests as form data, therefore source request object is converted to multipart/form-data.
 *
 * @author Peter Smith
 */
@Component
public class ReCaptchaRequestBodyAdapter extends AbstractMultipartRequestBodyAdapter<ReCaptchaRequest> {

    private static final String FIELD_SECRET = "secret";
    private static final String FIELD_RESPONSE = "response";
    private static final String FIELD_REMOTE_IP = "remoteip";

    public ReCaptchaRequestBodyAdapter(@Value("${java.io.tmpdir}") String baseDirectory) {
        super(baseDirectory);
    }

    @Override
    public FormDataMultiPart adapt(ReCaptchaRequest source) {

        return getMultipartBuilderFor(source)
                .withStringField(FIELD_SECRET, ReCaptchaRequest::getSecret)
                .withStringField(FIELD_RESPONSE, ReCaptchaRequest::getResponse)
                .withStringField(FIELD_REMOTE_IP, ReCaptchaRequest::getRemoteIp)
                .build();
    }
}
