package hu.psprog.leaflet.translation.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Translation pack meta information domain class.
 *
 * @author Peter Smith
 */
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
@Jacksonized
public record TranslationPackMetaInfo(
        UUID id,
        String packName,
        Locale locale,
        boolean enabled,
        Date created
) implements Serializable { }
