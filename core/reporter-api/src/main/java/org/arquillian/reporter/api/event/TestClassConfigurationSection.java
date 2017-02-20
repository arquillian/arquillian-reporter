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

    public TestClassConfigurationSection(String configurationId) {
        super(configurationId);
    }

    public TestClassConfigurationSection(String configurationId, Class<?> testClass) {
        super(testClass.getCanonicalName(), configurationId);
        this.testClass = testClass;
    }

    public TestClassConfigurationSection(String configurationId, Class<?> testClass, String testSuiteId) {
        super(testClass.getCanonicalName(), configurationId);
        this.testClass = testClass;
        this.testSuiteId = testSuiteId;
    }

    public TestClassConfigurationSection(ConfigurationReport configuration) {
        super(configuration);
    }

    public TestClassConfigurationSection(ConfigurationReport configuration, String configurationId) {
        super(configuration, configurationId);
    }

    public TestClassConfigurationSection(ConfigurationReport configuration, String configurationId,
        Class<?> testClass) {
        super(configuration, testClass.getCanonicalName(), configurationId);
        this.testClass = testClass;
    }

    public TestClassConfigurationSection(ConfigurationReport configuration, String configurationId, Class<?> testClass,
        String testSuiteId) {
        super(configuration, testClass.getCanonicalName(), configurationId);
        this.testClass = testClass;
        this.testSuiteId = testSuiteId;
    }

    @Override
    public TestClassSection getParentSectionThisSectionBelongsTo() {
        TestClassSection testClassSection = new TestClassSection();

        if (testClass != null) {
            testClassSection = new TestClassSection(testClass);
        }
        testClassSection.setTestSuiteId(testSuiteId);

        return testClassSection;
    }

    @Override
    public Class<ConfigurationReport> getReportTypeClass() {
        return ConfigurationReport.class;
    }

    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }
}
