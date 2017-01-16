package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportEventTestClass extends ReportEvent<TestClassReport, ReportEventTestSuite> {

    public ReportEventTestClass(Class<?> testClass) {
        super(testClass.getCanonicalName());
    }

    public ReportEventTestClass(Class<?> testClass, String testSuiteName) {
        super(testClass.getCanonicalName());
        setParentEvent(new ReportEventTestSuite(testSuiteName));
    }

    public ReportEventTestClass(TestClassReport sectionReport, Class<?> testClass) {
        super(sectionReport, testClass.getCanonicalName());
    }
}
