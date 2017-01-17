package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteNodeSection extends ReportNodeEvent<SectionReport, TestSuiteNode> {

    public TestSuiteNodeSection(SectionReport sectionReport) {
        super(sectionReport);
    }

    public TestSuiteNodeSection(String identifier) {
        super(identifier);
    }

    public TestSuiteNodeSection(SectionReport sectionReport, String identifier) {
        super(sectionReport, identifier);
    }

    public TestSuiteNodeSection(String identifier, String testSuiteIdentifier) {
        super(identifier);
        setParentEvent(new TestSuiteNode(testSuiteIdentifier));
    }

    public TestSuiteNodeSection(SectionReport sectionReport, String identifier, String testSuiteIdentifier) {
        super(sectionReport, identifier);
        setParentEvent(new TestSuiteNode(testSuiteIdentifier));
    }
}
