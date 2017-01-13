package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportTestClassEvent extends ReportEvent<TestClassReport> {
    public ReportTestClassEvent(TestClassReport sectionReport) {
        super(sectionReport);
    }

    public ReportTestClassEvent(String identifier) {
        super(identifier);
    }

    public ReportTestClassEvent(TestClassReport sectionReport, String identifier) {
        super(sectionReport, identifier);
    }

    public ReportTestClassEvent inTestSuite(String testSuiteName){
        setParentEvent(new ReportTestSuiteEvent(testSuiteName));
        return this;
    }
}
