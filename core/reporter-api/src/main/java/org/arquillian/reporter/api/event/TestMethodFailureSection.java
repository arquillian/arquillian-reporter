package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.report.FailureReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodFailureSection extends SectionEvent<TestMethodFailureSection, FailureReport, TestMethodSection> {

    private Method testMethod;
    private String testSuiteId;

    public TestMethodFailureSection() {
    }

    public TestMethodFailureSection(Method testMethod, String failureId) {
        super(ReporterUtils.getTestMethodId(testMethod), failureId);
        this.testMethod = testMethod;
    }

    public TestMethodFailureSection(FailureReport failureReport, Method testMethod, String failureId) {
        super(failureReport, ReporterUtils.getTestMethodId(testMethod), failureId);
        this.testMethod = testMethod;
    }

    public TestMethodFailureSection(FailureReport failureReport, Method testMethod, String failureId,
        String testSuiteId) {
        super(failureReport, ReporterUtils.getTestMethodId(testMethod), failureId);
        this.testMethod = testMethod;
        this.testSuiteId = testSuiteId;
    }

    @Override
    public TestMethodSection getParentSectionThisSectionBelongsTo() {
        TestMethodSection testMethodSection = new TestMethodSection(testMethod);
        testMethodSection.setTestSuiteId(testSuiteId);
        return testMethodSection;
    }

    @Override
    public Class<FailureReport> getReportTypeClass() {
        return FailureReport.class;
    }

    // todo add into builder or into constructor
    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }

    public String getTestSuiteId() {
        return testSuiteId;
    }
}
