package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportEventTestSuiteConfiguration extends ReportEvent {

    public ReportEventTestSuiteConfiguration(SectionReport sectionReport) {
        super(sectionReport);
    }

    public ReportEventTestSuiteConfiguration(String identifier) {
        super(identifier);
    }

    public ReportEventTestSuiteConfiguration(SectionReport sectionReport, String identifier) {
        super(sectionReport, identifier);
    }
}
