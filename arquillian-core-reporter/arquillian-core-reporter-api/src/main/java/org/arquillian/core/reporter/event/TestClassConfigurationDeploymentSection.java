package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassConfigurationSection;
import org.arquillian.reporter.api.model.report.BasicReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassConfigurationDeploymentSection extends
    SectionEvent<TestClassConfigurationDeploymentSection, BasicReport, TestClassConfigurationSection> {

    private Class testClass;
    private String testSuiteId;

    public TestClassConfigurationDeploymentSection() {
    }

    public TestClassConfigurationDeploymentSection(String deploymentId, Class testClass) {
        super(deploymentId);
        this.testClass = testClass;
    }

    public TestClassConfigurationDeploymentSection(String deploymentId, Class testClass, String testSuiteId) {
        super(deploymentId);
        this.testClass = testClass;
        this.testSuiteId = testSuiteId;
    }

    public TestClassConfigurationDeploymentSection(BasicReport sectionReport, String deploymentId,
        Class testClass) {
        super(sectionReport, deploymentId);
        this.testClass = testClass;
    }

    @Override
    public TestClassConfigurationSection getParentSectionThisSectionBelongsTo() {
        TestClassConfigurationSection configSection = new TestClassConfigurationSection("deployments");
        if (testClass != null){
            configSection = new TestClassConfigurationSection("deployments", testClass);
        }
        configSection.setTestSuiteId(testSuiteId);
        return configSection;
    }

    @Override
    public Class<BasicReport> getReportTypeClass() {
        return BasicReport.class;
    }
}
