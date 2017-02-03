package org.arquillian.reporter.impl.section.nonexisting;

import org.arquillian.reporter.api.event.TestClassConfigurationSection;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.SectionEventManager;
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

//    @Test
    public void testAddTestClassToNonExistingSectionInNonEmptyTreeUsingEventManager()
        throws InstantiationException, IllegalAccessException {
        ExecutionReport executionReport = new ExecutionReport();

        prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        TestClassConfigurationSection testClassConfigSection = createTestClassConfigSectionInNonExistingSection();

        SectionEventManager.processEvent(testClassConfigSection, executionReport);

        TestSuiteReport testSuiteReport =
            verifyNonExistingSuiteSectionAddedAndGetReport(executionReport.getSectionTree(),
                                                           new TestClassSection(),
                                                           executionReport.getTestSuiteReports(),
                                                           EXPECTED_NUMBER_OF_SECTIONS + 1,
                                                           TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE + 2);
        verifyTestClassAddedInNonExistingSection(testSuiteReport.getTestClassReports());

    }

//    todo
    @Test
    public void testAddTestClassConfigurationToNonExistingSectionInEmptyTreeUsingEventManager()
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

//    todo
//    @Test
    public void testAddTestClassConfigurationToNonExistingSectionInNonEmptyTreeUsingEventManager()
        throws InstantiationException, IllegalAccessException {
        ExecutionReport executionReport = new ExecutionReport();

        prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        TestClassConfigurationSection testClassConfigSection = createTestClassConfigSectionInNonExistingSection();

        SectionEventManager.processEvent(testClassConfigSection, executionReport);

        TestSuiteReport testSuiteReport =
            verifyNonExistingSuiteSectionAddedAndGetReport(executionReport.getSectionTree(),
                                                           new TestClassSection(),
                                                           executionReport.getTestSuiteReports(),
                                                           EXPECTED_NUMBER_OF_SECTIONS + 1,
                                                           TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE + 2);
        verifyTestClassAddedInNonExistingSection(testSuiteReport.getTestClassReports());

    }
}
