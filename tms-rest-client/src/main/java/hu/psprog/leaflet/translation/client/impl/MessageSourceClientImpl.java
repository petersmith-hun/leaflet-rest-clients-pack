package hu.psprog.leaflet.translation.client.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;
import hu.psprog.leaflet.translation.client.MessageSourceClient;
import hu.psprog.leaflet.translation.client.config.TMSPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link MessageSourceClient}.
 *
 * @author Peter Smith
 */
@Service
@ConfigurationProperties("bridge.clients.tms")
public class MessageSourceClientImpl implements MessageSourceClient {

    private static final String PARAMETER_PACKS = "packs";
    private static final GenericType<Set<TranslationPack>> ENTITY_TYPE = new GenericType<Set<TranslationPack>>() {};

    private Client client;
    private String hostUrl;

    @Autowired
    public MessageSourceClientImpl(Client client) {
        this.client = client;
    }

    @Override
    public Set<TranslationPack> retrievePacks(List<String> packs) throws CommunicationFailureException {

        Response response;
        try {
            response = client.target(hostUrl)
                    .path(TMSPath.TRANSLATIONS.getURI())
                    .queryParam(PARAMETER_PACKS, packs.toArray())
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();
        } catch (RuntimeException e) {
            throw new CommunicationFailureException(e);
        }

        return readResponse(response);
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    private Set<TranslationPack> readResponse(Response response) {

        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new IllegalStateException("Failed to retrieve translation packs. Service responded with HTTP status " + response.getStatus());
        }

        return response.readEntity(ENTITY_TYPE);
    }
}
