package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;

/**
 * An implementation of {@link SectionEvent} that represents section for {@link ConfigurationReport}s within a {@link TestMethodReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodConfigurationSection
    extends SectionEvent<TestMethodConfigurationSection, ConfigurationReport, TestMethodSection> implements Standalone {

    private Method testMethod;
    private String testSuiteId;

    /**
     * Creates an instance of {@link TestMethodConfigurationSection}
     */
    public TestMethodConfigurationSection() {
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with the given id.
     *
     * @param configurationId A configuration id to be used to identify the {@link TestMethodConfigurationSection}
     */
    public TestMethodConfigurationSection(String configurationId) {
        super(configurationId);
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with the given id.
     * It also stores the given test method for identifying parental section.
     *
     * @param configurationId A configuration id to be used to identify the {@link TestMethodConfigurationSection}
     * @param testMethod      A test method the {@link ConfigurationReport} belongs to.
     */
    public TestMethodConfigurationSection(String configurationId, Method testMethod) {
        super(configurationId);
        this.testMethod = testMethod;
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with the given id.
     * It also stores the given test method and test suite for identifying parental section.
     *
     * @param configurationId A configuration id to be used to identify the {@link TestMethodConfigurationSection}
     * @param testMethod      A test method the {@link ConfigurationReport} belongs to.
     * @param testSuiteId     An id of a test suite the {@link ConfigurationReport} belongs to.
     */
    public TestMethodConfigurationSection(String configurationId, Method testMethod, String testSuiteId) {
        super(configurationId);
        this.testMethod = testMethod;
        this.testSuiteId = testSuiteId;
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with the given {@link ConfigurationReport}
     *
     * @param configuration A {@link ConfigurationReport} that should be contained as the payload of this {@link TestMethodConfigurationSection}
     */
    public TestMethodConfigurationSection(ConfigurationReport configuration) {
        super(configuration);
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with with the given {@link ConfigurationReport}
     * and an id represented by the given configurationId
     *
     * @param configuration   A {@link ConfigurationReport} that should be contained as the payload of this {@link TestMethodConfigurationSection}
     * @param configurationId A configuration id to be used to identify the {@link TestMethodConfigurationSection}
     */
    public TestMethodConfigurationSection(ConfigurationReport configuration, String configurationId) {
        super(configuration, configurationId);
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with with the given id and given {@link ConfigurationReport}
     * It also stores the given test method for identifying parental section.
     *
     * @param configuration   A {@link ConfigurationReport} that should be contained as the payload of this {@link TestMethodConfigurationSection}
     * @param configurationId A configuration id to be used to identify the {@link TestMethodConfigurationSection}
     * @param testMethod      A test method the {@link ConfigurationReport} belongs to.
     */
    public TestMethodConfigurationSection(ConfigurationReport configuration, String configurationId,
        Method testMethod) {
        super(configuration, configurationId);
        this.testMethod = testMethod;
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with with the given id and given {@link ConfigurationReport}
     * It also stores the given test method and test suite for identifying parental section.
     *
     * @param configuration   A {@link ConfigurationReport} that should be contained as the payload of this {@link TestMethodConfigurationSection}
     * @param configurationId A configuration id to be used to identify the {@link TestMethodConfigurationSection}
     * @param testMethod      A test method the {@link ConfigurationReport} belongs to.
     * @param testSuiteId     An id of a test suite the {@link ConfigurationReport} belongs to.
     */
    public TestMethodConfigurationSection(ConfigurationReport configuration, String configurationId, Method testMethod,
        String testSuiteId) {
        super(configuration, configurationId);
        this.testMethod = testMethod;
        this.testSuiteId = testSuiteId;
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection}.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of configuration reports no matter what type it is.
     * The given.
     */
    public static TestMethodConfigurationSection standalone() {
        return new TestMethodConfigurationSection(Standalone.getStandaloneId());
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with the given test method that is stored to identify a parental section.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of configuration reports no matter what type it is.
     * The given.
     *
     * @param testMethod A test method the {@link ConfigurationReport} belongs to.
     */
    public static TestMethodConfigurationSection standalone(Method testMethod) {
        return new TestMethodConfigurationSection(Standalone.getStandaloneId(), testMethod);
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with the given test method and test suite id that are stored
     * to identify a parental section.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of configuration reports no matter what type it is.
     * The given .
     *
     * @param testMethod  A test method the {@link ConfigurationReport} belongs to.
     * @param testSuiteId An id of a test suite the {@link ConfigurationReport} belongs to.
     */
    public static TestMethodConfigurationSection standalone(Method testMethod, String testSuiteId) {
        return new TestMethodConfigurationSection(Standalone.getStandaloneId(), testMethod, testSuiteId);
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with the given {@link ConfigurationReport}.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of configuration reports no matter what type it is.
     *
     * @param configurationReport A {@link ConfigurationReport} that should be contained as the payload of this {@link TestMethodConfigurationSection}
     */
    public static TestMethodConfigurationSection standalone(ConfigurationReport configurationReport) {
        return new TestMethodConfigurationSection(configurationReport, Standalone.getStandaloneId());
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with the given {@link ConfigurationReport}.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of configuration reports no matter what type it is.
     * The given test method is stored to identify a parental section.
     *
     * @param configurationReport A {@link ConfigurationReport} that should be contained as the payload of this {@link TestMethodConfigurationSection}
     * @param testMethod          A test method the {@link ConfigurationReport} belongs to.
     */
    public static TestMethodConfigurationSection standalone(ConfigurationReport configurationReport,
        Method testMethod) {
        return new TestMethodConfigurationSection(configurationReport, Standalone.getStandaloneId(), testMethod);
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with the given {@link ConfigurationReport}.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of configuration reports no matter what type it is.
     * The given test method and test suite id are stored to identify a parental section.
     *
     * @param configurationReport A {@link ConfigurationReport} that should be contained as the payload of this {@link TestMethodConfigurationSection}
     * @param testMethod          A test method the {@link ConfigurationReport} belongs to.
     * @param testSuiteId         An id of a test suite the {@link ConfigurationReport} belongs to.
     */
    public static TestMethodConfigurationSection standalone(ConfigurationReport configurationReport, Method testMethod,
        String testSuiteId) {
        return new TestMethodConfigurationSection(configurationReport, Standalone.getStandaloneId(), testMethod,
                                                  testSuiteId);
    }

    @Override
    public TestMethodSection getParentSectionThisSectionBelongsTo() {
        TestMethodSection testMethodSection = new TestMethodSection();
        if (testMethod != null) {
            testMethodSection = new TestMethodSection(testMethod);
        }
        testMethodSection.setTestSuiteId(testSuiteId);
        return testMethodSection;
    }

    @Override
    public Class<ConfigurationReport> getReportTypeClass() {
        return ConfigurationReport.class;
    }

    /**
     * Sets a test suite id of the {@link TestSuiteSection} this section belongs to
     *
     * @param testSuiteId A test suite id of the {@link TestSuiteSection} this section belongs to
     */
    public void setTestSuiteId(String testSuiteId) {
        this.testSuiteId = testSuiteId;
    }

    /**
     * Returns a test suite id of the {@link TestSuiteSection} this section belongs to
     *
     * @return A test suite id of the {@link TestSuiteSection} this section belongs to
     */
    public String getTestSuiteId() {
        return testSuiteId;
    }
}
