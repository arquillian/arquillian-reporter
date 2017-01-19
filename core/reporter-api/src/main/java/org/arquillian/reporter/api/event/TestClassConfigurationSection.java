package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.ConfigurationReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassConfigurationSection extends SectionEvent<TestClassConfigurationSection, ConfigurationReport, TestClassSection> {

    private Class<?> testClass;

    public TestClassConfigurationSection(Class<?> testClass) {
        super(testClass.getCanonicalName());
        this.testClass = testClass;
    }

    public TestClassConfigurationSection(ConfigurationReport configuration, Class<?> testClass) {
        super(configuration, testClass.getCanonicalName());
        this.testClass = testClass;
    }

    @Override
    public TestClassSection getParentSectionThisSectionBelongsTo() {
        return new TestClassSection(testClass);
    }

//    @Override
//    public Identifier<TestClassConfigurationSection> identifyYourself() {
//        if (testClass != null) {
//            return new Identifier<>(TestClassConfigurationSection.class, testClass.getCanonicalName());
//        }
//        return null;
//    }
}
