package hu.psprog.leaflet.lsrs.api.response;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

/**
 * Response data model for folders in a VFSBrowserModel.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record FolderDataModel(
        String folderName,
        String absolutePath
) { }
