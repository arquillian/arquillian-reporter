package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class DeploymentReportTestSuiteConfiguration extends ContainerReportEventTestSuiteConfiguration {

    public DeploymentReportTestSuiteConfiguration(SectionReport sectionReport) {
        super(sectionReport);
    }

    public DeploymentReportTestSuiteConfiguration(String identifierSuffix) {
        super(identifierSuffix);
    }

    public DeploymentReportTestSuiteConfiguration(SectionReport sectionReport, String identifierSuffix) {
        super(sectionReport, identifierSuffix);
    }
}
