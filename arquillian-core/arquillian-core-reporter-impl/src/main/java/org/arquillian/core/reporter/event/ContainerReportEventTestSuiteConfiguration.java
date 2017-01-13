package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.ReportEventTestSuiteConfiguration;
import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ContainerReportEventTestSuiteConfiguration extends ReportEventTestSuiteConfiguration {

    public ContainerReportEventTestSuiteConfiguration(String identifier) {
        super(identifier);
    }

    public ContainerReportEventTestSuiteConfiguration(SectionReport sectionReport, String identifier) {
        super(sectionReport, identifier);
    }

    // todo support multiple test suites
}
