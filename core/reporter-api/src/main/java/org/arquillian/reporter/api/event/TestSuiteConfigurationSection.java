package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.ConfigurationReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteConfigurationSection
    extends SectionEvent<TestSuiteConfigurationSection, ConfigurationReport, TestSuiteSection> {

    private String testSuiteId;

    public TestSuiteConfigurationSection(String testSuiteId) {
        super(testSuiteId);
        this.testSuiteId = testSuiteId;
    }

    public TestSuiteConfigurationSection(ConfigurationReport section, String testSuiteId) {
        super(section, testSuiteId);
        this.testSuiteId = testSuiteId;
    }

    @Override
    public TestSuiteSection getParentSectionThisSectionBelongsTo() {
        return new TestSuiteSection(testSuiteId);
    }
}
