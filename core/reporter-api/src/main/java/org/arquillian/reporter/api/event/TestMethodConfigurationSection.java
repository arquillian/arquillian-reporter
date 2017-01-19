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

    public TestMethodConfigurationSection(Method testMethod) {
        super(Utils.getTestMethodId(testMethod));
        this.testMethod = testMethod;
    }

    public TestMethodConfigurationSection(ConfigurationReport configuration, Method testMethod) {
        super(configuration, Utils.getTestMethodId(testMethod));
        this.testMethod = testMethod;
    }


    @Override
    public TestMethodSection getParentSectionThisSectionBelongsTo() {
        return new TestMethodSection(testMethod);
    }
}
