package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassNode extends ReportNodeEvent<TestClassReport, TestSuiteNode> {

    public TestClassNode(Class<?> testClass) {
        super(testClass.getCanonicalName());
    }

    public TestClassNode(Class<?> testClass, String testSuiteName) {
        super(testClass.getCanonicalName());
        setParentEvent(new TestSuiteNode(testSuiteName));
    }

    public TestClassNode(TestClassReport testClassReport, Class<?> testClass) {
        super(testClassReport, testClass.getCanonicalName());
    }
}
