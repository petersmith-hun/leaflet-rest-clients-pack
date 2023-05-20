package hu.psprog.leaflet.lens.client.impl.testdata;

import hu.psprog.leaflet.lens.api.domain.CommentNotification;
import hu.psprog.leaflet.lens.api.domain.ContactRequest;
import hu.psprog.leaflet.lens.api.domain.MailContent;
import hu.psprog.leaflet.lens.api.domain.MailRequestWrapper;
import hu.psprog.leaflet.lens.api.domain.PasswordResetRequest;
import hu.psprog.leaflet.lens.api.domain.PasswordResetSuccess;
import hu.psprog.leaflet.lens.api.domain.SignUpConfirmation;
import hu.psprog.leaflet.lens.api.domain.SystemStartup;

/**
 * Test data class for LENS client integration tests.
 *
 * @author Peter Smith
 */
public enum LENSClientScenario implements Scenario {

    COMMENT_NOTIFICATION("/mail/comment_notification") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            return MailRequestWrapper.<CommentNotification>builder()
                    .overrideSubjectKey("override-for-test")
                    .recipients("editor1@dev.local")
                    .content(CommentNotification.builder()
                            .entryTitle("Entry 1")
                            .content("Comment content")
                            .authorName("Author 1")
                            .authorEmail("author@dev.local")
                            .username("User 1")
                            .email("editor1@dev.local")
                            .build())
                    .build();
        }
    },

    CONTACT_REQUEST("/mail/contact_request") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            return MailRequestWrapper.<ContactRequest>builder()
                    .replyTo("reply-to@dev.local")
                    .content(ContactRequest.builder()
                            .name("User 1")
                            .email("reply-to@dev.local")
                            .message("Message of the user")
                            .build())
                    .build();
        }
    },

    PASSWORD_RESET_REQUEST("/mail/pw_reset_request") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            return MailRequestWrapper.<PasswordResetRequest>builder()
                    .recipients("user1@dev.local")
                    .content(PasswordResetRequest.builder()
                            .expiration(300)
                            .resetLink("http://localhost:9999/pwreset")
                            .token("reset-token")
                            .username("User 1")
                            .build())
                    .build();
        }
    },

    PASSWORD_RESET_CONFIRMATION("/mail/pw_reset_confirmation") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            return MailRequestWrapper.<PasswordResetSuccess>builder()
                    .recipients("user1@dev.local")
                    .content(new PasswordResetSuccess("User 1"))
                    .build();
        }
    },

    SIGNUP_CONFIRMATION("/mail/signup_confirmation") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            return MailRequestWrapper.<SignUpConfirmation>builder()
                    .recipients("new-user@dev.local")
                    .content(new SignUpConfirmation("New User"))
                    .build();
        }
    },

    SYSTEM_STARTUP("/mail/system_startup") {

        @Override
        public MailRequestWrapper<? extends MailContent> request() {

            return MailRequestWrapper.<SystemStartup>builder()
                    .content(SystemStartup.builder()
                            .applicationName("app1")
                            .version("1.0.0")
                            .build())
                    .build();
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
