package hu.psprog.leaflet.lens.client.impl.testdata;

import hu.psprog.leaflet.lens.api.domain.MailContent;
import hu.psprog.leaflet.lens.api.domain.MailRequestWrapper;

/**
 * Interface for LENS client test data sets.
 *
 * @author Peter Smith
 */
public interface Scenario {

    MailRequestWrapper<? extends MailContent> request();

    String expectedPath();
}
