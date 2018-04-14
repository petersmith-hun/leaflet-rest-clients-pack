package hu.psprog.leaflet.bridge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.api.rest.request.file.DirectoryCreationRequestModel;
import hu.psprog.leaflet.api.rest.request.file.FileUploadRequestModel;
import hu.psprog.leaflet.api.rest.request.file.UpdateFileMetaInfoRequestModel;
import hu.psprog.leaflet.api.rest.response.file.DirectoryDataModel;
import hu.psprog.leaflet.api.rest.response.file.DirectoryListDataModel;
import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.api.rest.response.file.FileListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.it.config.BridgeITSuite;
import hu.psprog.leaflet.bridge.service.FileBridgeService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link FileBridgeServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@BridgeITSuite
public class FileBridgeServiceImplIT extends WireMockBaseTest {

    @Autowired
    private FileBridgeService fileBridgeService;

    @Test
    public void shouldGetUploadedFiles() throws CommunicationFailureException {

        // given
        FileListDataModel fileListDataModel = prepareFileListDataModel();
        givenThat(get(LeafletPath.FILES.getURI()).willReturn(ResponseDefinitionBuilder
                .okForJson(fileListDataModel)));

        // when
        FileListDataModel result = fileBridgeService.getUploadedFiles();

        // then
        assertThat(result, equalTo(fileListDataModel));
        verify(getRequestedFor(urlEqualTo(LeafletPath.FILES.getURI()))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldDownloadFile() throws CommunicationFailureException, IOException {

        // given
        UUID fileIdentifier = UUID.randomUUID();
        String filename = "filename";
        String uri = prepareURI(LeafletPath.FILES_BY_ID.getURI(), fileIdentifier, filename);
        String responseBody = "responseBody";
        givenThat(get(uri)
                .willReturn(ResponseDefinitionBuilder.responseDefinition().withBody(responseBody.getBytes())));

        // when
        InputStream result = fileBridgeService.downloadFile(fileIdentifier, filename);

        // then
        verify(getRequestedFor(urlEqualTo(uri)));
        assertThat(new String(IOUtils.toByteArray(result)), equalTo(responseBody));
    }

    @Test
    public void shouldGetFileDetails() throws CommunicationFailureException {

        // given
        FileDataModel fileDataModel = prepareFileDataModel();
        UUID fileIdentifier = UUID.randomUUID();
        String uri = prepareURI(LeafletPath.FILES_ONLY_UUID.getURI(), fileIdentifier);
        givenThat(get(uri)
                .willReturn(ResponseDefinitionBuilder.okForJson(fileDataModel)));

        // when
        FileDataModel result = fileBridgeService.getFileDetails(fileIdentifier);

        // then
        assertThat(result, equalTo(fileDataModel));
        verify(getRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldUploadFile() throws CommunicationFailureException, JsonProcessingException {

        // given
        FileUploadRequestModel fileUploadRequestModel = prepareFileUploadRequestModel();
        FileDataModel fileDataModel = prepareFileDataModel();
        givenThat(post(LeafletPath.FILES.getURI())
                .willReturn(ResponseDefinitionBuilder.okForJson(fileDataModel)));

        // when
        FileDataModel result = fileBridgeService.uploadFile(fileUploadRequestModel);

        // then
        assertThat(result, equalTo(fileDataModel));
        verify(postRequestedFor(urlEqualTo(LeafletPath.FILES.getURI()))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldDeleteFile() throws CommunicationFailureException {

        // given
        UUID fileIdentifier = UUID.randomUUID();
        String uri = prepareURI(LeafletPath.FILES_ONLY_UUID.getURI(), fileIdentifier);
        givenThat(delete(uri));

        // when
        fileBridgeService.deleteFile(fileIdentifier);

        // then
        verify(deleteRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldCreateDirectory() throws CommunicationFailureException, JsonProcessingException {

        // given
        DirectoryCreationRequestModel directoryCreationRequestModel = prepareDirectoryCreationRequestModel();
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(directoryCreationRequestModel));
        givenThat(post(LeafletPath.FILES_DIRECTORIES.getURI())
                .withRequestBody(requestBody));

        // when
        fileBridgeService.createDirectory(directoryCreationRequestModel);

        // then
        verify(postRequestedFor(urlEqualTo(LeafletPath.FILES_DIRECTORIES.getURI()))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldUpdateFileMetaInfo() throws CommunicationFailureException, JsonProcessingException {

        // given
        UUID fileIdentifier = UUID.randomUUID();
        String uri = prepareURI(LeafletPath.FILES_ONLY_UUID.getURI(), fileIdentifier);
        UpdateFileMetaInfoRequestModel updateFileMetaInfoRequestModel = prepareUpdateFileMetaInfoRequestModel();
        StringValuePattern requestBody = equalToJson(OBJECT_MAPPER.writeValueAsString(updateFileMetaInfoRequestModel));
        givenThat(put(uri)
                .withRequestBody(requestBody));

        // when
        fileBridgeService.updateFileMetaInfo(fileIdentifier, updateFileMetaInfoRequestModel);

        // then
        verify(putRequestedFor(urlEqualTo(uri))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldGetDirectories() throws CommunicationFailureException {

        // given
        String uri = LeafletPath.FILES_DIRECTORIES.getURI();
        DirectoryListDataModel directoryListDataModel = prepareDirectoryListDataModel();
        givenThat(get(uri)
                .willReturn(ResponseDefinitionBuilder.okForJson(directoryListDataModel)));

        // when
        DirectoryListDataModel result = fileBridgeService.getDirectories();

        // then
        assertThat(result, equalTo(directoryListDataModel));
        verify(getRequestedFor(urlEqualTo(uri))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    private UpdateFileMetaInfoRequestModel prepareUpdateFileMetaInfoRequestModel() {
        UpdateFileMetaInfoRequestModel updateFileMetaInfoRequestModel = new UpdateFileMetaInfoRequestModel();
        updateFileMetaInfoRequestModel.setDescription("new description");
        updateFileMetaInfoRequestModel.setOriginalFilename("new filename");
        return updateFileMetaInfoRequestModel;
    }

    private DirectoryCreationRequestModel prepareDirectoryCreationRequestModel() {
        DirectoryCreationRequestModel directoryCreationRequestModel = new DirectoryCreationRequestModel();
        directoryCreationRequestModel.setName("folder");
        directoryCreationRequestModel.setParent("parent");
        return directoryCreationRequestModel;
    }

    private FileUploadRequestModel prepareFileUploadRequestModel() {
        FileUploadRequestModel fileUploadRequestModel = new FileUploadRequestModel();
        fileUploadRequestModel.setDescription("file");
        fileUploadRequestModel.setSubFolder(StringUtils.EMPTY);
        return fileUploadRequestModel;
    }

    private FileListDataModel prepareFileListDataModel() {
        return FileListDataModel.getBuilder()
                .withItem(prepareFileDataModel())
                .withItem(prepareFileDataModel())
                .build();
    }

    private FileDataModel prepareFileDataModel() {
        return FileDataModel.getBuilder()
                .withReference(UUID.randomUUID().toString())
                .build();
    }

    private DirectoryListDataModel prepareDirectoryListDataModel() {
        return DirectoryListDataModel.getBuilder()
                .withItem(prepareDirectoryDataModel("ACCEPTOR-1"))
                .withItem(prepareDirectoryDataModel("ACCEPTOR-2"))
                .build();
    }

    private DirectoryDataModel prepareDirectoryDataModel(String id) {
        return DirectoryDataModel.getBuilder()
                .withId(id)
                .withRoot(id + "-root")
                .withChildren(Arrays.asList("sub1", "sub2", "sub/sub3"))
                .build();
    }
}
