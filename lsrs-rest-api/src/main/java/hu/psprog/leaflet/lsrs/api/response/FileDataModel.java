package hu.psprog.leaflet.lsrs.api.response;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

/**
 * Response data model for uploaded file meta records.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record FileDataModel(
        String originalFilename,
        String reference,
        String acceptedAs,
        String description,
        String path
) { }
