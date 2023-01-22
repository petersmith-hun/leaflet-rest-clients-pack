package hu.psprog.leaflet.lsrs.api.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Request model for updating file meta information.
 *
 * @author Peter Smith
 */
@Data
@Builder
public class UpdateFileMetaInfoRequestModel implements Serializable {

    @NotEmpty
    @Size(max = 255)
    private final String originalFilename;

    @Size(max = 255)
    private final String description;
}
