package hu.psprog.leaflet.lsrs.api.response;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Response data model for directory meta information.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record DirectoryDataModel(
        String id,
        String root,
        List<String> children,
        List<String> acceptableMimeTypes
) { }
