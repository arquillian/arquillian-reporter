package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;

/**
 * An implementation of {@link SectionEvent} that represents section for {@link ConfigurationReport}s within a {@link TestSuiteReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteConfigurationSection
    extends SectionEvent<TestSuiteConfigurationSection, ConfigurationReport, TestSuiteSection> {

    private String testSuiteId;

    /**
     * Creates an instance of {@link TestSuiteConfigurationSection}
     */
    public TestSuiteConfigurationSection() {
    }

    /**
     * Creates an instance of {@link TestSuiteConfigurationSection} with the given id
     *
     * @param configurationId A configuration id to be used to identify this {@link TestSuiteConfigurationSection}
     */
    public TestSuiteConfigurationSection(String configurationId) {
        super(configurationId);
    }

    /**
     * Creates an instance of {@link TestSuiteConfigurationSection} with the given id.
     * It also stores the given test suite id for identifying parental section.
     *
     * @param configurationId A configuration id to be used in resulting id
     * @param testSuiteId     An id of a test suite the {@link ConfigurationReport} belongs to.
     */
    public TestSuiteConfigurationSection(String configurationId, String testSuiteId) {
        super(configurationId);
        this.testSuiteId = testSuiteId;
    }

    /**
     * Creates an instance of {@link TestSuiteConfigurationSection} with the given {@link ConfigurationReport}
     *
     * @param configuration A {@link ConfigurationReport} that should be contained within this {@link TestSuiteConfigurationSection}
     */
    public TestSuiteConfigurationSection(ConfigurationReport configuration) {
        super(configuration);
    }

    /**
     * Creates an instance of {@link TestSuiteConfigurationSection} with the given {@link ConfigurationReport} and the given id.
     *
     * @param configuration   A {@link ConfigurationReport} that should be contained within this {@link TestSuiteConfigurationSection}
     * @param configurationId A configuration id to be used to identify this {@link TestSuiteConfigurationSection}
     */
    public TestSuiteConfigurationSection(ConfigurationReport configuration, String configurationId) {
        super(configuration, configurationId);
    }

    /**
     * Creates an instance of {@link TestSuiteConfigurationSection} with the given {@link ConfigurationReport} and the given id.
     * It also stores the given test suite id for identifying parental section.
     *
     * @param configuration   A {@link ConfigurationReport} that should be contained within this {@link TestSuiteConfigurationSection}
     * @param configurationId A configuration id to be used to identify this {@link TestSuiteConfigurationSection}
     * @param testSuiteId     An id of a test suite the {@link ConfigurationReport} belongs to.
     */
    public TestSuiteConfigurationSection(ConfigurationReport configuration, String configurationId,
        String testSuiteId) {
        super(configuration, configurationId);
        this.testSuiteId = testSuiteId;
    }

    @Override
    public TestSuiteSection getParentSectionThisSectionBelongsTo() {
        return new TestSuiteSection(testSuiteId);
    }

    @Override public Class<ConfigurationReport> getReportTypeClass() {
        return ConfigurationReport.class;
    }
}
