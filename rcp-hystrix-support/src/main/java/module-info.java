open module leaflet.component.rest.support.hystrix {
    requires hystrix.core;
    requires hystrix.javanica;
    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.core;
    requires spring.context;
    requires spring.web;
    requires slf4j.api;
    requires tomcat.embed.core;
}