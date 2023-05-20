package hu.psprog.leaflet.tlp.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Exception (throwableProxy) node deserialization model that conforms Logback-original log event model format.
 *
 * @author Peter Smith
 */
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
@Jacksonized
public record ThrowableProxyLogItem(
        String className,
        String message,
        String stackTrace,
        ThrowableProxyLogItem cause,
        List<ThrowableProxyLogItem> suppressed
) { }
