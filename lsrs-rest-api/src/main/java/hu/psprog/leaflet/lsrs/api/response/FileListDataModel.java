package hu.psprog.leaflet.lsrs.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Response data model for list of files.
 *
 * @author Peter Smith
 */
@Data
@Builder
@JsonDeserialize(builder = FileListDataModel.FileListDataModelBuilder.class)
public class FileListDataModel {

    private List<FileDataModel> files;

    @JsonPOJOBuilder(withPrefix = "")
    public static class FileListDataModelBuilder {

    }
}
