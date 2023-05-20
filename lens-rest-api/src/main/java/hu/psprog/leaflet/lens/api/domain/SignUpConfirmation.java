package hu.psprog.leaflet.lens.api.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;

/**
 * Mail domain for sign-up confirmation mails.
 *
 * @author Peter Smith
 */
public record SignUpConfirmation(
        String username
) implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "signup_confirmation";

    private static final String USERNAME = "username";

    @JsonCreator
    public SignUpConfirmation {
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
