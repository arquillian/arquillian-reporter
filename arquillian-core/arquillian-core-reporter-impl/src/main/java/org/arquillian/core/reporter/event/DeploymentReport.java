package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class DeploymentReport extends ContainerReportEvent {

    public DeploymentReport(SectionReport sectionReport) {
        super(sectionReport);
    }

    public DeploymentReport(String identifierSuffix) {
        super(identifierSuffix);
    }

    public DeploymentReport(SectionReport sectionReport, String identifierSuffix) {
        super(sectionReport, identifierSuffix);
    }
}
