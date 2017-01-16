package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.TestSuiteSection;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportTestSuite extends ReportEvent<TestSuiteSection, ReportEvent> {

    public ReportTestSuite(TestSuiteSection sectionReport) {
        super(sectionReport);
    }

    public ReportTestSuite(String identifier) {
        super(identifier);
    }

    public ReportTestSuite(TestSuiteSection sectionReport, String identifier) {
        super(sectionReport, identifier);
    }
}
