package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.TestSuiteReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportTestSuiteEvent extends ReportEvent<TestSuiteReport> {
    public ReportTestSuiteEvent(TestSuiteReport sectionReport) {
        super(sectionReport);
    }

    public ReportTestSuiteEvent(String identifier) {
        super(identifier);
    }

    public ReportTestSuiteEvent(TestSuiteReport sectionReport, String identifier) {
        super(sectionReport, identifier);
    }
}
