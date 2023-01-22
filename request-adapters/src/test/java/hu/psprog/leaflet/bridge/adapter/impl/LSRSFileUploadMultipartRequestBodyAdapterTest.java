package hu.psprog.leaflet.bridge.adapter.impl;

import hu.psprog.leaflet.lsrs.api.request.FileUploadRequestModel;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link LSRSFileUploadMultipartRequestBodyAdapter}.
 *
 * author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class LSRSFileUploadMultipartRequestBodyAdapterTest {

    private static final String DESCRIPTION = "Mocked file upload";
    private static final String SUB_FOLDER = "mocked_test";
    private static final String MOCKED_FILE_NAME = "inputFile";
    private static final String MOCKED_FILE_FILENAME = "mocked_input_file.jpg";
    private static final String MOCKED_FILE_MEDIA_TYPE = "image/jpeg";
    private static final byte[] MOCKED_FILE_CONTENT = "file_content".getBytes();

    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_SUB_FOLDER = "subFolder";
    private static final String FIELD_INPUT_FILE = "inputFile";

    @InjectMocks
    private LSRSFileUploadMultipartRequestBodyAdapter adapter;

    @BeforeEach
    public void setup() throws IllegalAccessException {
        setBaseDirectory();
    }

    @Test
    public void shouldAdaptFileUploadRequest() {

        // given
        FileUploadRequestModel request = prepareFileUploadRequestModel(true);

        // when
        FormDataMultiPart result = adapter.adapt(request);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getBodyParts().size(), equalTo(3));
        assertThat(result.getField(FIELD_DESCRIPTION).getValue(), equalTo(DESCRIPTION));
        assertThat(result.getField(FIELD_SUB_FOLDER).getValue(), equalTo(SUB_FOLDER));
        assertThat(result.getField(FIELD_INPUT_FILE).getMediaType().toString(), equalTo(MOCKED_FILE_MEDIA_TYPE));
    }

    @Test
    public void shouldAdaptFileUploadRequestWithoutFile() {

        // given
        FileUploadRequestModel request = prepareFileUploadRequestModel(false);

        // when
        FormDataMultiPart result = adapter.adapt(request);

        // then
        assertThat(result, notNullValue());
        assertThat(result.getBodyParts().size(), equalTo(2));
        assertThat(result.getField(FIELD_DESCRIPTION).getValue(), equalTo(DESCRIPTION));
        assertThat(result.getField(FIELD_SUB_FOLDER).getValue(), equalTo(SUB_FOLDER));
    }

    private void setBaseDirectory() throws IllegalAccessException {
        Field baseDirectoryField = ReflectionUtils.findField(LSRSFileUploadMultipartRequestBodyAdapter.class, "baseDirectory");
        baseDirectoryField.setAccessible(true);
        baseDirectoryField.set(adapter, System.getProperty("java.io.tmpdir"));
    }

    private FileUploadRequestModel prepareFileUploadRequestModel(boolean withFile) {

        return FileUploadRequestModel.builder()
                .description(DESCRIPTION)
                .subFolder(SUB_FOLDER)
                .inputFile(withFile
                        ? new MockMultipartFile(MOCKED_FILE_NAME, MOCKED_FILE_FILENAME, MOCKED_FILE_MEDIA_TYPE, MOCKED_FILE_CONTENT)
                        : null)
                .build();
    }
}