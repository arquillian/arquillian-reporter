package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.model.report.TestMethodReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodSection extends SectionEvent<TestMethodSection, TestMethodReport, TestClassSection> {

    private Method method;

    public TestMethodSection() {
    }

    public TestMethodSection(Method method) {
        super(Utils.getTestMethodId(method));
        this.method = method;
    }

    public TestMethodSection(TestMethodReport testMethodReport, Method method) {
        super(testMethodReport, Utils.getTestMethodId(method));
        this.method = method;
    }


    @Override
    public TestClassSection getParentSectionThisSectionBelongsTo() {
        return new TestClassSection(method.getDeclaringClass());
    }

    @Override
    public Class<TestMethodReport> getReportTypeClass() {
        return TestMethodReport.class;
    }
}
