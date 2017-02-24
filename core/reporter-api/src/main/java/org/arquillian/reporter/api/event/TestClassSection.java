package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.utils.ReporterUtils;

/**
 * An implementation of {@link SectionEvent} that represents section for {@link TestClassReport}s
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassSection extends SectionEvent<TestClassSection, TestClassReport, TestSuiteSection> {

    private String testSuiteId;

    /**
     * Creates an instance of {@link TestClassSection}
     */
    public TestClassSection() {
    }

    /**
     * Creates an instance of {@link TestClassSection} with an id created as canonical name of the given test class
     *
     * @param testClass A test class the section relates to and whose canonical name should be used as an id of this {@link TestClassSection}
     */
    public TestClassSection(Class<?> testClass) {
        super(ReporterUtils.getTestClassId(testClass));
    }

    /**
     * Creates an instance of {@link TestClassSection} with an id created as a canonical name of the given test class.
     * The test suite id is stored to identify a parental section this section belongs to
     *
     * @param testClass   A test class the section relates to and whose canonical name should be used as an id of this {@link TestClassSection}
     * @param testSuiteId A test suite id this class belongs to
     */
    public TestClassSection(Class<?> testClass, String testSuiteId) {
        super(ReporterUtils.getTestClassId(testClass));
        this.testSuiteId = testSuiteId;
    }

    /**
     * Creates an instance of {@link TestClassSection} with the given {@link TestClassReport}
     *
     * @param testClassReport A {@link TestClassReport} that should be contained as the payload of this {@link TestClassSection}
     */
    public TestClassSection(TestClassReport testClassReport) {
        super(testClassReport);
    }

    /**
     * Creates an instance of {@link TestClassSection} with the given {@link TestClassReport}
     * and an id created as canonical name of the given test class
     *
     * @param testClassReport A {@link TestClassReport} that should be contained as the payload of this {@link TestClassSection}
     * @param testClass       A test class the section relates to and whose canonical name should be used as an id of this {@link TestClassSection}
     */
    public TestClassSection(TestClassReport testClassReport, Class<?> testClass) {
        super(testClassReport, ReporterUtils.getTestClassId(testClass));
    }

    /**
     * Creates an instance of {@link TestClassSection} with the given {@link TestClassReport}
     * and an id created as canonical name of the given test class
     * The test suite id is stored to identify a parental section this section belongs to
     *
     * @param testClassReport A {@link TestClassReport} that should be contained as the payload of this {@link TestClassSection}
     * @param testClass       A test class the section relates to and whose canonical name should be used as an id of this {@link TestClassSection}
     * @param testSuiteId     A test suite id this class belongs to
     */
    public TestClassSection(TestClassReport testClassReport, Class<?> testClass, String testSuiteId) {
        super(testClassReport, ReporterUtils.getTestClassId(testClass));
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

    /**
     * Returns a test suite id of the {@link TestSuiteSection} this section belongs to
     *
     * @return A test suite id of the {@link TestSuiteSection} this section belongs to
     */
    public String getTestSuiteId() {
        return testSuiteId;
    }

    /**
     * Sets a test suite id of the {@link TestSuiteSection} this section belongs to
     *
     * @param testSuiteId A test suite id of the {@link TestSuiteSection} this section belongs to
     */
    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }
}
