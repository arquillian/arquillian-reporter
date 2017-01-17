package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.ReportNodeEvent;
import org.arquillian.reporter.api.model.report.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteConfigurationContainerDeploymentNode extends
    ReportNodeEvent<SectionReport, TestSuiteConfigurationContainerNode> {

    public TestSuiteConfigurationContainerDeploymentNode(){
    }

    public TestSuiteConfigurationContainerDeploymentNode(String identifier) {
        super(identifier);
    }

    public TestSuiteConfigurationContainerDeploymentNode(String identifier, String containerIdentifier) {
        super(identifier);
        setParentEvent(new TestSuiteConfigurationContainerNode(containerIdentifier));
    }

    public TestSuiteConfigurationContainerDeploymentNode(SectionReport sectionReport, String identifier,
        String containerIdentifier) {
        super(sectionReport, identifier);
        setParentEvent(new TestSuiteConfigurationContainerNode(containerIdentifier));
    }
}
