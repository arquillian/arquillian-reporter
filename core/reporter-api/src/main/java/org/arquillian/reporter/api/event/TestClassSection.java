package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassSection extends SectionEvent<TestClassSection, TestClassReport, TestSuiteSection> {

    private String testSuiteId;

    public TestClassSection() {
    }

    public TestClassSection(Class<?> testClass) {
        super(testClass != null ? testClass.getCanonicalName() : null);
    }

    public TestClassSection(TestClassReport testClassReport) {
        super(testClassReport);
    }

    public TestClassSection(Class<?> testClass, String testSuiteId) {
        super(testClass.getCanonicalName());
        this.testSuiteId = testSuiteId;
    }

    public TestClassSection(TestClassReport testClassReport, Class<?> testClass, String testSuiteId) {
        super(testClassReport, testClass.getCanonicalName());
        this.testSuiteId = testSuiteId;
    }

    @Override
    public TestSuiteSection getParentSectionThisSectionBelongsTo() {
        return new TestSuiteSection(testSuiteId);
    }

    @Override
    public Class<TestClassReport> getReportTypeClass() {
        return TestClassReport.class;
    }

    public String getTestSuiteId() {
        return testSuiteId;
    }

    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }
}
