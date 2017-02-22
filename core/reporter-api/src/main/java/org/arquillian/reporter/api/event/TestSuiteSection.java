package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.TestSuiteReport;

/**
 * An implementation of {@link SectionEvent} that represents section for {@link TestSuiteReport}s
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteSection extends SectionEvent<TestSuiteSection, TestSuiteReport, SectionEvent> {

    /**
     * Creates an instance of {@link TestSuiteSection}
     */
    public TestSuiteSection() {
    }

    /**
     * Creates an instance of {@link TestSuiteSection} with the given id
     *
     * @param testSuiteId A test suite id to be used to identify this {@link TestSuiteSection}
     */
    public TestSuiteSection(String testSuiteId) {
        super(testSuiteId);
    }

    /**
     * Creates an instance of {@link TestSuiteSection} with the given {@link TestSuiteReport}
     *
     * @param testSuiteReport A {@link TestSuiteReport} that should be contained within this {@link TestSuiteSection}
     */
    public TestSuiteSection(TestSuiteReport testSuiteReport) {
        super(testSuiteReport);
    }

    /**
     * Creates an instance of {@link TestSuiteSection} with the given {@link TestSuiteReport}
     *
     * @param testSuiteReport A {@link TestSuiteReport} that should be contained within this {@link TestSuiteSection}
     * @param testSuiteId     A test suite id to be used to identify this {@link TestSuiteSection}
     */
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
}
