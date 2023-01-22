package hu.psprog.leaflet.lsrs.api.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Request model for creating a new directory.
 *
 * @author Peter Smith
 */
@Data
@Builder
public class DirectoryCreationRequestModel implements Serializable {

    @NotEmpty
    @Size(max = 255)
    private final String parent;

    @NotEmpty
    @Size(max = 255)
    private final String name;
}
