package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.ConfigurationReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassConfigurationSection extends SectionEvent<TestClassConfigurationSection, ConfigurationReport, TestClassSection> {

    private Class<?> testClass;

    public TestClassConfigurationSection() {
    }

    public TestClassConfigurationSection(Class<?> testClass, String configurationId) {
        super(testClass.getCanonicalName(), configurationId);
        this.testClass = testClass;
    }

    public TestClassConfigurationSection(ConfigurationReport configuration, Class<?> testClass, String configurationId) {
        super(configuration, testClass.getCanonicalName(), configurationId);
        this.testClass = testClass;
    }

    @Override
    public TestClassSection getParentSectionThisSectionBelongsTo() {
        return new TestClassSection(testClass);
    }

    @Override
    public Class<ConfigurationReport> getReportTypeClass() {
        return ConfigurationReport.class;
    }
}
