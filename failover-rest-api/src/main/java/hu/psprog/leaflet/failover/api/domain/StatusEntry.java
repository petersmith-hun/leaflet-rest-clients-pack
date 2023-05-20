package hu.psprog.leaflet.failover.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

/**
 * Model object for status tracking entries.
 *
 * @author Peter Smith
 */
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
@Jacksonized
public record StatusEntry(
        Date created,
        FailoverStatus status,
        String parameter
) { }
