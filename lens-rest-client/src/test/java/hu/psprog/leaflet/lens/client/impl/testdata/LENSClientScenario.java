package hu.psprog.leaflet.lens.client.impl.testdata;

import hu.psprog.leaflet.lens.api.domain.CommentNotification;
import hu.psprog.leaflet.lens.api.domain.ContactRequest;
import hu.psprog.leaflet.lens.api.domain.MailContent;
import hu.psprog.leaflet.lens.api.domain.MailRequestWrapper;
import hu.psprog.leaflet.lens.api.domain.PasswordResetRequest;
import hu.psprog.leaflet.lens.api.domain.PasswordResetSuccess;
import hu.psprog.leaflet.lens.api.domain.SignUpConfirmation;
import hu.psprog.leaflet.lens.api.domain.SystemStartup;

import java.util.List;

/**
 * Test data class for LENS client integration tests.
 *
 * @author Peter Smith
 */
public enum LENSClientScenario implements Scenario {

    COMMENT_NOTIFICATION("/mail/comment_notification") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            MailRequestWrapper<CommentNotification> commentNotification = new MailRequestWrapper<>();
            commentNotification.setOverrideSubjectKey("override-for-test");
            commentNotification.setRecipients(List.of("editor1@dev.local"));
            commentNotification.setContent(CommentNotification.builder()
                    .entryTitle("Entry 1")
                    .content("Comment content")
                    .authorName("Author 1")
                    .authorEmail("author@dev.local")
                    .username("User 1")
                    .email("editor1@dev.local")
                    .build());

            return commentNotification;
        }
    },

    CONTACT_REQUEST("/mail/contact_request") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            MailRequestWrapper<ContactRequest> contactRequest = new MailRequestWrapper<>();
            contactRequest.setReplyTo("reply-to@dev.local");
            contactRequest.setContent(ContactRequest.builder()
                    .name("User 1")
                    .email("reply-to@dev.local")
                    .message("Message of the user")
                    .build());

            return contactRequest;
        }
    },

    PASSWORD_RESET_REQUEST("/mail/pw_reset_request") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            MailRequestWrapper<PasswordResetRequest> passwordResetRequest = new MailRequestWrapper<>();
            passwordResetRequest.setRecipients(List.of("user1@dev.local"));
            passwordResetRequest.setContent(PasswordResetRequest.builder()
                    .expiration(300)
                    .resetLink("http://localhost:9999/pwreset")
                    .token("reset-token")
                    .username("User 1")
                    .build());

            return passwordResetRequest;
        }
    },

    PASSWORD_RESET_CONFIRMATION("/mail/pw_reset_confirmation") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            MailRequestWrapper<PasswordResetSuccess> passwordResetSuccess = new MailRequestWrapper<>();
            passwordResetSuccess.setRecipients(List.of("user1@dev.local"));
            passwordResetSuccess.setContent(new PasswordResetSuccess("User 1"));

            return passwordResetSuccess;
        }
    },

    SIGNUP_CONFIRMATION("/mail/signup_confirmation") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            MailRequestWrapper<SignUpConfirmation> signUpConfirmation = new MailRequestWrapper<>();
            signUpConfirmation.setRecipients(List.of("new-user@dev.local"));
            signUpConfirmation.setContent(new SignUpConfirmation("New User"));

            return signUpConfirmation;
        }
    },

    SYSTEM_STARTUP("/mail/system_startup") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            MailRequestWrapper<SystemStartup> systemStartup = new MailRequestWrapper<>();
            systemStartup.setContent(SystemStartup.builder()
                    .applicationName("app1")
                    .version("1.0.0")
                    .build());

            return systemStartup;
        }
    };

    private final String expectedPath;

    LENSClientScenario(String expectedPath) {
        this.expectedPath = expectedPath;
    }

    @Override
    public String expectedPath() {
        return expectedPath;
    }
}
