package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.TestSuiteReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportEventTestSuite extends ReportEvent {
    public ReportEventTestSuite(TestSuiteReport sectionReport) {
        super(sectionReport);
    }

    public ReportEventTestSuite(String identifier) {
        super(identifier);
    }

    public ReportEventTestSuite(TestSuiteReport sectionReport, String identifier) {
        super(sectionReport, identifier);
    }
}
