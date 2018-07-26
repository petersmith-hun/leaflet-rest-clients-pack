package hu.psprog.leaflet.bridge.adapter.impl;

import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ReCaptchaRequestBodyAdapter}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class ReCaptchaRequestBodyAdapterTest {

    private static final String FIELD_SECRET = "secret";
    private static final String FIELD_RESPONSE = "response";
    private static final String FIELD_REMOTE_IP = "remoteip";

    private static final String RESPONSE = "recaptcha-svc-response";
    private static final String REMOTE_IP = "remote-ip";
    private static final String SECRET = "secret";

    @InjectMocks
    private ReCaptchaRequestBodyAdapter reCaptchaRequestBodyAdapter;

    @Test
    public void shouldAdapt() {

        // given
        ReCaptchaRequest reCaptchaRequest = ReCaptchaRequest.getBuilder()
                .withSecret(SECRET)
                .withRemoteIp(REMOTE_IP)
                .withResponse(RESPONSE)
                .build();

        // when
        FormDataMultiPart result = reCaptchaRequestBodyAdapter.adapt(reCaptchaRequest);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getBodyParts().size(), equalTo(3));
        assertThat(result.getField(FIELD_REMOTE_IP).getValue(), equalTo(REMOTE_IP));
        assertThat(result.getField(FIELD_RESPONSE).getValue(), equalTo(RESPONSE));
        assertThat(result.getField(FIELD_SECRET).getValue(), equalTo(SECRET));
    }
}