package hu.psprog.leaflet.lens.api.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import java.util.Map;

/**
 * Domain object holding information for successful password reset notification mail.
 *
 * @author Peter Smith
 */
@Data
public class PasswordResetSuccess implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "pw_reset_confirmation";

    private static final String USERNAME = "username";

    private final String username;

    @JsonCreator
    public PasswordResetSuccess(String username) {
        this.username = username;
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
