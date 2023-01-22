package hu.psprog.leaflet.lsrs.api.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Request model for file uploads.
 *
 * @author Peter Smith
 */
@Data
@Builder
public class FileUploadRequestModel implements Serializable {

    @NotNull
    private final MultipartFile inputFile;

    @Size(max = 255)
    private final String subFolder;

    @Size(max = 255)
    private final String description;
}
