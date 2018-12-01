open module leaflet.component.rest.failover.client {
    requires leaflet.component.bridge.api;
    requires leaflet.component.bridge.implementation;
    requires spring.beans;
    requires spring.context;
    requires leaflet.component.rest.failover.api;

    exports hu.psprog.leaflet.failover.api.client;
}