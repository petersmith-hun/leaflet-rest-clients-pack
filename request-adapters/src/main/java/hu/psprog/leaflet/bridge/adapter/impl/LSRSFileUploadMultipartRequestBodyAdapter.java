package hu.psprog.leaflet.bridge.adapter.impl;

import hu.psprog.leaflet.bridge.adapter.RequestBodyAdapter;
import hu.psprog.leaflet.lsrs.api.request.FileUploadRequestModel;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * {@link RequestBodyAdapter} implementation for {@link FileUploadRequestModel}.
 * Adapter will convert the {@link FileUploadRequestModel} to Jersey-compatible
 * multipart/form-data wrapper object.
 *
 * @author Peter Smith
 */
@Component
public class LSRSFileUploadMultipartRequestBodyAdapter extends AbstractMultipartRequestBodyAdapter<FileUploadRequestModel> {

    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_SUB_FOLDER = "subFolder";
    private static final String FIELD_INPUT_FILE = "inputFile";

    @Autowired
    public LSRSFileUploadMultipartRequestBodyAdapter(@Value("${java.io.tmpdir}") String baseDirectory) {
        super(baseDirectory);
    }

    @Override
    public FormDataMultiPart adapt(FileUploadRequestModel source) {

        return getMultipartBuilderFor(source)
                .withFileField(FIELD_INPUT_FILE, FileUploadRequestModel::getInputFile)
                .withStringField(FIELD_DESCRIPTION, FileUploadRequestModel::getDescription)
                .withStringField(FIELD_SUB_FOLDER, FileUploadRequestModel::getSubFolder)
                .build();
    }
}
