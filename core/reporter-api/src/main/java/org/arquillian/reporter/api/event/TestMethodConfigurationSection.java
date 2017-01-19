package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.model.report.ConfigurationReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodConfigurationSection
    extends SectionEvent<TestMethodConfigurationSection, ConfigurationReport, TestMethodSection> {

    private Method testMethod;

    public TestMethodConfigurationSection() {
    }

    public TestMethodConfigurationSection(Method testMethod, String configurationId) {
        super(Utils.getTestMethodId(testMethod), configurationId);
        this.testMethod = testMethod;
    }

    public TestMethodConfigurationSection(ConfigurationReport configuration, Method testMethod, String configurationId) {
        super(configuration, Utils.getTestMethodId(testMethod), configurationId);
        this.testMethod = testMethod;
    }


    @Override
    public TestMethodSection getParentSectionThisSectionBelongsTo() {
        return new TestMethodSection(testMethod);
    }

    @Override
    public Class<ConfigurationReport> getReportTypeClass() {
        return ConfigurationReport.class;
    }
}
