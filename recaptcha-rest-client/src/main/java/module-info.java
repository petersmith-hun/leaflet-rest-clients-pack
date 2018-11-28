module leaflet.component.rest.recaptcha.client {
    requires leaflet.component.bridge.api;
    requires leaflet.component.bridge.implementation;
    requires spring.beans;
    requires spring.context;
    requires leaflet.component.rest.request.adapters;
    requires leaflet.component.rest.recaptcha.api;

    exports hu.psprog.leaflet.recaptcha.api.client;
}