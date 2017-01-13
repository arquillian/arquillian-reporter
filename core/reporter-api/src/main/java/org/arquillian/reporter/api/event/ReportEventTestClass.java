package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportEventTestClass extends ReportEvent {

    public ReportEventTestClass(Class<?> testClass) {
        super(testClass.getCanonicalName());
    }

    public ReportEventTestClass(TestClassReport sectionReport, Class<?> testClass) {
        super(sectionReport, testClass.getCanonicalName());
    }

    public ReportEventTestClass inTestSuite(String testSuiteName){
        setParentEvent(new ReportEventTestSuite(testSuiteName));
        return this;
    }
}
