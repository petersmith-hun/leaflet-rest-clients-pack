package hu.psprog.leaflet.lsrs.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Response data model for directory meta information.
 *
 * @author Peter Smith
 */
@Data
@Builder
@JsonDeserialize(builder = DirectoryDataModel.DirectoryDataModelBuilder.class)
public class DirectoryDataModel {

    private final String id;
    private final String root;
    private final List<String> children;
    private final List<String> acceptableMimeTypes;

    @JsonPOJOBuilder(withPrefix = "")
    public static class DirectoryDataModelBuilder {

    }
}
