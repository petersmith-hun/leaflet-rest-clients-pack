open module leaflet.component.rest.tlp.api {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.lang3;
    requires spring.context;

    exports hu.psprog.leaflet.tlp.api.domain;
}