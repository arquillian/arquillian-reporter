package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.model.TestMethodSection;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportTestMethod extends ReportEvent<TestMethodSection, ReportTestClass> {

    public ReportTestMethod(Method method) {
        super(getIdentifier(method));
        setParentEvent(new ReportTestClass(method.getDeclaringClass()));
    }

    public ReportTestMethod(TestMethodSection sectionReport, Method method) {
        super(sectionReport, getIdentifier(method));
        setParentEvent(new ReportTestClass(method.getDeclaringClass()));
    }

    private static String getIdentifier(Method method) {
        return String.format("%s#%s", method.getDeclaringClass(), method.getName());
    }

}
