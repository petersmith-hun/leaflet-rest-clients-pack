open module leaflet.component.rest.request.adapters {
    requires java.ws.rs;
    requires jersey.media.multipart;
    requires leaflet.component.bridge.api;
    requires leaflet.component.rest.backend.api;
    requires org.apache.commons.io;
    requires org.slf4j;
    requires spring.beans;
    requires spring.context;
    requires spring.web;
    requires leaflet.component.rest.recaptcha.api;

    exports hu.psprog.leaflet.bridge.adapter.impl;
}