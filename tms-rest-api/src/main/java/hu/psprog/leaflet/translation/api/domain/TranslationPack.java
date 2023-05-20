package hu.psprog.leaflet.translation.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Translation pack (meta information with definitions) domain class.
 *
 * @author Peter Smith
 */
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
@Jacksonized
public record TranslationPack(
        UUID id,
        String packName,
        Locale locale,
        boolean enabled,
        Date created,
        List<TranslationDefinition> definitions
) implements Serializable { }
