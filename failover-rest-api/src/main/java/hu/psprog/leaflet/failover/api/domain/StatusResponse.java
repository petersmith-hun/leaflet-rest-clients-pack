package hu.psprog.leaflet.failover.api.domain;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;

/**
 * Domain object for CBFS status requests.
 *
 * @author Peter Smith
 */
@Builder(setterPrefix = "with", builderMethodName = "getBuilder")
@Jacksonized
public record StatusResponse(
        FailoverStatus status,
        Date lastCall,
        Date lastMirroring,
        List<MirrorStatus> mirrorStatus,
        List<StatusEntry> statusEntryList
) { }
