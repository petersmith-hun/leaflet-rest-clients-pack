open module leaflet.component.rest.support.hystrix {
    requires hystrix.core;
    requires hystrix.javanica;
    requires hystrix.servo.metrics.publisher;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.core;
    requires spring.context;
    requires spring.security.core;
    requires spring.web;
    requires slf4j.api;
    requires tomcat.embed.core;

    exports hu.psprog.leaflet.rcp.hystrix.support.filter;
}