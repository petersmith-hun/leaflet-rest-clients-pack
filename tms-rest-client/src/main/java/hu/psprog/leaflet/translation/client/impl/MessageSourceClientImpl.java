package hu.psprog.leaflet.translation.client.impl;

import hu.psprog.leaflet.bridge.client.domain.BridgeConstants;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.handler.ResponseReader;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.client.MessageSourceClient;
import hu.psprog.leaflet.translation.client.config.TMSPath;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link MessageSourceClient}.
 *
 * @author Peter Smith
 */
@Service
@Setter(AccessLevel.PACKAGE)
@ConfigurationProperties("bridge.clients.tms")
public class MessageSourceClientImpl implements MessageSourceClient {

    private static final TypeReference<Set<TranslationPack>> ENTITY_TYPE = new TypeReference<>() {};

    private final HttpClient httpClient;
    private final ResponseReader responseReader;

    private String hostUrl;

    @Autowired
    public MessageSourceClientImpl(HttpClient bridgeHttpClient, ResponseReader responseReader) {
        this.httpClient = bridgeHttpClient;
        this.responseReader = responseReader;
    }

    @Override
    public Set<TranslationPack> retrievePacks(List<String> packs) throws CommunicationFailureException {

        try {
            return httpClient.execute(createRequest(packs), response -> {

                if (response.getCode() != HttpStatus.SC_OK) {
                    throw new IllegalStateException("Failed to retrieve translation packs. Service responded with HTTP status %s".formatted(response.getCode()));
                }

                return responseReader.read(response, ENTITY_TYPE);
            });

        } catch (Exception exception) {
            throw new CommunicationFailureException(exception);
        }
    }

    private ClassicHttpRequest createRequest(List<String> packs) {

        String baseURL = hostUrl.endsWith("/")
                ? hostUrl.substring(0, hostUrl.length() - 1)
                : hostUrl;
        ClassicHttpRequest request = new HttpGet("%s%s?packs=%s".formatted(baseURL, TMSPath.TRANSLATIONS.getURI(), String.join(",", packs)));
        request.addHeader(BridgeConstants.CONTENT_TYPE_HEADER, BridgeConstants.CONTENT_TYPE_JSON);

        return request;
    }
}
