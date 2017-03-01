package org.arquillian.reporter.impl.section.nonexisting;

import java.lang.reflect.Method;
import java.util.List;

import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestMethodConfigurationSection;
import org.arquillian.reporter.api.event.TestMethodFailureSection;
import org.arquillian.reporter.api.event.TestMethodSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.SectionEventManager;
import org.arquillian.reporter.impl.SectionTree;
import org.arquillian.reporter.impl.utils.dummy.SecondDummyTestClass;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.asserts.TestMethodReportAssert.assertThatTestMethodReport;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.prepareSectionTreeWithReporterCoreSectionsAndReports;
import static org.arquillian.reporter.impl.utils.SectionGeneratorVerificationHelper.TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodNonExistingTreeSectionTest extends AbstractNonExistingTreeSectionTest {

    @Test
    public void testAddMethodToNonExistingSectionInEmptyTreeUsingEventManager() throws Exception {
        ExecutionReport executionReport = new ExecutionReport();

        TestMethodSection methodSection = createTestMethodSectionInNonExistingSection();

        SectionEventManager.processEvent(methodSection, executionReport);

        SectionTree<TestSuiteSection, TestSuiteReport> suiteTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(executionReport.getSectionTree(),
                                                         new TestClassSection(SecondDummyTestClass.class),
                                                         1, 4);

        List<TestClassReport> testClassReports = suiteTree.getAssociatedReport().getTestClassReports();
        TestClassReport classReport = verifyNonExistingSectionAddedAndGetReport(suiteTree,
                                                                                methodSection,
                                                                                testClassReports,
                                                                                1, 3);

        verifyTestMethodReportAddedInNonExistingSection(classReport.getTestMethodReports());

    }

    @Test
    public void testAddTestMethodToNonExistingSectionInNonEmptyTreeUsingEventManager() throws Exception {
        ExecutionReport executionReport = new ExecutionReport();

        prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        TestMethodSection methodSection = createTestMethodSectionInNonExistingSection();
        SectionEventManager.processEvent(methodSection, executionReport);

        SectionTree<TestSuiteSection, TestSuiteReport> suiteTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(executionReport.getSectionTree(),
                                                         new TestClassSection(SecondDummyTestClass.class),
                                                         EXPECTED_NUMBER_OF_SECTIONS + 1,
                                                         TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE + 3);

        List<TestClassReport> testClassReports = suiteTree.getAssociatedReport().getTestClassReports();
        TestClassReport classReport = verifyNonExistingSectionAddedAndGetReport(suiteTree,
                                                                                methodSection,
                                                                                testClassReports,
                                                                                1, 3);

        verifyTestMethodReportAddedInNonExistingSection(classReport.getTestMethodReports());
    }

    @Test
    public void testAddTestMethodConfigurationToNonExistingSectionInEmptyTreeUsingEventManager() throws Exception {
        ExecutionReport executionReport = new ExecutionReport();

        TestMethodConfigurationSection methodConfigSection = createTestMethodConfigSectionInNonExistingSection();

        SectionEventManager.processEvent(methodConfigSection, executionReport);

        SectionTree<TestSuiteSection, TestSuiteReport> suiteTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(executionReport.getSectionTree(),
                                                         new TestClassSection(SecondDummyTestClass.class),
                                                         1, 5);

        SectionTree<TestClassSection, TestClassReport> classTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(suiteTree,
                                                         new TestMethodSection(getDummyMethodInSecond()),
                                                         1, 4);

        List<TestMethodReport> testMethodReports = classTree.getAssociatedReport().getTestMethodReports();
        TestMethodReport methodReport = verifyNonExistingSectionAddedAndGetReport(classTree,
                                                                                  methodConfigSection,
                                                                                  testMethodReports,
                                                                                  1, 3);

        verifyConfigAddedInNonExistingSection(methodReport.getConfiguration());
    }

    @Test
    public void testAddTestMethodConfigurationToNonExistingSectionInNonEmptyTreeUsingEventManager() throws Exception {
        ExecutionReport executionReport = new ExecutionReport();
        prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        TestMethodConfigurationSection methodConfigSection = createTestMethodConfigSectionInNonExistingSection();
        SectionEventManager.processEvent(methodConfigSection, executionReport);

        SectionTree<TestSuiteSection, TestSuiteReport> suiteTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(executionReport.getSectionTree(),
                                                         new TestClassSection(SecondDummyTestClass.class),
                                                         EXPECTED_NUMBER_OF_SECTIONS + 1,
                                                         TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE + 4);

        SectionTree<TestClassSection, TestClassReport> classTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(suiteTree,
                                                         new TestMethodSection(getDummyMethodInSecond()),
                                                         1, 4);

        List<TestMethodReport> testMethodReports = classTree.getAssociatedReport().getTestMethodReports();
        TestMethodReport methodReport = verifyNonExistingSectionAddedAndGetReport(classTree,
                                                                                  methodConfigSection,
                                                                                  testMethodReports,
                                                                                  1, 3);

        verifyConfigAddedInNonExistingSection(methodReport.getConfiguration());
    }

    @Test
    public void testAddTestMethodFailureToNonExistingSectionInEmptyTreeUsingEventManager() throws Exception {
        ExecutionReport executionReport = new ExecutionReport();

        TestMethodFailureSection methodFailureSection = createTestMethodFailureSectionInNonExistingSection();

        SectionEventManager.processEvent(methodFailureSection, executionReport);

        SectionTree<TestSuiteSection, TestSuiteReport> suiteTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(executionReport.getSectionTree(),
                                                         new TestClassSection(SecondDummyTestClass.class),
                                                         1, 5);

        SectionTree<TestClassSection, TestClassReport> classTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(suiteTree,
                                                         new TestMethodSection(getDummyMethodInSecond()),
                                                         1, 4);

        List<TestMethodReport> testMethodReports = classTree.getAssociatedReport().getTestMethodReports();
        TestMethodReport methodReport = verifyNonExistingSectionAddedAndGetReport(classTree,
                                                                                  methodFailureSection,
                                                                                  testMethodReports,
                                                                                  1, 3);

        verifyFailureAddedInNonExistingSection(methodReport.getFailureReport());
    }

    @Test
    public void testAddTestMethodFailureToNonExistingSectionInNonEmptyTreeUsingEventManager() throws Exception {
        ExecutionReport executionReport = new ExecutionReport();
        prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        TestMethodFailureSection methodFailureSection = createTestMethodFailureSectionInNonExistingSection();
        SectionEventManager.processEvent(methodFailureSection, executionReport);

        SectionTree<TestSuiteSection, TestSuiteReport> suiteTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(executionReport.getSectionTree(),
                                                         new TestClassSection(SecondDummyTestClass.class),
                                                         EXPECTED_NUMBER_OF_SECTIONS + 1,
                                                         TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE + 4);

        SectionTree<TestClassSection, TestClassReport> classTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(suiteTree,
                                                         new TestMethodSection(getDummyMethodInSecond()),
                                                         1, 4);

        List<TestMethodReport> testMethodReports = classTree.getAssociatedReport().getTestMethodReports();
        TestMethodReport methodReport = verifyNonExistingSectionAddedAndGetReport(classTree,
                                                                                  methodFailureSection,
                                                                                  testMethodReports,
                                                                                  1, 3);

        verifyFailureAddedInNonExistingSection(methodReport.getFailureReport());
    }

    private TestMethodSection createTestMethodSectionInNonExistingSection() throws Exception {

        TestMethodSection methodSection = new TestMethodSection(
            createReportInNonExistingSection(TestMethodReport.class),
            getDummyMethodInSecond());
        methodSection.setTestSuiteId(NON_EXISTING_SECTION_NAME);
        return methodSection;
    }

    private Method getDummyMethodInSecond() {
        return SecondDummyTestClass.class.getDeclaredMethods()[0];
    }

    private TestMethodConfigurationSection createTestMethodConfigSectionInNonExistingSection() throws Exception {

        TestMethodConfigurationSection testMethodConfigurationSection = new TestMethodConfigurationSection(
            createReportInNonExistingSection(ConfigurationReport.class),
            SECTION_IN_NON_EXISTING_SECTION_NAME,
            getDummyMethodInSecond());
        testMethodConfigurationSection.setTestSuiteId(NON_EXISTING_SECTION_NAME);

        return testMethodConfigurationSection;
    }

    private TestMethodFailureSection createTestMethodFailureSectionInNonExistingSection() throws Exception {

        TestMethodFailureSection testMethodConfigurationSection = new TestMethodFailureSection(
            createReportInNonExistingSection(FailureReport.class),
            SECTION_IN_NON_EXISTING_SECTION_NAME,
            getDummyMethodInSecond());
        testMethodConfigurationSection.setTestSuiteId(NON_EXISTING_SECTION_NAME);

        return testMethodConfigurationSection;
    }

    private void verifyTestMethodReportAddedInNonExistingSection(List<TestMethodReport> methodReports) {
        assertThat(methodReports).hasSize(1);
        TestMethodReport testMethodReport = methodReports.get(0);

        assertThatTestMethodReport(testMethodReport)
            .hasName(REPORT_NAME_IN_NON_EXISTING_SECTION)
            .hasConfigWithNumberOfSubreportsAndEntries(0)
            .hasFailureWithNumberOfSubreportsAndEntries(0)
            .hasGeneratedSubReportsAndEntriesWithDefaults();
    }

    private void verifyFailureAddedInNonExistingSection(FailureReport failureReport) {

        assertThatReport(failureReport)
            .hasNumberOfSubReports(1);

        assertThatReport(failureReport.getSubReports().get(0))
            .hasName(REPORT_NAME_IN_NON_EXISTING_SECTION)
            .hasGeneratedSubReportsAndEntriesWithDefaults();
    }
}
