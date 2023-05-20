package hu.psprog.leaflet.tlp.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * List of {@link LoggingEvent} objects with paging information.
 *
 * @author Peter Smith
 */
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
@Jacksonized
public record LogEventPage(
        long entityCount,
        int pageCount,
        int pageNumber,
        int pageSize,
        int entityCountOnPage,
        List<LoggingEvent> entitiesOnPage,
        boolean first,
        boolean last,
        boolean hasNext,
        boolean hasPrevious
) { }
