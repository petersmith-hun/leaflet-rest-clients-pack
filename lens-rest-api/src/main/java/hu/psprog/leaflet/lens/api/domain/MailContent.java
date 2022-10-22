package hu.psprog.leaflet.lens.api.domain;

import java.util.Map;

/**
 * Interface for domain classes to be used as mail request objects.
 * Implementing classes must be able to define their contents as a {@link String}-{@link Object} {@link Map}.
 *
 * @author Peter Smith
 */
public interface MailContent {

    /**
     * Returns the contents of the mail request as a {@link String}-{@link Object} {@link Map}.
     *
     * @return contents as map
     */
    Map<String, Object> asContentMap();

    /**
     * Returns the content type identifier of the implementing class.
     *
     * @return attached content type identifier
     */
    String getMailContentType();
}
