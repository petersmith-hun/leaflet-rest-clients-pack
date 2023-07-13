package hu.psprog.leaflet.lsrs.api.response;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Response data model for VFS browser.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record VFSBrowserModel(
        String parent,
        String currentPath,
        List<FolderDataModel> directories,
        List<FileDataModel> files
) { }
