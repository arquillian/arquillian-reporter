package org.arquillian.reporter.api.event;

import java.lang.reflect.Method;

import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.utils.ReporterUtils;

/**
 * An implementation of {@link SectionEvent} that represents section for {@link FailureReport}s within a {@link TestMethodReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodFailureSection extends SectionEvent<TestMethodFailureSection, FailureReport, TestMethodSection> {

    private Method testMethod;
    private String testSuiteId;

    /**
     * Creates an instance of {@link TestMethodFailureSection}
     */
    public TestMethodFailureSection() {
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with the given id
     *
     * @param failureId A failure id to be used to identify the {@link TestMethodFailureSection}
     */
    public TestMethodFailureSection(String failureId) {
        super(failureId);
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with an id build from the given failureId and
     * test method id of the method the {@link FailureReport} belongs to.
     * <p>
     * The resulting id is created as: 'declaring.test.class.canonical.Name#methodName#failureId'
     * </p>
     * It also stores the given test method for identifying parental section.
     *
     * @param failureId  A failure id to be used in resulting id
     * @param testMethod A test method the {@link FailureReport} belongs to.
     */
    public TestMethodFailureSection(String failureId, Method testMethod) {
        super(ReporterUtils.getTestMethodId(testMethod), failureId);
        this.testMethod = testMethod;
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with an id build from the given failureId and
     * test method id of the method the {@link FailureReport} belongs to.
     * <p>
     * The resulting id is created as: 'declaring.test.class.canonical.Name#methodName#failureId'
     * </p>
     * It also stores the given test method and test suite for identifying parental section.
     *
     * @param failureId   A failure id to be used in resulting id
     * @param testMethod  A test method the {@link FailureReport} belongs to.
     * @param testSuiteId An id of a test suite the {@link FailureReport} belongs to.
     */
    public TestMethodFailureSection(String failureId, Method testMethod, String testSuiteId) {
        super(ReporterUtils.getTestMethodId(testMethod), failureId);
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
     * Creates an instance of {@link TestMethodFailureSection} with with the given {@link FailureReport}
     * and an id represented by the given failureId
     *
     * @param failureReport A {@link FailureReport} that should be contained as the payload of this {@link TestMethodFailureSection}
     * @param failureId     A failure id to be used in resulting id
     */
    public TestMethodFailureSection(FailureReport failureReport, String failureId) {
        super(failureReport, failureId);
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with with the given {@link FailureReport}
     * and an id build from the given failureId and test method id of the method the {@link FailureReport} belongs to.
     * <p>
     * The resulting id is created as: 'declaring.test.class.canonical.Name#methodName#failureId'
     * </p>
     * It also stores the given test method for identifying parental section.
     *
     * @param failureReport A {@link FailureReport} that should be contained as the payload of this {@link TestMethodFailureSection}
     * @param failureId     A failure id to be used in resulting id
     * @param testMethod    A test method the {@link FailureReport} belongs to.
     */
    public TestMethodFailureSection(FailureReport failureReport, String failureId, Method testMethod) {
        super(failureReport, ReporterUtils.getTestMethodId(testMethod), failureId);
        this.testMethod = testMethod;
    }

    /**
     * Creates an instance of {@link TestMethodFailureSection} with with the given {@link FailureReport}
     * and an id build from the given failureId and test method id of the method the {@link FailureReport} belongs to.
     * <p>
     * The resulting id is created as: 'declaring.test.class.canonical.Name#methodName#failureId'
     * </p>
     * It also stores the given test method and test suite id for identifying parental section.
     *
     * @param failureReport A {@link FailureReport} that should be contained as the payload of this {@link TestMethodFailureSection}
     * @param failureId     A failure id to be used in resulting id
     * @param testMethod    A test method the {@link FailureReport} belongs to.
     * @param testSuiteId   An id of a test suite the {@link FailureReport} belongs to.
     */
    public TestMethodFailureSection(FailureReport failureReport, String failureId, Method testMethod,
        String testSuiteId) {
        super(failureReport, ReporterUtils.getTestMethodId(testMethod), failureId);
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
