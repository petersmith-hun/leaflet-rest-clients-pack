package hu.psprog.leaflet.lens.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

/**
 * Domain class holding system start-up information.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record SystemStartup(
        String applicationName,
        String version
) implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "system_startup";

    private static final String APPLICATION_NAME = "applicationName";
    private static final String VERSION = "version";

    @Override
    public Map<String, Object> asContentMap() {

        return Map.of(
                APPLICATION_NAME, applicationName,
                VERSION, version
        );
    }

    @Override
    public String getMailContentType() {
        return MAIL_CONTENT_TYPE;
    }
}
