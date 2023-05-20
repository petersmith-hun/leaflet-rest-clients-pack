package hu.psprog.leaflet.lens.api.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;

/**
 * Domain object holding information for successful password reset notification mail.
 *
 * @author Peter Smith
 */
public record PasswordResetSuccess(
        String username
) implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "pw_reset_confirmation";

    private static final String USERNAME = "username";

    @JsonCreator
    public PasswordResetSuccess {
    }

    @Override
    public Map<String, Object> asContentMap() {
        return Map.of(USERNAME, username);
    }

    @Override
    public String getMailContentType() {
        return MAIL_CONTENT_TYPE;
    }
}
