package hu.psprog.leaflet.translation.client;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.translation.api.domain.TranslationPack;

import java.util.List;
import java.util.Set;

/**
 * TMS REST client for translation pack retrieval.
 * Note: Bridge requires HttpServletRequest which is not available for non-user-triggered requests.
 * As pack retrieval is a request of this kind, separate client is required.
 *
 * @author Peter Smith
 */
public interface MessageSourceClient {

    /**
     * Calls TMS to retrieve latest enabled translation packs by their names.
     *
     * @param packs pack names to retrieve
     * @return available {@link TranslationPack}s as {@link Set}
     */
    Set<TranslationPack> retrievePacks(List<String> packs) throws CommunicationFailureException;
}
