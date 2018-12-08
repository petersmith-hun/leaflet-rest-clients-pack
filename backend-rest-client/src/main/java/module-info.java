open module leaflet.component.rest.backend.client {
    requires java.ws.rs;
    requires leaflet.component.bridge.api;
    requires leaflet.component.bridge.implementation;
    requires leaflet.component.rest.backend.api;
    requires spring.beans;
    requires spring.context;

    exports hu.psprog.leaflet.bridge.service;
    exports hu.psprog.leaflet.bridge.service.impl;
}