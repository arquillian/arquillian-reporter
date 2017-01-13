package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.ReportEventTestSuiteConfiguration;
import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ContainerReportEventTestSuiteConfiguration extends ReportEventTestSuiteConfiguration {

    public ContainerReportEventTestSuiteConfiguration(SectionReport sectionReport) {
        super(sectionReport);
    }

    public ContainerReportEventTestSuiteConfiguration(String identifierSuffix) {
        super(identifierSuffix);
    }

    public ContainerReportEventTestSuiteConfiguration(SectionReport sectionReport, String identifierSuffix) {
        super(sectionReport, identifierSuffix);
    }
}
