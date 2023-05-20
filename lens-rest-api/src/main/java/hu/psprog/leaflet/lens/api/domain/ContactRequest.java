package hu.psprog.leaflet.lens.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

/**
 * Domain object holding contact request information.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record ContactRequest(
        String name,
        String email,
        String message
) implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "contact_request";

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String MESSAGE = "message";

    @Override
    public Map<String, Object> asContentMap() {

        return Map.of(
                NAME, name,
                EMAIL, email,
                MESSAGE, message
        );
    }

    @Override
    public String getMailContentType() {
        return MAIL_CONTENT_TYPE;
    }
}
