package hu.psprog.leaflet.translation.api.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

/**
 * Creation request model for translation packs.
 *
 * @author Peter Smith
 */
@Data
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
@Jacksonized
public class TranslationPackCreationRequest implements Serializable {

    @NotNull
    private Locale locale;

    @NotEmpty
    private String packName;

    @NotEmpty
    private Map<String, String> definitions;

}
