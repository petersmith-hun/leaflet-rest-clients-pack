package hu.psprog.leaflet.lsrs.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

/**
 * Response data model for uploaded file meta records.
 *
 * @author Peter Smith
 */
@Data
@Builder
@JsonDeserialize(builder = FileDataModel.FileDataModelBuilder.class)
public class FileDataModel {

    private final String originalFilename;
    private final String reference;
    private final String acceptedAs;
    private final String description;
    private final String path;

    @JsonPOJOBuilder(withPrefix = "")
    public static class FileDataModelBuilder {

    }
}
