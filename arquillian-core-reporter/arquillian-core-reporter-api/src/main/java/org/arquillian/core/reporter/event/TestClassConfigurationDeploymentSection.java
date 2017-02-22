package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassConfigurationSection;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.ConfigurationReport;

/**
 * An implementation of {@link SectionEvent} that represents section for deployment related reports within a {@link ConfigurationReport}
 * of some {@link TestClassReport}.
 * <p>
 * This section is added as a subsection of the section node identified by TestClassConfigurationSection + "deployments".
 * If there isn't any parental deployments-config section/report present then it is automatically created.
 * </p>
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassConfigurationDeploymentSection extends
    SectionEvent<TestClassConfigurationDeploymentSection, BasicReport, TestClassConfigurationSection> {

    private Class testClass;
    private String testSuiteId;

    /**
     * Creates an instance of {@link TestClassConfigurationDeploymentSection}
     */
    public TestClassConfigurationDeploymentSection() {
    }

    /**
     * Creates an instance of {@link TestClassConfigurationDeploymentSection} with the given deployment id
     *
     * @param deploymentId A deployment id to be used to identify this {@link SectionEvent}
     */
    public TestClassConfigurationDeploymentSection(String deploymentId) {
        super(deploymentId);
    }

    /**
     * Creates an instance of {@link TestClassConfigurationDeploymentSection} with an id build from the given deploymentId and
     * canonical name of the given testClass the deployment was deployed from.
     * <p>
     * The resulting id is created as: 'test.class.canonical.name#deploymentId'
     * </p>
     * It also stores the given test class for identifying parental section.
     *
     * @param deploymentId A deployment id to be used in resulting id
     * @param testClass    A test class the deployment was deployed from
     */
    public TestClassConfigurationDeploymentSection(String deploymentId, Class testClass) {
        super(deploymentId);
        this.testClass = testClass;
    }

    /**
     * Creates an instance of {@link TestClassConfigurationDeploymentSection} with an id build from the given deploymentId and
     * canonical name of the given testClass the deployment was deployed from.
     * <p>
     * The resulting id is created as: 'test.class.canonical.name#deploymentId'
     * </p>
     * It also stores the given test class and test suite id for identifying parental section.
     *
     * @param deploymentId A deployment id to be used in resulting id
     * @param testClass    A test class the deployment was deployed from
     * @param testSuiteId  A test suite id of a test suite the test is running in
     */
    public TestClassConfigurationDeploymentSection(String deploymentId, Class testClass, String testSuiteId) {
        super(deploymentId);
        this.testClass = testClass;
        this.testSuiteId = testSuiteId;
    }

    /**
     * Creates an instance of {@link TestClassConfigurationDeploymentSection} with the given {@link BasicReport}
     *
     * @param sectionReport A {@link BasicReport} that should be contained within this {@link TestClassConfigurationDeploymentSection}
     */
    public TestClassConfigurationDeploymentSection(BasicReport sectionReport) {
        super(sectionReport);
    }

    /**
     * Creates an instance of {@link TestClassConfigurationDeploymentSection} with the given {@link BasicReport} and deployment id
     *
     * @param sectionReport A {@link BasicReport} that should be contained within this {@link TestClassConfigurationDeploymentSection}
     * @param deploymentId  A deployment id to be used to identify this {@link SectionEvent}
     */
    public TestClassConfigurationDeploymentSection(BasicReport sectionReport, String deploymentId) {
        super(sectionReport, deploymentId);
    }

    /**
     * Creates an instance of {@link TestClassConfigurationDeploymentSection} with the given {@link BasicReport} and
     * an id build from the given deploymentId and canonical name of the given testClass the deployment was deployed from.
     * <p>
     * The resulting id is created as: 'test.class.canonical.name#deploymentId'
     * </p>
     * It also stores the given test class for identifying parental section.
     *
     * @param sectionReport A {@link BasicReport} that should be contained within this {@link TestClassConfigurationDeploymentSection}
     * @param deploymentId  A configuration id to be used in resulting id
     * @param testClass     A test class the deployment was deployed from
     */
    public TestClassConfigurationDeploymentSection(BasicReport sectionReport, String deploymentId, Class testClass) {
        super(sectionReport, deploymentId);
        this.testClass = testClass;
    }

    /**
     * Creates an instance of {@link TestClassConfigurationDeploymentSection} with the given {@link BasicReport} and
     * an id build from the given deploymentId and canonical name of the given testClass the deployment was deployed from.
     * <p>
     * The resulting id is created as: 'test.class.canonical.name#deploymentId'
     * </p>
     * It also stores the given test class and test suite id for identifying parental section.
     *
     * @param sectionReport A {@link BasicReport} that should be contained within this {@link TestClassConfigurationDeploymentSection}
     * @param deploymentId  A configuration id to be used in resulting id
     * @param testClass     A test class the deployment was deployed from
     * @param testSuiteId   A test suite id of a test suite the test is running in
     */
    public TestClassConfigurationDeploymentSection(BasicReport sectionReport, String deploymentId, Class testClass,
        String testSuiteId) {
        super(sectionReport, deploymentId);
        this.testClass = testClass;
        this.testSuiteId = testSuiteId;
    }

    @Override
    public TestClassConfigurationSection getParentSectionThisSectionBelongsTo() {
        TestClassConfigurationSection configSection = new TestClassConfigurationSection("deployments");
        if (testClass != null) {
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
