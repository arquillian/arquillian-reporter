package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;

/**
 * An implementation of {@link SectionEvent} that represents section for {@link FailureReport}s within a {@link TestMethodReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodFailureSection extends SectionEvent<TestMethodFailureSection, FailureReport, TestMethodSection>
    implements Standalone {

    private Method testMethod;
    private String testSuiteId;

    /**
     * Creates an instance of {@link TestMethodFailureSection}
     */
    public TestMethodFailureSection() {
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given id.
     *
     * @param failureId A failure id to be used to identify the {@link TestMethodFailureSection}
     */
    public TestMethodFailureSection(String failureId) {
        super(failureId);
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given id.
     * It also stores the given test method for identifying parental section.
     *
     * @param failureId  A failure id to be used to identify the {@link TestMethodFailureSection}
     * @param testMethod A test method the {@link FailureReport} belongs to.
     */
    public TestMethodFailureSection(String failureId, Method testMethod) {
        super(failureId);
        this.testMethod = testMethod;
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given id.
     * It also stores the given test method and test suite for identifying parental section.
     *
     * @param failureId   A failure id to be used to identify the {@link TestMethodFailureSection}
     * @param testMethod  A test method the {@link FailureReport} belongs to.
     * @param testSuiteId An id of a test suite the {@link FailureReport} belongs to.
     */
    public TestMethodFailureSection(String failureId, Method testMethod, String testSuiteId) {
        super(failureId);
        this.testMethod = testMethod;
        this.testSuiteId = testSuiteId;
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given {@link FailureReport}
     *
     * @param failureReport A {@link FailureReport} that should be contained as the payload of this {@link TestMethodFailureSection}
     */
    public TestMethodFailureSection(FailureReport failureReport) {
        super(failureReport);
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given {@link FailureReport}
     * and an id represented by the given failureId
     *
     * @param failureReport A {@link FailureReport} that should be contained as the payload of this {@link TestMethodFailureSection}
     * @param failureId     A failure id to be used to identify the {@link TestMethodFailureSection}
     */
    public TestMethodFailureSection(FailureReport failureReport, String failureId) {
        super(failureReport, failureId);
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given {@link FailureReport}
     * and an id represented by the given failureId
     * It also stores the given test method for identifying parental section.
     *
     * @param failureReport A {@link FailureReport} that should be contained as the payload of this {@link TestMethodFailureSection}
     * @param failureId     A failure id to be used to identify the {@link TestMethodFailureSection}
     * @param testMethod    A test method the {@link FailureReport} belongs to.
     */
    public TestMethodFailureSection(FailureReport failureReport, String failureId, Method testMethod) {
        super(failureReport, failureId);
        this.testMethod = testMethod;
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given {@link FailureReport}
     * and an id represented by the given failureId
     * It also stores the given test method and test suite id for identifying parental section.
     *
     * @param failureReport A {@link FailureReport} that should be contained as the payload of this {@link TestMethodFailureSection}
     * @param failureId     A failure id to be used to identify the {@link TestMethodFailureSection}
     * @param testMethod    A test method the {@link FailureReport} belongs to.
     * @param testSuiteId   An id of a test suite the {@link FailureReport} belongs to.
     */
    public TestMethodFailureSection(FailureReport failureReport, String failureId, Method testMethod,
        String testSuiteId) {
        super(failureReport, failureId);
        this.testMethod = testMethod;
        this.testSuiteId = testSuiteId;
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection}.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of failure reports no matter what type it is.
     * The given.
     */
    public static TestMethodFailureSection standalone() {
        return new TestMethodFailureSection(Standalone.getStandaloneId());
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given test method that is stored to identify a parental section.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of failure reports no matter what type it is.
     * The given.
     *
     * @param testMethod    A test method the {@link FailureReport} belongs to.
     */
    public static TestMethodFailureSection standalone(Method testMethod) {
        return new TestMethodFailureSection(Standalone.getStandaloneId(), testMethod);
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given test method and test suite id that are stored
     * to identify a parental section.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of failure reports no matter what type it is.
     * The given .
     *
     * @param testMethod    A test method the {@link FailureReport} belongs to.
     * @param testSuiteId   An id of a test suite the {@link FailureReport} belongs to.
     */
    public static TestMethodFailureSection standalone(Method testMethod, String testSuiteId) {
        return new TestMethodFailureSection(Standalone.getStandaloneId(), testMethod, testSuiteId);
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given {@link FailureReport}.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of failure reports no matter what type it is.
     *
     * @param failureReport A {@link FailureReport} that should be contained as the payload of this {@link TestMethodFailureSection}
     */
    public static TestMethodFailureSection standalone(FailureReport failureReport) {
        return new TestMethodFailureSection(failureReport, Standalone.getStandaloneId());
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given {@link FailureReport}.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of failure reports no matter what type it is.
     * The given test method is stored to identify a parental section.
     *
     * @param failureReport A {@link FailureReport} that should be contained as the payload of this {@link TestMethodFailureSection}
     * @param testMethod    A test method the {@link FailureReport} belongs to.
     */
    public static TestMethodFailureSection standalone(FailureReport failureReport, Method testMethod) {
        return new TestMethodFailureSection(failureReport, Standalone.getStandaloneId(), testMethod);
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given {@link FailureReport}.
     * This section is treated as a standalone one which means that it won't be registered in the section tree and the report
     * won't be merged with any existing one, so it will be added in the list of failure reports no matter what type it is.
     * The given test method and test suite id are stored to identify a parental section.
     *
     * @param failureReport A {@link FailureReport} that should be contained as the payload of this {@link TestMethodFailureSection}
     * @param testMethod    A test method the {@link FailureReport} belongs to.
     * @param testSuiteId   An id of a test suite the {@link FailureReport} belongs to.
     */
    public static TestMethodFailureSection standalone(FailureReport failureReport, Method testMethod,
        String testSuiteId) {
        return new TestMethodFailureSection(failureReport, Standalone.getStandaloneId(), testMethod, testSuiteId);
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
    public Class<FailureReport> getReportTypeClass() {
        return FailureReport.class;
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
