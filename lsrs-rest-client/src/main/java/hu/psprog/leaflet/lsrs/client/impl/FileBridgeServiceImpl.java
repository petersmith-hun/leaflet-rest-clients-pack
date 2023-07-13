package hu.psprog.leaflet.lsrs.client.impl;

import hu.psprog.leaflet.bridge.adapter.RequestBodyAdapter;
import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.lsrs.api.request.DirectoryCreationRequestModel;
import hu.psprog.leaflet.lsrs.api.request.FileUploadRequestModel;
import hu.psprog.leaflet.lsrs.api.request.UpdateFileMetaInfoRequestModel;
import hu.psprog.leaflet.lsrs.api.response.DirectoryListDataModel;
import hu.psprog.leaflet.lsrs.api.response.FileDataModel;
import hu.psprog.leaflet.lsrs.api.response.FileListDataModel;
import hu.psprog.leaflet.lsrs.api.response.VFSBrowserModel;
import hu.psprog.leaflet.lsrs.client.FileBridgeService;
import hu.psprog.leaflet.lsrs.client.config.LSRSPath;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;
import java.util.UUID;

/**
 * Implementation of {@link FileBridgeService}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "lsrs")
public class FileBridgeServiceImpl implements FileBridgeService {

    private static final String FILE_IDENTIFIER = "fileIdentifier";
    private static final String STORED_FILENAME = "storedFilename";

    private final BridgeClient bridgeClient;
    private final RequestBodyAdapter<FileUploadRequestModel, FormDataMultiPart> multipartAdapter;

    @Autowired
    public FileBridgeServiceImpl(BridgeClient bridgeClient,
                                 @Qualifier("LSRSFileUploadMultipartRequestBodyAdapter") RequestBodyAdapter<FileUploadRequestModel, FormDataMultiPart> multipartAdapter) {
        this.bridgeClient = bridgeClient;
        this.multipartAdapter = multipartAdapter;
    }

    @Override
    public FileListDataModel getUploadedFiles() throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LSRSPath.FILES)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, FileListDataModel.class);
    }

    @Override
    public InputStream downloadFile(UUID fileIdentifier, String storedFilename) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LSRSPath.FILES_BY_ID)
                .addPathParameter(FILE_IDENTIFIER, fileIdentifier.toString())
                .addPathParameter(STORED_FILENAME, storedFilename)
                .build();

        return bridgeClient.call(restRequest, InputStream.class);
    }

    @Override
    public FileDataModel getFileDetails(UUID fileIdentifier) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LSRSPath.FILES_ONLY_UUID)
                .addPathParameter(FILE_IDENTIFIER, fileIdentifier.toString())
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, FileDataModel.class);
    }

    @Override
    public FileDataModel uploadFile(FileUploadRequestModel fileUploadRequestModel) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.POST)
                .path(LSRSPath.FILES)
                .requestBody(fileUploadRequestModel)
                .multipart()
                .adapter(multipartAdapter)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, FileDataModel.class);
    }

    @Override
    public void deleteFile(UUID fileIdentifier) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.DELETE)
                .path(LSRSPath.FILES_ONLY_UUID)
                .addPathParameter(FILE_IDENTIFIER, fileIdentifier.toString())
                .authenticated()
                .build();

        bridgeClient.call(restRequest);
    }

    @Override
    public void createDirectory(DirectoryCreationRequestModel directoryCreationRequestModel) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.POST)
                .path(LSRSPath.FILES_DIRECTORIES)
                .requestBody(directoryCreationRequestModel)
                .authenticated()
                .build();

        bridgeClient.call(restRequest);
    }

    @Override
    public void updateFileMetaInfo(UUID fileIdentifier, UpdateFileMetaInfoRequestModel updateFileMetaInfoRequestModel)
            throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.PUT)
                .path(LSRSPath.FILES_ONLY_UUID)
                .requestBody(updateFileMetaInfoRequestModel)
                .addPathParameter(FILE_IDENTIFIER, fileIdentifier.toString())
                .authenticated()
                .build();

        bridgeClient.call(restRequest);
    }

    @Override
    public DirectoryListDataModel getDirectories() throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(LSRSPath.FILES_DIRECTORIES)
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, DirectoryListDataModel.class);
    }

    @Override
    public VFSBrowserModel browse(String path) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(() -> String.format("/files/browse/%s", path))
                .authenticated()
                .build();

        return bridgeClient.call(restRequest, VFSBrowserModel.class);
    }
}
