package hu.psprog.leaflet.translation.client.impl;

import hu.psprog.leaflet.bridge.client.BridgeClient;
import hu.psprog.leaflet.bridge.client.domain.BridgeService;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.request.RESTRequest;
import hu.psprog.leaflet.bridge.client.request.RequestMethod;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.api.domain.TranslationPackCreationRequest;
import hu.psprog.leaflet.translation.api.domain.TranslationPackMetaInfo;
import hu.psprog.leaflet.translation.client.TranslationServiceClient;
import hu.psprog.leaflet.translation.client.config.TMSPath;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link TranslationServiceClient}.
 *
 * @author Peter Smith
 */
@BridgeService(client = "tms")
public class TranslationServiceClientImpl implements TranslationServiceClient {

    private static final String PARAMETER_PACK_ID = "packID";

    private BridgeClient bridgeClient;

    @Autowired
    public TranslationServiceClientImpl(BridgeClient bridgeClient) {
        this.bridgeClient = bridgeClient;
    }

    @Override
    public List<TranslationPackMetaInfo> listStoredPacks() throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(TMSPath.TRANSLATIONS)
                .build();

        return bridgeClient.call(restRequest, new GenericType<List<TranslationPackMetaInfo>>(){});
    }

    @Override
    public TranslationPack getPackByID(UUID packID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.GET)
                .path(TMSPath.TRANSLATIONS_BY_ID)
                .addPathParameter(PARAMETER_PACK_ID, packID)
                .build();

        return bridgeClient.call(restRequest, TranslationPack.class);
    }

    @Override
    public TranslationPack createTranslationPack(TranslationPackCreationRequest translationPackCreationRequest) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.POST)
                .path(TMSPath.TRANSLATIONS)
                .requestBody(translationPackCreationRequest)
                .build();

        return bridgeClient.call(restRequest, TranslationPack.class);
    }

    @Override
    public TranslationPack changePackStatus(UUID packID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.PUT)
                .path(TMSPath.TRANSLATIONS_STATUS)
                .addPathParameter(PARAMETER_PACK_ID, packID)
                .build();

        return bridgeClient.call(restRequest, TranslationPack.class);
    }

    @Override
    public void deleteTranslationPack(UUID packID) throws CommunicationFailureException {

        RESTRequest restRequest = RESTRequest.getBuilder()
                .method(RequestMethod.DELETE)
                .path(TMSPath.TRANSLATIONS_BY_ID)
                .addPathParameter(PARAMETER_PACK_ID, packID)
                .build();

        bridgeClient.call(restRequest);
    }
}
