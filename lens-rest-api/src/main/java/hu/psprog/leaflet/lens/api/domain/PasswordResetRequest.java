package hu.psprog.leaflet.lens.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

/**
 * Domain object holding password reset request information for reset mail.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record PasswordResetRequest(
        String username,
        String token,
        String resetLink,
        int expiration
) implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "pw_reset_request";

    private static final String USERNAME = "username";
    private static final String TOKEN = "token";
    private static final String RESET_LINK = "resetLink";
    private static final String EXPIRATION = "expiration";

    @Override
    public Map<String, Object> asContentMap() {

        return Map.of(
                USERNAME, username,
                TOKEN, token,
                RESET_LINK, resetLink,
                EXPIRATION, expiration
        );
    }

    @Override
    public String getMailContentType() {
        return MAIL_CONTENT_TYPE;
    }
}
