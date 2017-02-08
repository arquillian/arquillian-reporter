package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.TestSuiteReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteSection extends SectionEvent<TestSuiteSection, TestSuiteReport, SectionEvent> {

    public TestSuiteSection() {
    }

    public TestSuiteSection(String testSuiteId) {
        super(testSuiteId);
    }

    public TestSuiteSection(TestSuiteReport testSuiteReport) {
        super(testSuiteReport);
    }

    public TestSuiteSection(TestSuiteReport testSuiteReport, String testSuiteId) {
        super(testSuiteReport, testSuiteId);
    }

    @Override
    public SectionEvent getParentSectionThisSectionBelongsTo() {
        return null;
    }

    @Override
    public Class<TestSuiteReport> getReportTypeClass() {
        return TestSuiteReport.class;
    }

    @Override
    public Identifier<TestSuiteSection> identifyYourself() {
        return new Identifier<>(TestSuiteSection.class, getSectionId());
    }
}
