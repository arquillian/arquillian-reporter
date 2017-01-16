package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.event.ReportTestSuiteConfiguration;
import org.arquillian.reporter.api.model.Section;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ContainerReportEventTestSuiteConfiguration extends ReportEvent<Section, ReportTestSuiteConfiguration> {

    public ContainerReportEventTestSuiteConfiguration(String identifier) {
        super(identifier);
    }

    public ContainerReportEventTestSuiteConfiguration(Section section, String identifier) {
        super(section, identifier);
    }

    // todo support multiple test suites
}
