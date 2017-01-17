package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.TestSuiteReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteNode extends ReportNodeEvent<TestSuiteReport, ReportNodeEvent> {

    public TestSuiteNode(TestSuiteReport testSuiteReport) {
        super(testSuiteReport);
    }

    public TestSuiteNode(String identifier) {
        super(identifier);
    }

    public TestSuiteNode(TestSuiteReport testSuiteReport, String identifier) {
        super(testSuiteReport, identifier);
    }
}
