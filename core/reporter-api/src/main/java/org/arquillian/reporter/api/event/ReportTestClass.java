package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.TestClassSection;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportTestClass extends ReportEvent<TestClassSection, ReportTestSuite> {

    public ReportTestClass(Class<?> testClass) {
        super(testClass.getCanonicalName());
    }

    public ReportTestClass(Class<?> testClass, String testSuiteName) {
        super(testClass.getCanonicalName());
        setParentEvent(new ReportTestSuite(testSuiteName));
    }

    public ReportTestClass(TestClassSection sectionReport, Class<?> testClass) {
        super(sectionReport, testClass.getCanonicalName());
    }
}
