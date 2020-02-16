open module leaflet.component.rest.tlp.client {
    requires leaflet.component.bridge.api;
    requires leaflet.component.bridge.implementation;
    requires spring.beans;
    requires spring.context;
    requires leaflet.component.rest.tlp.api;
    requires com.fasterxml.jackson.databind;

    exports hu.psprog.leaflet.tlp.api.client;
    exports hu.psprog.leaflet.tlp.api.client.impl;
}