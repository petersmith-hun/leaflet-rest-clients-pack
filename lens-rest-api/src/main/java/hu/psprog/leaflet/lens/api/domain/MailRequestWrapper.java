package hu.psprog.leaflet.lens.api.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Common "frame" class of mail requests.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record MailRequestWrapper<T extends MailContent>(
        List<String> recipients,
        String replyTo,
        String overrideSubjectKey,
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_contentType") @JsonSubTypes({
                @JsonSubTypes.Type(value = CommentNotification.class, name = CommentNotification.MAIL_CONTENT_TYPE),
                @JsonSubTypes.Type(value = ContactRequest.class, name = ContactRequest.MAIL_CONTENT_TYPE),
                @JsonSubTypes.Type(value = PasswordResetRequest.class, name = PasswordResetRequest.MAIL_CONTENT_TYPE),
                @JsonSubTypes.Type(value = PasswordResetSuccess.class, name = PasswordResetSuccess.MAIL_CONTENT_TYPE),
                @JsonSubTypes.Type(value = SignUpConfirmation.class, name = SignUpConfirmation.MAIL_CONTENT_TYPE),
                @JsonSubTypes.Type(value = SystemStartup.class, name = SystemStartup.MAIL_CONTENT_TYPE)
        }) T content
) implements Serializable {

    public static class MailRequestWrapperBuilder<T extends MailContent> {

        public MailRequestWrapperBuilder<T> recipients(String... recipients) {
            this.recipients = Objects.isNull(recipients)
                    ? Collections.emptyList()
                    : Arrays.asList(recipients);
            return this;
        }
    }
}
