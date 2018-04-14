package hu.psprog.leaflet.failover.api.domain;

/**
 * Possible failover statuses.
 *
 * @author Peter Smith
 */
public enum FailoverStatus {

    /**
     * Application is currently creating content mirror.
     */
    MIRRORING,

    /**
     * Failed to retrieve mirror.
     */
    MIRRORING_FAILURE,

    /**
     * Application is in standby mode, currently not serving traffic.
     */
    STANDBY,

    /**
     * Application is serving live traffic.
     */
    SERVING
}
