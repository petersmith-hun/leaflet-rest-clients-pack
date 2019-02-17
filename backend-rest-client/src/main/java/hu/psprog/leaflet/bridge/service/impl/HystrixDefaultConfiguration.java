package hu.psprog.leaflet.bridge.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;

/**
 * This class holds default common configuration for Hystrix-wrapped Bridge clients.
 *
 * @author Peter Smith
 */
@DefaultProperties(ignoreExceptions = {
        CommunicationFailureException.class,
        DefaultNonSuccessfulResponseException.class})
abstract class HystrixDefaultConfiguration {
}
