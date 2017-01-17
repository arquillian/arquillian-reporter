package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.model.report.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodNodeSection extends ReportNodeEvent<SectionReport, TestMethodNode> {

    public TestMethodNodeSection(SectionReport sectionReport) {
        super(sectionReport);
    }

    public TestMethodNodeSection(String identifier) {
        super(identifier);
    }

    public TestMethodNodeSection(SectionReport sectionReport, String identifier) {
        super(sectionReport, identifier);
    }

    public TestMethodNodeSection(SectionReport sectionReport, Method testMethod) {
        super(sectionReport);
        setParentEvent(new TestMethodNode(testMethod));
    }

    public TestMethodNodeSection(String identifier, Method testMethod) {
        super(identifier);
        setParentEvent(new TestMethodNode(testMethod));
    }

    public TestMethodNodeSection(SectionReport sectionReport, String identifier, Method testMethod) {
        super(sectionReport, identifier);
        setParentEvent(new TestMethodNode(testMethod));
    }
}
