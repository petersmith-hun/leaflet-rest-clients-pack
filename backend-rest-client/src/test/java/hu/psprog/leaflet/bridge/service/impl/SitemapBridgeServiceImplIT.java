package hu.psprog.leaflet.bridge.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.config.LeafletPath;
import hu.psprog.leaflet.bridge.it.config.BridgeITSuite;
import hu.psprog.leaflet.bridge.service.SitemapBridgeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for {@link SitemapBridgeServiceImpl}.
 *
 * @author Peter Smith
 */
@BridgeITSuite
public class SitemapBridgeServiceImplIT extends WireMockBaseTest {

    private static final Sitemap SITEMAP = Sitemap.getBuilder()
            .withLocation("/location-1")
            .withLocation("/location-2")
            .withLocation("/location/sub/3")
            .build();

    @Autowired
    private SitemapBridgeService sitemapBridgeService;

    @Test
    public void shouldGetSitemap() throws CommunicationFailureException, JsonProcessingException {

        // given
        givenThat(get(LeafletPath.SITEMAP.getURI())
                .willReturn(jsonResponse(SITEMAP, 200)));

        // when
        Sitemap result = sitemapBridgeService.getSitemap();

        // then
        assertThat(result, equalTo(SITEMAP));
        verify(getRequestedFor(urlEqualTo(LeafletPath.SITEMAP.getURI())));
    }
}
