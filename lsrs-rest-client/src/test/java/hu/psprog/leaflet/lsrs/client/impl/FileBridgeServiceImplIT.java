package hu.psprog.leaflet.lsrs.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RequestAuthentication;
import hu.psprog.leaflet.lsrs.api.request.DirectoryCreationRequestModel;
import hu.psprog.leaflet.lsrs.api.request.FileUploadRequestModel;
import hu.psprog.leaflet.lsrs.api.request.UpdateFileMetaInfoRequestModel;
import hu.psprog.leaflet.lsrs.api.response.DirectoryDataModel;
import hu.psprog.leaflet.lsrs.api.response.DirectoryListDataModel;
import hu.psprog.leaflet.lsrs.api.response.FileDataModel;
import hu.psprog.leaflet.lsrs.api.response.FileListDataModel;
import hu.psprog.leaflet.lsrs.client.FileBridgeService;
import hu.psprog.leaflet.lsrs.client.config.LSRSPath;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import static hu.psprog.leaflet.lsrs.client.impl.FileBridgeServiceImplIT.LSRSTestClientConfiguration.LSRS_CLIENT_INTEGRATION_TEST_PROFILE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link FileBridgeServiceImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        classes = FileBridgeServiceImplIT.LSRSTestClientConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@WireMockTest(httpPort = 9999)
@ActiveProfiles(LSRS_CLIENT_INTEGRATION_TEST_PROFILE)
public class FileBridgeServiceImplIT {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final StringValuePattern VALUE_PATTERN_BEARER_TOKEN = WireMock.equalTo("Bearer token");
    
    @Autowired
    private FileBridgeService fileBridgeService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldGetUploadedFiles() throws CommunicationFailureException {

        // given
        FileListDataModel fileListDataModel = prepareFileListDataModel();
        givenThat(get(LSRSPath.FILES.getURI()).willReturn(ResponseDefinitionBuilder
                .okForJson(fileListDataModel)));

        // when
        FileListDataModel result = fileBridgeService.getUploadedFiles();

        // then
        assertThat(result, equalTo(fileListDataModel));
        verify(getRequestedFor(urlEqualTo(LSRSPath.FILES.getURI()))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldDownloadFile() throws CommunicationFailureException, IOException {

        // given
        UUID fileIdentifier = UUID.randomUUID();
        String filename = "filename";
        String uri = prepareURI(LSRSPath.FILES_BY_ID.getURI(), fileIdentifier, filename);
        String responseBody = "responseBody";
        givenThat(get(uri)
                .willReturn(ResponseDefinitionBuilder.responseDefinition().withBody(responseBody.getBytes())));

        // when
        InputStream result = fileBridgeService.downloadFile(fileIdentifier, filename);

        // then
        verify(getRequestedFor(urlEqualTo(uri)));
        assertThat(new String(IOUtils.toByteArray(result)), equalTo(responseBody));
        result.close();
    }

    @Test
    public void shouldGetFileDetails() throws CommunicationFailureException {

        // given
        FileDataModel fileDataModel = prepareFileDataModel();
        UUID fileIdentifier = UUID.randomUUID();
        String uri = prepareURI(LSRSPath.FILES_ONLY_UUID.getURI(), fileIdentifier);
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
    public void shouldUploadFile() throws CommunicationFailureException {

        // given
        FileUploadRequestModel fileUploadRequestModel = prepareFileUploadRequestModel();
        FileDataModel fileDataModel = prepareFileDataModel();
        givenThat(post(LSRSPath.FILES.getURI())
                .willReturn(ResponseDefinitionBuilder.okForJson(fileDataModel)));

        // when
        FileDataModel result = fileBridgeService.uploadFile(fileUploadRequestModel);

        // then
        assertThat(result, equalTo(fileDataModel));
        verify(postRequestedFor(urlEqualTo(LSRSPath.FILES.getURI()))
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldDeleteFile() throws CommunicationFailureException {

        // given
        UUID fileIdentifier = UUID.randomUUID();
        String uri = prepareURI(LSRSPath.FILES_ONLY_UUID.getURI(), fileIdentifier);
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
        StringValuePattern requestBody = equalToJson(objectMapper.writeValueAsString(directoryCreationRequestModel));
        givenThat(post(LSRSPath.FILES_DIRECTORIES.getURI())
                .withRequestBody(requestBody));

        // when
        fileBridgeService.createDirectory(directoryCreationRequestModel);

        // then
        verify(postRequestedFor(urlEqualTo(LSRSPath.FILES_DIRECTORIES.getURI()))
                .withRequestBody(requestBody)
                .withHeader(AUTHORIZATION_HEADER, VALUE_PATTERN_BEARER_TOKEN));
    }

    @Test
    public void shouldUpdateFileMetaInfo() throws CommunicationFailureException, JsonProcessingException {

        // given
        UUID fileIdentifier = UUID.randomUUID();
        String uri = prepareURI(LSRSPath.FILES_ONLY_UUID.getURI(), fileIdentifier);
        UpdateFileMetaInfoRequestModel updateFileMetaInfoRequestModel = prepareUpdateFileMetaInfoRequestModel();
        StringValuePattern requestBody = equalToJson(objectMapper.writeValueAsString(updateFileMetaInfoRequestModel));
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
        String uri = LSRSPath.FILES_DIRECTORIES.getURI();
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

        return UpdateFileMetaInfoRequestModel.builder()
                .description("new description")
                .originalFilename("new filename")
                .build();
    }

    private DirectoryCreationRequestModel prepareDirectoryCreationRequestModel() {

        return DirectoryCreationRequestModel.builder()
                .name("folder")
                .parent("parent")
                .build();
    }

    private FileUploadRequestModel prepareFileUploadRequestModel() {

        return FileUploadRequestModel.builder()
                .description("file")
                .subFolder(StringUtils.EMPTY)
                .build();
    }

    private FileListDataModel prepareFileListDataModel() {
        return FileListDataModel.builder()
                .files(List.of(
                        prepareFileDataModel(),
                        prepareFileDataModel()))
                .build();
    }

    private FileDataModel prepareFileDataModel() {
        return FileDataModel.builder()
                .reference(UUID.randomUUID().toString())
                .build();
    }

    private DirectoryListDataModel prepareDirectoryListDataModel() {
        return DirectoryListDataModel.builder()
                .acceptors(List.of(
                        prepareDirectoryDataModel("ACCEPTOR-1"),
                        prepareDirectoryDataModel("ACCEPTOR-2")))
                .build();
    }

    private DirectoryDataModel prepareDirectoryDataModel(String id) {
        return DirectoryDataModel.builder()
                .id(id)
                .root(id + "-root")
                .children(Arrays.asList("sub1", "sub2", "sub/sub3"))
                .build();
    }

    private String prepareURI(String template, Object ... values) {

        String preparedString = template;
        for (Object value : values) {
            preparedString = preparedString.replaceFirst("(\\{[a-zA-Z]+})", String.valueOf(value));
        }

        return preparedString;
    }
    
    /**
     * Context configuration for integration tests.
     *
     * @author Peter Smith
     */
    @Profile(LSRS_CLIENT_INTEGRATION_TEST_PROFILE)
    @Configuration
    @ComponentScan(basePackages = {
            "hu.psprog.leaflet.lsrs.client",
            "hu.psprog.leaflet.bridge"})
    @EnableConfigurationProperties
    public static class LSRSTestClientConfiguration {

        public static final String LSRS_CLIENT_INTEGRATION_TEST_PROFILE = "it";

        private final RequestAuthentication requestAuthentication = () -> {
            Map<String, String> auth = new HashMap<>();
            auth.put("Authorization", "Bearer token");
            return auth;
        };

        @Bean
        public ObjectMapper objectMapper() {
            
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.setDateFormat(DateFormat.getInstance());
            
            return objectMapper;
        }

        @Bean
        public RequestAuthentication requestAuthenticationStub() {
            return requestAuthentication;
        }

        @Bean
        public HttpServletRequest httpServletRequestStub() {
            return Mockito.mock(HttpServletRequest.class);
        }

        @Bean
        public HttpServletResponse httpServletResponseStub() {
            return Mockito.mock(HttpServletResponse.class);
        }
    }
}
