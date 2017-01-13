package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.ReportTestSuiteConfigurationEvent;
import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ContainerReportEvent extends ReportTestSuiteConfigurationEvent {

    public ContainerReportEvent(SectionReport sectionReport) {
        super(sectionReport);
    }

    public ContainerReportEvent(String identifierSuffix) {
        super(identifierSuffix);
    }

    public ContainerReportEvent(SectionReport sectionReport, String identifierSuffix) {
        super(sectionReport, identifierSuffix);
    }
}
