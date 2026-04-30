package hu.psprog.leaflet.bridge.adapter.impl;

import hu.psprog.leaflet.recaptcha.api.domain.ReCaptchaRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ReCaptchaRequestBodyAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ReCaptchaRequestBodyAdapterTest {

    private static final String FIELD_SECRET = "secret";
    private static final String FIELD_RESPONSE = "response";
    private static final String FIELD_REMOTE_IP = "remoteip";

    private static final String RESPONSE = "recaptcha-svc-response";
    private static final String REMOTE_IP = "remote-ip";
    private static final String SECRET = "secret-1234";

    @InjectMocks
    private ReCaptchaRequestBodyAdapter reCaptchaRequestBodyAdapter;

    @Test
    public void shouldAdapt() throws IOException {

        // given
        ReCaptchaRequest reCaptchaRequest = ReCaptchaRequest.getBuilder()
                .withSecret(SECRET)
                .withRemoteIp(REMOTE_IP)
                .withResponse(RESPONSE)
                .build();

        // when
        HttpEntity result = reCaptchaRequestBodyAdapter.adapt(reCaptchaRequest);

        // then
        assertThat(result, notNullValue());
        var contents = new String(result.getContent().readAllBytes());
        assertFieldPresence(contents, FIELD_SECRET, SECRET);
        assertFieldPresence(contents, FIELD_RESPONSE, RESPONSE);
        assertFieldPresence(contents, FIELD_REMOTE_IP, REMOTE_IP);
    }

    private void assertFieldPresence(String contents, String name, String value) {

        assertThat(contents.contains("Content-Disposition: form-data; name=\"%s\"".formatted(name)), equalTo(true));
        assertThat(contents.contains(value), is(true));
    }
}