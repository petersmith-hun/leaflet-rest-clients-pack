package hu.psprog.leaflet.lsrs.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Response data model for list of directories.
 *
 * @author Peter Smith
 */
@Data
@Builder
@JsonDeserialize(builder = DirectoryListDataModel.DirectoryListDataModelBuilder.class)
public class DirectoryListDataModel {

    private final List<DirectoryDataModel> acceptors;

    @JsonPOJOBuilder(withPrefix = "")
    public static class DirectoryListDataModelBuilder {

    }
}
