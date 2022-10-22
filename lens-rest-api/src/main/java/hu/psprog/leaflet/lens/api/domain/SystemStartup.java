package hu.psprog.leaflet.lens.api.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Domain class holding system start-up information.
 *
 * @author Peter Smith
 */
@Data
@Builder
public class SystemStartup implements MailContent {

    public static final String MAIL_CONTENT_TYPE = "system_startup";

    private static final String APPLICATION_NAME = "applicationName";
    private static final String VERSION = "version";

    private final String applicationName;
    private final String version;

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
