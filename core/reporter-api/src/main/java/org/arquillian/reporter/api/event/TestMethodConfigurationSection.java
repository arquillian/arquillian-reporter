package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.report.ConfigurationReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodConfigurationSection
    extends SectionEvent<TestMethodConfigurationSection, ConfigurationReport, TestMethodSection> {

    private Method testMethod;
    private String testSuiteId;

    public TestMethodConfigurationSection() {
    }

    public TestMethodConfigurationSection(ConfigurationReport configuration) {
        super(configuration);
    }

    public TestMethodConfigurationSection(Method testMethod, String configurationId) {
        super(ReporterUtils.getTestMethodId(testMethod), configurationId);
        this.testMethod = testMethod;
    }

    public TestMethodConfigurationSection(ConfigurationReport configuration, Method testMethod,
        String configurationId) {
        super(configuration, ReporterUtils.getTestMethodId(testMethod), configurationId);
        this.testMethod = testMethod;
    }

    public TestMethodConfigurationSection(ConfigurationReport configuration, Method testMethod,
        String configurationId, String testSuiteId) {
        super(configuration, ReporterUtils.getTestMethodId(testMethod), configurationId);
        this.testMethod = testMethod;
        this.testSuiteId = testSuiteId;
    }

    @Override
    public TestMethodSection getParentSectionThisSectionBelongsTo() {
        TestMethodSection testMethodSection = new TestMethodSection();
        if (testMethod != null){
            testMethodSection = new TestMethodSection(testMethod);
        }
        testMethodSection.setTestSuiteId(testSuiteId);
        return testMethodSection;
    }

    @Override
    public Class<ConfigurationReport> getReportTypeClass() {
        return ConfigurationReport.class;
    }

    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }

    public String getTestSuiteId() {
        return testSuiteId;
    }
}
