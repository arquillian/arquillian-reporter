package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassSection extends SectionEvent<TestClassSection, TestClassReport, TestSuiteSection> {

    private String testSuiteId;
    private Class<?> testClass;

    public TestClassSection(Class<?> testClass) {
        super(testClass.getCanonicalName());
        this.testClass = testClass;
    }

    public TestClassSection(Class<?> testClass, String testSuiteId) {
        super(testClass.getCanonicalName());
        this.testSuiteId = testSuiteId;
        this.testClass = testClass;
    }

    public TestClassSection(TestClassReport testClassReport, Class<?> testClass, String testSuiteId) {
        super(testClassReport, testClass.getCanonicalName());
        this.testSuiteId = testSuiteId;
        this.testClass = testClass;
    }

    @Override
    public TestSuiteSection getParentSectionThisSectionBelongsTo() {
        return new TestSuiteSection(testSuiteId);
    }

//    @Override
//    public Identifier<TestClassSection> identifyYourself() {
//        if (testClass != null) {
//         return new Identifier<>(TestClassSection.class, testClass.getCanonicalName());
//        }
//        return null;
//    }
}
