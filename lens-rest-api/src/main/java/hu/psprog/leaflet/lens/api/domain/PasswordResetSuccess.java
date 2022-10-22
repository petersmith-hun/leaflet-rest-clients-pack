package hu.psprog.leaflet.lens.api.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Domain object holding information for successful password reset notification mail.
 *
 * @author Peter Smith
 */
@Data
@Builder
public class PasswordResetSuccess implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "pw_reset_confirmation";

    private static final String USERNAME = "username";

    private final String username;

    @Override
    public Map<String, Object> asContentMap() {
        return Map.of(USERNAME, username);
    }

    @Override
    public String getMailContentType() {
        return MAIL_CONTENT_TYPE;
    }
}
