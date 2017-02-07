package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.ConfigurationReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassConfigurationSection
    extends SectionEvent<TestClassConfigurationSection, ConfigurationReport, TestClassSection> {

    private Class<?> testClass;
    private String testSuiteId;

    public TestClassConfigurationSection() {
    }

    public TestClassConfigurationSection(Class<?> testClass, String configurationId) {
        super(testClass.getCanonicalName(), configurationId);
        this.testClass = testClass;
    }

    // todo rewrite
    public TestClassConfigurationSection(ConfigurationReport configuration, Class<?> testClass,
        String configurationId) {
        super(configuration, testClass.getCanonicalName(), configurationId);
        this.testClass = testClass;
    }

    public TestClassConfigurationSection(ConfigurationReport configuration, Class<?> testClass,
        String configurationId, String testSuiteId) {
        super(configuration, testClass.getCanonicalName(), configurationId);
        this.testClass = testClass;
        this.testSuiteId = testSuiteId;
    }

    @Override
    public TestClassSection getParentSectionThisSectionBelongsTo() {
        if (testSuiteId == null) {
            return new TestClassSection(testClass);
        } else {
            return new TestClassSection(testClass, testSuiteId);
        }
    }

    @Override
    public Class<ConfigurationReport> getReportTypeClass() {
        return ConfigurationReport.class;
    }

    // todo add into builder or into constructor
    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }
}
