package hu.psprog.leaflet.lens.api.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Mail domain for sign-up confirmation mails.
 *
 * @author Peter Smith
 */
@Data
@Builder
public class SignUpConfirmation implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "signup_confirmation";

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
