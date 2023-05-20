package hu.psprog.leaflet.lsrs.api.response;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Response data model for list of files.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record FileListDataModel(
        List<FileDataModel> files
) { }
