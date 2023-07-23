package hu.psprog.leaflet.tlp.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.Map;

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
        String source,
        Map<String, String> context
) { }
