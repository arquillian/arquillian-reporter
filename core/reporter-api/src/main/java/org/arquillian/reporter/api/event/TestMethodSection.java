package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.report.TestMethodReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodSection extends SectionEvent<TestMethodSection, TestMethodReport, TestClassSection> {

    private Method method;
    private String testSuiteId;

    public TestMethodSection() {
    }

    public TestMethodSection(Method method) {
        super(ReporterUtils.getTestMethodId(method));
        this.method = method;
    }

    public TestMethodSection(TestMethodReport testMethodReport) {
        super(testMethodReport);
    }

    public TestMethodSection(TestMethodReport testMethodReport, Method method) {
        super(testMethodReport, ReporterUtils.getTestMethodId(method));
        this.method = method;
    }

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

    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }

    public String getTestSuiteId() {
        return testSuiteId;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
