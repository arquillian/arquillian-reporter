package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.model.report.FailureReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodFailureNode extends ReportNodeEvent<FailureReport, TestMethodNode> {

    public TestMethodFailureNode(FailureReport failureReport) {
        super(failureReport);
    }

    public TestMethodFailureNode(String identifier) {
        super(identifier);
    }

    public TestMethodFailureNode(FailureReport failureReport, String identifier) {
        super(failureReport, identifier);
    }

    public TestMethodFailureNode(FailureReport failureReport, Method testMethod) {
        super(failureReport);
        setParentEvent(new TestMethodNode(testMethod));
    }

    public TestMethodFailureNode(String identifier, Method testMethod) {
        super(identifier);
        setParentEvent(new TestMethodNode(testMethod));
    }

    public TestMethodFailureNode(FailureReport failureReport, String identifier, Method testMethod) {
        super(failureReport, identifier);
        setParentEvent(new TestMethodNode(testMethod));
    }
}
