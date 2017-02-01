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

    public TestMethodSection(TestMethodReport testMethodReport, Method method) {
        super(testMethodReport, ReporterUtils.getTestMethodId(method));
        this.method = method;
    }

    @Override
    public TestClassSection getParentSectionThisSectionBelongsTo() {
        if (testSuiteId == null) {
            return new TestClassSection(method.getDeclaringClass());
        } else {
            return new TestClassSection(method.getDeclaringClass(), testSuiteId);
        }
    }

    @Override
    public Class<TestMethodReport> getReportTypeClass() {
        return TestMethodReport.class;
    }

    // todo add into builder or into constructor
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
