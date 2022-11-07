package hu.psprog.leaflet.lens.api.domain;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

/**
 * Domain object holding contact request information.
 *
 * @author Peter Smith
 */
@Data
@Builder
@Jacksonized
public class ContactRequest implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "contact_request";

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String MESSAGE = "message";

    private final String name;
    private final String email;
    private final String message;

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
