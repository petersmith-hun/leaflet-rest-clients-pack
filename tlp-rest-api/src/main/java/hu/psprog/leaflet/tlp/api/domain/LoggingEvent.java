package hu.psprog.leaflet.tlp.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

/**
 * Domain object for parsing and storing received log events.
 *
 * @author Peter Smith
 */
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
@Jacksonized
public record LoggingEvent(
        String threadName,
        String loggerName,
        String level,
        String content,
        ThrowableProxyLogItem exception,
        Date timeStamp,
        String source
) { }
