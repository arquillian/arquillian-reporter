package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.Report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteConfigurationContainerDeploymentSection extends
    SectionEvent<TestSuiteConfigurationContainerDeploymentSection, Report, TestSuiteConfigurationContainerSection> {

    private String containerId;
    private String testSuiteId;

    public TestSuiteConfigurationContainerDeploymentSection() {
    }

    public TestSuiteConfigurationContainerDeploymentSection(String deploymentId, String containerId) {
        super(deploymentId);
        this.containerId = containerId;
    }

    public TestSuiteConfigurationContainerDeploymentSection(String deploymentId, String containerId, String testSuiteId) {
        super(deploymentId);
        this.containerId = containerId;
        this.testSuiteId = testSuiteId;
    }

    public TestSuiteConfigurationContainerDeploymentSection(Report sectionReport, String deploymentId,
        String containerId) {
        super(sectionReport, deploymentId);
        this.containerId = containerId;
    }

    @Override
    public TestSuiteConfigurationContainerSection getParentSectionThisSectionBelongsTo() {
        return new TestSuiteConfigurationContainerSection(containerId, testSuiteId);
    }

    @Override
    public Class<Report> getReportTypeClass() {
        return Report.class;
    }
}
