package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.utils.ReporterUtils;

/**
 * An implementation of {@link SectionEvent} that represents section for {@link ConfigurationReport}s within a {@link TestMethodReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodConfigurationSection
    extends SectionEvent<TestMethodConfigurationSection, ConfigurationReport, TestMethodSection> {

    private Method testMethod;
    private String testSuiteId;

    /**
     * Creates an instance of {@link TestMethodConfigurationSection}
     */
    public TestMethodConfigurationSection() {
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with the given id
     *
     * @param configurationId A configuration id to be used to identify the {@link TestMethodConfigurationSection}
     */
    public TestMethodConfigurationSection(String configurationId) {
        super(configurationId);
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with an id build from the given configurationId and
     * test method id of the method the {@link ConfigurationReport} belongs to.
     * <p>
     * The resulting id is created as: 'declaring.test.class.canonical.Name#methodName#configurationId'
     * </p>
     * It also stores the given test method for identifying parental section.
     *
     * @param configurationId A configuration id to be used in resulting id
     * @param testMethod      A test method the {@link ConfigurationReport} belongs to.
     */
    public TestMethodConfigurationSection(String configurationId, Method testMethod) {
        super(ReporterUtils.getTestMethodId(testMethod), configurationId);
        this.testMethod = testMethod;
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with an id build from the given configurationId and
     * test method id of the method the {@link ConfigurationReport} belongs to.
     * <p>
     * The resulting id is created as: 'declaring.test.class.canonical.Name#methodName#configurationId'
     * </p>
     * It also stores the given test method and test suite for identifying parental section.
     *
     * @param configurationId A configuration id to be used in resulting id
     * @param testMethod      A test method the {@link ConfigurationReport} belongs to.
     * @param testSuiteId     An id of a test suite the {@link ConfigurationReport} belongs to.
     */
    public TestMethodConfigurationSection(String configurationId, Method testMethod, String testSuiteId) {
        super(ReporterUtils.getTestMethodId(testMethod), configurationId);
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
     * @param configurationId A configuration id to be used in resulting id
     */
    public TestMethodConfigurationSection(ConfigurationReport configuration, String configurationId) {
        super(configuration, configurationId);
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with with the given {@link ConfigurationReport}
     * and an id build from the given configurationId and test method id of the method the {@link ConfigurationReport} belongs to.
     * <p>
     * The resulting id is created as: 'declaring.test.class.canonical.Name#methodName#configurationId'
     * </p>
     * It also stores the given test method for identifying parental section.
     *
     * @param configuration   A {@link ConfigurationReport} that should be contained as the payload of this {@link TestMethodConfigurationSection}
     * @param configurationId A configuration id to be used in resulting id
     * @param testMethod      A test method the {@link ConfigurationReport} belongs to.
     */
    public TestMethodConfigurationSection(ConfigurationReport configuration, String configurationId,
        Method testMethod) {
        super(configuration, ReporterUtils.getTestMethodId(testMethod), configurationId);
        this.testMethod = testMethod;
    }

    /**
     * Creates an instance of {@link TestMethodConfigurationSection} with with the given {@link ConfigurationReport}
     * and an id build from the given configurationId and test method id of the method the {@link ConfigurationReport} belongs to.
     * <p>
     * The resulting id is created as: 'declaring.test.class.canonical.Name#methodName#configurationId'
     * </p>
     * It also stores the given test method and test suite for identifying parental section.
     *
     * @param configuration   A {@link ConfigurationReport} that should be contained as the payload of this {@link TestMethodConfigurationSection}
     * @param configurationId A configuration id to be used in resulting id
     * @param testMethod      A test method the {@link ConfigurationReport} belongs to.
     * @param testSuiteId     An id of a test suite the {@link ConfigurationReport} belongs to.
     */
    public TestMethodConfigurationSection(ConfigurationReport configuration, String configurationId, Method testMethod,
        String testSuiteId) {
        super(configuration, ReporterUtils.getTestMethodId(testMethod), configurationId);
        this.testMethod = testMethod;
        this.testSuiteId = testSuiteId;
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
