package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestClassReport;

/**
 * An implementation of {@link SectionEvent} that represents section for {@link ConfigurationReport}s within a {@link TestClassReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassConfigurationSection
    extends SectionEvent<TestClassConfigurationSection, ConfigurationReport, TestClassSection> {

    private Class<?> testClass;
    private String testSuiteId;

    /**
     * Creates an instance of {@link TestClassConfigurationSection}
     */
    public TestClassConfigurationSection() {
    }

    /**
     * Creates an instance of {@link TestClassConfigurationSection} with the given id.
     *
     * @param configurationId A configuration id to be used to identify the {@link TestClassConfigurationSection}
     */
    public TestClassConfigurationSection(String configurationId) {
        super(configurationId);
    }

    /**
     * Creates an instance of {@link TestClassConfigurationSection} with the given id.
     * It also stores the given test class for identifying parental section.
     *
     * @param configurationId A configuration id to be used to identify the {@link TestClassConfigurationSection}
     * @param testClass       A test class the {@link ConfigurationReport} belongs to.
     */
    public TestClassConfigurationSection(String configurationId, Class<?> testClass) {
        super(configurationId);
        this.testClass = testClass;
    }

    /**
     * Creates an instance of {@link TestClassConfigurationSection} with the given id.
     * It also stores the given test class and test suite id for identifying parental section.
     *
     * @param configurationId A configuration id to be used to identify the {@link TestClassConfigurationSection}
     * @param testClass       A test class the {@link ConfigurationReport} belongs to.
     * @param testSuiteId     An id of a test suite the {@link ConfigurationReport} belongs to.
     */
    public TestClassConfigurationSection(String configurationId, Class<?> testClass, String testSuiteId) {
        super(configurationId);
        this.testClass = testClass;
        this.testSuiteId = testSuiteId;
    }

    /**
     * Creates an instance of {@link TestClassConfigurationSection} with the given {@link ConfigurationReport}
     *
     * @param configuration A {@link ConfigurationReport} that should be contained within this {@link TestClassConfigurationSection}
     */
    public TestClassConfigurationSection(ConfigurationReport configuration) {
        super(configuration);
    }

    /**
     * Creates an instance of {@link TestClassConfigurationSection} with the given id and given {@link ConfigurationReport}
     *
     * @param configuration   A {@link ConfigurationReport} that should be contained within this {@link TestClassConfigurationSection}
     * @param configurationId A configuration id to be used to identify the {@link TestClassConfigurationSection}
     */
    public TestClassConfigurationSection(ConfigurationReport configuration, String configurationId) {
        super(configuration, configurationId);
    }

    /**
     * Creates an instance of {@link TestClassConfigurationSection} with the given id and given {@link ConfigurationReport}
     * It also stores the given test class for identifying parental section.
     *
     * @param configuration   A {@link ConfigurationReport} that should be contained within this {@link TestClassConfigurationSection}
     * @param configurationId A configuration id to be used to identify the {@link TestClassConfigurationSection}
     * @param testClass       A test class the {@link ConfigurationReport} belongs to.
     */
    public TestClassConfigurationSection(ConfigurationReport configuration, String configurationId,
        Class<?> testClass) {
        super(configuration, configurationId);
        this.testClass = testClass;
    }

    /**
     * Creates an instance of {@link TestClassConfigurationSection} with the given id and given {@link ConfigurationReport}
     * It also stores the given test class and test suite id for identifying parental section.
     *
     * @param configuration   A {@link ConfigurationReport} that should be contained within this {@link TestClassConfigurationSection}
     * @param configurationId A configuration id to be used to identify the {@link TestClassConfigurationSection}
     * @param testClass       A test class the {@link ConfigurationReport} belongs to.
     * @param testSuiteId     An id of a test suite the {@link ConfigurationReport} belongs to.
     */
    public TestClassConfigurationSection(ConfigurationReport configuration, String configurationId, Class<?> testClass,
        String testSuiteId) {
        super(configuration, configurationId);
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

    /**
     * Returns a test suite id of the {@link TestSuiteSection} this section belongs to
     *
     * @return A test suite id of the {@link TestSuiteSection} this section belongs to
     */
    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }
}
