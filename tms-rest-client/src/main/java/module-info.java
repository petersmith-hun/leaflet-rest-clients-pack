module leaflet.component.rest.tms.client {
    requires java.ws.rs;
    requires leaflet.component.bridge.api;
    requires leaflet.component.bridge.implementation;
    requires spring.beans;
    requires spring.boot;
    requires spring.context;
    requires leaflet.component.rest.tms.api;

    exports hu.psprog.leaflet.translation.client;
}