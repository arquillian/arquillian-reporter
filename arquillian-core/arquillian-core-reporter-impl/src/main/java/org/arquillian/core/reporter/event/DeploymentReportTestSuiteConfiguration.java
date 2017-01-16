package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.Section;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class DeploymentReportTestSuiteConfiguration extends ReportEvent<Section, ContainerReportEventTestSuiteConfiguration> {

    public DeploymentReportTestSuiteConfiguration(String identifier) {
        super(identifier);
    }

    public DeploymentReportTestSuiteConfiguration(String identifier, String containerIdentifier) {
        super(identifier);
        setParentEvent(new ContainerReportEventTestSuiteConfiguration(containerIdentifier));
    }

    public DeploymentReportTestSuiteConfiguration(Section section, String identifier,
        String containerIdentifier) {
        super(section, identifier);
        setParentEvent(new ContainerReportEventTestSuiteConfiguration(containerIdentifier));
    }
}
