package hu.psprog.leaflet.translation.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

/**
 * Translation definition model containing a key-value pair.
 *
 * @author Peter Smith
 */
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
@Jacksonized
public record TranslationDefinition(
        String key,
        String value
) implements Serializable { }
