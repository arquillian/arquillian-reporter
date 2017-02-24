package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;

/**
 * An implementation of {@link SectionEvent} that represents section for container related reports within a {@link ConfigurationReport}
 * of some {@link TestSuiteReport}.
 * <p>
 * This section is added as a subsection of the section node identified by TestSuiteConfigurationSection + "containers".
 * If there isn't any parental containers-config section/report present then it is automatically created.
 * </p>
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteConfigurationContainerSection
    extends SectionEvent<TestSuiteConfigurationContainerSection, BasicReport, TestSuiteConfigurationSection> {

    private String testSuiteId;
    public static final String TEST_SUITE_CONFIGURATION_SECTION_ID = "containers";

    /**
     * Creates an instance of {@link TestSuiteConfigurationContainerSection}
     */
    public TestSuiteConfigurationContainerSection() {
    }

    /**
     * Creates an instance of {@link TestSuiteConfigurationContainerSection} with the given container id
     *
     * @param containerId A container id to be used to identify this {@link TestSuiteConfigurationContainerSection}
     */
    public TestSuiteConfigurationContainerSection(String containerId) {
        super(containerId);
    }

    /**
     * Creates an instance of {@link TestSuiteConfigurationContainerSection} with the given container id.
     * It also stores the given test suite id for identifying parental section.
     * <p>
     * It also stores the given test class and test suite id for identifying parental section.
     *
     * @param containerId A container id to be used to identify this {@link TestSuiteConfigurationContainerSection}
     * @param testSuiteId A test suite id of some test suite the container is running in
     */
    public TestSuiteConfigurationContainerSection(String containerId, String testSuiteId) {
        super(containerId);
        this.testSuiteId = testSuiteId;
    }

    /**
     * Creates an instance of {@link TestSuiteConfigurationContainerSection} with the given {@link BasicReport}
     *
     * @param sectionReport A {@link BasicReport} that should be contained within the {@link TestSuiteConfigurationContainerSection}
     */
    public TestSuiteConfigurationContainerSection(BasicReport sectionReport) {
        super(sectionReport);
    }

    /**
     * Creates an instance of {@link TestSuiteConfigurationContainerSection} with the given {@link BasicReport} and container id
     *
     * @param sectionReport A {@link BasicReport} that should be contained within the {@link TestSuiteConfigurationContainerSection}
     * @param containerId   A container id to be used to identify this {@link TestSuiteConfigurationContainerSection}
     */
    public TestSuiteConfigurationContainerSection(BasicReport sectionReport, String containerId) {
        super(sectionReport, containerId);
    }

    /**
     * Creates an instance of {@link TestSuiteConfigurationContainerSection} with the given {@link BasicReport} and container id
     * It also stores the given test suite id for identifying parental section.
     *
     * @param sectionReport A {@link BasicReport} that should be contained within the {@link TestSuiteConfigurationContainerSection}
     * @param containerId   A container id to be used to identify this {@link TestSuiteConfigurationContainerSection}
     * @param testSuiteId   A test suite id of some test suite the container is running in
     */
    public TestSuiteConfigurationContainerSection(BasicReport sectionReport, String containerId, String testSuiteId) {
        super(sectionReport, containerId);
        this.testSuiteId = testSuiteId;
    }

    @Override
    public TestSuiteConfigurationSection getParentSectionThisSectionBelongsTo() {
        return new TestSuiteConfigurationSection(TEST_SUITE_CONFIGURATION_SECTION_ID, testSuiteId);
    }

    @Override
    public Class<BasicReport> getReportTypeClass() {
        return BasicReport.class;
    }
}
