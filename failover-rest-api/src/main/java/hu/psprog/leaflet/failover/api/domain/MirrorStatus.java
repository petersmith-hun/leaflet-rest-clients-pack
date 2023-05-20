package hu.psprog.leaflet.failover.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

/**
 * Mirroring result containing mirror type and number of processed records.
 *
 * @author Peter Smith
 */
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
@Jacksonized
public record MirrorStatus(
        MirrorType mirrorType,
        int numberOfRecords
) { }
