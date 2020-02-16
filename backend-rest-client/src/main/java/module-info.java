open module leaflet.component.rest.backend.client {
    requires leaflet.component.bridge.api;
    requires leaflet.component.bridge.implementation;
    requires leaflet.component.rest.backend.api;
    requires static leaflet.component.rest.support.hystrix;

    requires hystrix.javanica;
    requires java.ws.rs;
    requires spring.beans;
    requires spring.context;
    requires static spring.security.core;
    requires static org.apache.tomcat.embed.core;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;

    exports hu.psprog.leaflet.bridge.service;
    exports hu.psprog.leaflet.bridge.service.impl;
}