package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassNodeSection extends ReportNodeEvent<SectionReport, TestClassNode> {

    public TestClassNodeSection(SectionReport sectionReport) {
        super(sectionReport);
    }

    public TestClassNodeSection(String identifier) {
        super(identifier);
    }

    public TestClassNodeSection(SectionReport sectionReport, String identifier) {
        super(sectionReport, identifier);
    }

    public TestClassNodeSection(SectionReport sectionReport, Class<?> testClass) {
        super(sectionReport);
        setParentEvent(new TestClassNode(testClass));
    }

    public TestClassNodeSection(String identifier, Class<?> testClass) {
        super(identifier);
        setParentEvent(new TestClassNode(testClass));
    }

    public TestClassNodeSection(SectionReport sectionReport, String identifier, Class<?> testClass) {
        super(sectionReport, identifier);
        setParentEvent(new TestClassNode(testClass));
    }
}
