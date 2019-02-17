package hu.psprog.leaflet.rcp.hystrix.support.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Hystrix support test application entry point.
 *
 * @author Peter Smith
 */
@SpringBootApplication
@EnableWebMvc
public class HystrixTestApp {

    public static void main(String[] args) {
        SpringApplication.run(HystrixTestApp.class, args);
    }
}
