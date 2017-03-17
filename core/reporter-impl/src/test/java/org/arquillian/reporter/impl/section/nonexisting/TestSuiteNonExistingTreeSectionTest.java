package org.arquillian.reporter.impl.section.nonexisting;

import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionStore;
import org.arquillian.reporter.impl.SectionEventManager;
import org.junit.Test;

import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.prepareSectionTreeWithReporterCoreSectionsAndReports;
import static org.arquillian.reporter.impl.utils.SectionGeneratorVerificationHelper.TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteNonExistingTreeSectionTest extends AbstractNonExistingTreeSectionTest {

    @Test
    public void testAddTestSuiteConfigurationToNonExistingSectionInEmptyTreeUsingEventManager() throws Exception {
        ExecutionStore executionStore = new ExecutionStore();

        TestSuiteConfigurationSection testSuiteConfigSection = createTestSuiteConfigSectionInNonExistingSection();

        SectionEventManager.processEvent(testSuiteConfigSection, executionStore);

        TestSuiteReport testSuiteReport =
            verifyNonExistingSectionAddedAndGetReport(executionStore.getSectionTree(), testSuiteConfigSection,
                                                      executionStore.getExecutionReport().getTestSuiteReports(), 1, 3);
        verifyConfigAddedInNonExistingSection(testSuiteReport.getConfiguration());

    }

    @Test
    public void testAddTestSuiteConfigurationToNonExistingSectionInNonEmptyTreeUsingEventManager() throws Exception {
        ExecutionStore executionStore = new ExecutionStore();

        prepareSectionTreeWithReporterCoreSectionsAndReports(executionStore);

        TestSuiteConfigurationSection testSuiteConfigSection = createTestSuiteConfigSectionInNonExistingSection();

        SectionEventManager.processEvent(testSuiteConfigSection, executionStore);

        TestSuiteReport testSuiteReport =
            verifyNonExistingSectionAddedAndGetReport(executionStore.getSectionTree(),
                                                      testSuiteConfigSection,
                                                      executionStore.getExecutionReport().getTestSuiteReports(),
                                                      EXPECTED_NUMBER_OF_SECTIONS + 1,
                                                      TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE + 2);

        verifyConfigAddedInNonExistingSection(testSuiteReport.getConfiguration());
    }

    private TestSuiteConfigurationSection createTestSuiteConfigSectionInNonExistingSection() throws Exception {
        return new TestSuiteConfigurationSection(
            createReportInNonExistingSection(ConfigurationReport.class),
            SECTION_IN_NON_EXISTING_SECTION_NAME,
            NON_EXISTING_SECTION_NAME);
    }
}
