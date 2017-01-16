package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class DeploymentReportTestSuiteConfiguration extends ReportEvent<SectionReport, ContainerReportEventTestSuiteConfiguration> {

    public DeploymentReportTestSuiteConfiguration(String identifier) {
        super(identifier);
    }

    public DeploymentReportTestSuiteConfiguration(String identifier, String containerIdentifier) {
        super(identifier);
        setParentEvent(new ContainerReportEventTestSuiteConfiguration(containerIdentifier));
    }

    public DeploymentReportTestSuiteConfiguration(SectionReport sectionReport, String identifier,
        String containerIdentifier) {
        super(sectionReport, identifier);
        setParentEvent(new ContainerReportEventTestSuiteConfiguration(containerIdentifier));
    }
}
