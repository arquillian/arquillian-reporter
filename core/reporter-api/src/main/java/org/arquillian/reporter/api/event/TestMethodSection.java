package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.utils.ReporterUtils;

/**
 * An implementation of {@link SectionEvent} that represents section for {@link TestMethodReport}s within a {@link TestClassReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodSection extends SectionEvent<TestMethodSection, TestMethodReport, TestClassSection> {

    private Method method;
    private String testSuiteId;

    /**
     * Creates an instance of {@link TestMethodSection}
     */
    public TestMethodSection() {
    }

    /**
     * Creates an instance of {@link TestMethodSection} with an id created from given test method name.
     *
     * @param method A test method this {@link TestMethodSection} relates to
     */
    public TestMethodSection(Method method) {
        super(ReporterUtils.getTestMethodId(method));
        this.method = method;
    }

    /**
     * Creates an instance of {@link TestMethodSection} with an id created from given test method name.
     *
     * @param method      A test method this {@link TestMethodSection} relates to
     * @param testSuiteId An id of a test suite the {@link TestMethodReport} belongs to.
     */
    public TestMethodSection(Method method, String testSuiteId) {
        super(ReporterUtils.getTestMethodId(method));
        this.method = method;
        this.testSuiteId = testSuiteId;
    }

    /**
     * Creates an instance of {@link TestMethodSection} with the given {@link TestMethodReport}
     *
     * @param testMethodReport A {@link TestMethodReport} that should be contained within this {@link TestMethodSection}
     */
    public TestMethodSection(TestMethodReport testMethodReport) {
        super(testMethodReport);
    }

    /**
     * Creates an instance of {@link TestMethodSection} with the given {@link TestMethodReport}
     * and an id created from given test method name.
     *
     * @param testMethodReport A {@link TestMethodReport} that should be contained within this {@link TestMethodSection}
     * @param method           A test method this {@link TestMethodSection} relates to
     */
    public TestMethodSection(TestMethodReport testMethodReport, Method method) {
        super(testMethodReport, ReporterUtils.getTestMethodId(method));
        this.method = method;
    }

    /**
     * Creates an instance of {@link TestMethodSection} with the given {@link TestMethodReport}
     * and an id created from given test method name.
     * It also stores the given test suite id for identifying parental section.
     *
     * @param testMethodReport A {@link TestMethodReport} that should be contained within this {@link TestMethodSection}
     * @param method           A test method this {@link TestMethodSection} relates to
     * @param testSuiteId      An id of a test suite the {@link TestMethodReport} belongs to.
     */
    public TestMethodSection(TestMethodReport testMethodReport, Method method, String testSuiteId) {
        super(testMethodReport, ReporterUtils.getTestMethodId(method));
        this.method = method;
        this.testSuiteId = testSuiteId;
    }

    @Override
    public TestClassSection getParentSectionThisSectionBelongsTo() {
        TestClassSection testClassSection = new TestClassSection();

        if (method != null) {
            testClassSection = new TestClassSection(method.getDeclaringClass());
        }
        testClassSection.setTestSuiteId(testSuiteId);

        return testClassSection;
    }

    @Override
    public Class<TestMethodReport> getReportTypeClass() {
        return TestMethodReport.class;
    }

    /**
     * Sets a test suite id of the {@link TestSuiteSection} this section belongs to
     *
     * @param testSuiteId A test suite id of the {@link TestSuiteSection} this section belongs to
     */
    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
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
     * Returns a test method this {@link TestMethodSection} relates to
     *
     * @return A test method this {@link TestMethodSection} relates to
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Sets a test method this {@link TestMethodSection} relates to
     *
     * @param method A test method this {@link TestMethodSection} relates to
     */
    public void setMethod(Method method) {
        this.method = method;
    }
}
