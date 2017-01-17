package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.model.report.TestMethodReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodNode extends ReportNodeEvent<TestMethodReport, TestClassNode> {

    public TestMethodNode(Method method) {
        super(getIdentifier(method));
        setParentEvent(new TestClassNode(method.getDeclaringClass()));
    }

    public TestMethodNode(TestMethodReport testMethodReport, Method method) {
        super(testMethodReport, getIdentifier(method));
        setParentEvent(new TestClassNode(method.getDeclaringClass()));
    }

    private static String getIdentifier(Method method) {
        return String.format("%s#%s", method.getDeclaringClass(), method.getName());
    }

}
