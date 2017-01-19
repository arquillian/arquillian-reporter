package org.arquillian.core.reporter.event;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.model.report.Report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteConfigurationContainerSection extends SectionEvent<TestSuiteConfigurationContainerSection, Report, TestSuiteConfigurationSection> {

    private String testSuiteId;

    public TestSuiteConfigurationContainerSection() {
    }

    public TestSuiteConfigurationContainerSection(String containerId, String testSuiteId) {
        super(containerId);
        this.testSuiteId = testSuiteId;
    }

    public TestSuiteConfigurationContainerSection(Report sectionReport, String containerId, String testSuiteId) {
        super(sectionReport, containerId);
        this.testSuiteId = testSuiteId;
    }

    @Override
    public TestSuiteConfigurationSection getParentSectionThisSectionBelongsTo() {
        return new TestSuiteConfigurationSection(testSuiteId, "containers");
    }

    @Override
    public Class<Report> getReportTypeClass() {
        return Report.class;
    }

    // todo support multiple test suites
}
