package hu.psprog.leaflet.lens.api.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Domain object holding comment notification information.
 *
 * @author Peter Smith
 */
@Data
@Builder
public class CommentNotification implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "comment_notification";

    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String CONTENT = "content";
    private static final String ENTRY_TITLE = "entryTitle";
    private static final String AUTHOR_NAME = "authorName";

    private final String username;
    private final String email;
    private final String content;
    private final String authorEmail;
    private final String authorName;
    private final String entryTitle;

    @Override
    public Map<String, Object> asContentMap() {

        return Map.of(
                USERNAME, username,
                EMAIL, email,
                CONTENT, content,
                ENTRY_TITLE, entryTitle,
                AUTHOR_NAME, authorEmail
        );
    }

    @Override
    public String getMailContentType() {
        return MAIL_CONTENT_TYPE;
    }
}
