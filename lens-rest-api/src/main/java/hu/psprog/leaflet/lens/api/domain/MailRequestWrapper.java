package hu.psprog.leaflet.lens.api.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Common "frame" class of mail requests.
 *
 * @author Peter Smith
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailRequestWrapper<T extends MailContent> implements Serializable {

    private List<String> recipients;
    private String replyTo;
    private String overrideSubjectKey;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_contentType")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = CommentNotification.class, name = CommentNotification.MAIL_CONTENT_TYPE),
            @JsonSubTypes.Type(value = ContactRequest.class, name = ContactRequest.MAIL_CONTENT_TYPE),
            @JsonSubTypes.Type(value = PasswordResetRequest.class, name = PasswordResetRequest.MAIL_CONTENT_TYPE),
            @JsonSubTypes.Type(value = PasswordResetSuccess.class, name = PasswordResetSuccess.MAIL_CONTENT_TYPE),
            @JsonSubTypes.Type(value = SignUpConfirmation.class, name = SignUpConfirmation.MAIL_CONTENT_TYPE),
            @JsonSubTypes.Type(value = SystemStartup.class, name = SystemStartup.MAIL_CONTENT_TYPE)
    })
    private T content;

    public static class MailRequestWrapperBuilder<T extends MailContent> {

        public MailRequestWrapperBuilder<T> recipients(String... recipients) {
            this.recipients = Arrays.asList(recipients);
            return this;
        }
    }
}
