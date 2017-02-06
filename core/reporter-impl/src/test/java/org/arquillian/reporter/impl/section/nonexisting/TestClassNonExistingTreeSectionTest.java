package org.arquillian.reporter.impl.section.nonexisting;

import java.util.List;

import org.arquillian.reporter.api.event.TestClassConfigurationSection;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.SectionEventManager;
import org.arquillian.reporter.impl.SectionTree;
import org.arquillian.reporter.impl.utils.dummy.SecondDummyTestClass;
import org.junit.Test;

import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.prepareSectionTreeWithReporterCoreSectionsAndReports;
import static org.arquillian.reporter.impl.utils.SectionGeneratorVerificationHelper.TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassNonExistingTreeSectionTest extends AbstractNonExistingTreeSectionTest {

    @Test
    public void testAddClassToNonExistingSectionInEmptyTreeUsingEventManager()
        throws InstantiationException, IllegalAccessException {
        ExecutionReport executionReport = new ExecutionReport();

        TestClassSection testClassSection = createTestClassSectionInNonExistingSection();

        SectionEventManager.processEvent(testClassSection, executionReport);

        TestSuiteReport testSuiteReport =
            verifyNonExistingSuiteSectionAddedAndGetReport(executionReport.getSectionTree(),
                                                           testClassSection,
                                                           executionReport.getTestSuiteReports(), 1, 3);
        verifyTestClassAddedInNonExistingSection(testSuiteReport.getTestClassReports());

    }

    @Test
    public void testAddTestClassToNonExistingSectionInNonEmptyTreeUsingEventManager()
        throws InstantiationException, IllegalAccessException {
        ExecutionReport executionReport = new ExecutionReport();

        prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        TestClassSection testClassSection = createTestClassSectionInNonExistingSection();

        SectionEventManager.processEvent(testClassSection, executionReport);

        TestSuiteReport testSuiteReport =
            verifyNonExistingSuiteSectionAddedAndGetReport(executionReport.getSectionTree(),
                                                           testClassSection,
                                                           executionReport.getTestSuiteReports(),
                                                           EXPECTED_NUMBER_OF_SECTIONS + 1,
                                                           TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE + 2);
        verifyTestClassAddedInNonExistingSection(testSuiteReport.getTestClassReports());

    }

    @Test
    public void testAddTestClassConfigurationToNonExistingSectionInEmptyTreeUsingEventManager()
        throws InstantiationException, IllegalAccessException {
        ExecutionReport executionReport = new ExecutionReport();

        TestClassConfigurationSection testClassConfigSection = createTestClassConfigSectionInNonExistingSection();

        SectionEventManager.processEvent(testClassConfigSection, executionReport);

        SectionTree<TestSuiteSection, TestSuiteReport> suiteTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(executionReport.getSectionTree(),
                                                         new TestClassSection(SecondDummyTestClass.class),
                                                         1, 4);

        List<TestClassReport> testClassReports = suiteTree.getAssociatedReport().getTestClassReports();
        TestClassReport classReport = verifyNonExistingSuiteSectionAddedAndGetReport(suiteTree,
                                                                                     testClassConfigSection,
                                                                                     testClassReports,
                                                                                     1, 3);

        verifyConfigAddedInNonExistingSection(classReport.getConfiguration());

    }

    @Test
    public void testAddTestClassConfigurationToNonExistingSectionInNonEmptyTreeUsingEventManager()
        throws InstantiationException, IllegalAccessException {
        ExecutionReport executionReport = new ExecutionReport();

        prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        TestClassConfigurationSection testClassConfigSection = createTestClassConfigSectionInNonExistingSection();

        SectionEventManager.processEvent(testClassConfigSection, executionReport);

        SectionTree<TestSuiteSection, TestSuiteReport> suiteTree =
            verifyNonExistingSuiteSectionAddedAndGetTree(executionReport.getSectionTree(),
                                                         new TestClassSection(SecondDummyTestClass.class),
                                                         EXPECTED_NUMBER_OF_SECTIONS + 1,
                                                         TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE + 3);

        List<TestClassReport> testClassReports = suiteTree.getAssociatedReport().getTestClassReports();
        TestClassReport classReport = verifyNonExistingSuiteSectionAddedAndGetReport(suiteTree,
                                                                                     testClassConfigSection,
                                                                                     testClassReports,
                                                                                     1, 3);

        verifyConfigAddedInNonExistingSection(classReport.getConfiguration());

    }
}
