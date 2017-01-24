package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.report.FailureReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodFailureSection extends SectionEvent<TestMethodFailureSection, FailureReport, TestMethodSection> {

    private Method testMethod;

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

    @Override
    public TestMethodSection getParentSectionThisSectionBelongsTo() {
        return new TestMethodSection(testMethod);
    }

    @Override
    public Class<FailureReport> getReportTypeClass() {
        return FailureReport.class;
    }
}
