package hu.psprog.leaflet.lens.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

/**
 * Domain object holding comment notification information.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record CommentNotification(
        String username,
        String email,
        String content,
        String authorEmail,
        String authorName,
        String entryTitle
) implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "comment_notification";

    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String CONTENT = "content";
    private static final String ENTRY_TITLE = "entryTitle";
    private static final String AUTHOR_NAME = "authorName";

    @Override
    public Map<String, Object> asContentMap() {

        return Map.of(
                USERNAME, username,
                EMAIL, email,
                CONTENT, content,
                ENTRY_TITLE, entryTitle,
                AUTHOR_NAME, authorName
        );
    }

    @Override
    public String getMailContentType() {
        return MAIL_CONTENT_TYPE;
    }
}
