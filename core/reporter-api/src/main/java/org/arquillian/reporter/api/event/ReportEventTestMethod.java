package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.model.TestMethodReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportEventTestMethod extends ReportEvent<TestMethodReport, ReportEventTestClass> {

    public ReportEventTestMethod(Method method) {
        super(getIdentifier(method));
        setParentEvent(new ReportEventTestClass(method.getDeclaringClass()));
    }

    public ReportEventTestMethod(TestMethodReport sectionReport, Method method) {
        super(sectionReport, getIdentifier(method));
        setParentEvent(new ReportEventTestClass(method.getDeclaringClass()));
    }

    private static String getIdentifier(Method method) {
        return String.format("%s#%s", method.getDeclaringClass(), method.getName());
    }

}
