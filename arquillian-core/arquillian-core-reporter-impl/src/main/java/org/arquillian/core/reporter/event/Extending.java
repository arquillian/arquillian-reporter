package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.ReportEventTestClass;
import org.arquillian.reporter.api.model.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Extending extends ReportEventTestClass {
    public Extending(Class<?> testClass) {
        super(testClass);
    }

    public Extending(Class<?> testClass, String testSuiteName) {
        super(testClass, testSuiteName);
        setpa
    }

    public Extending(TestClassReport sectionReport,
        Class<?> testClass) {
        super(sectionReport, testClass);
    }
}
