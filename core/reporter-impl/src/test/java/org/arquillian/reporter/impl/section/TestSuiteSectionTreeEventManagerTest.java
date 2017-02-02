package org.arquillian.reporter.impl.section;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.SectionEventManager;
import org.arquillian.reporter.impl.SectionTree;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.PARENT_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.feedWithTestSuiteConfigurationSections;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.feedWithTestSuiteSections;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getSectionIdentifier;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getSubsectionsOfSomeSection;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTestSuiteReportName;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTestSuiteSectionName;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTreeWithIdAndReportNameFromWholeTree;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTreeWithIdentifierFromList;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.prepareSectionTreeWithReporterCoreSectionsAndReports;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.verifyAllSectionsAreProcessed;
import static org.arquillian.reporter.impl.utils.Utils.prepareReport;
import static org.arquillian.reporter.impl.utils.Utils.prepareReportWithDefaults;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteSectionTreeEventManagerTest {

    @Test
    public void testAddingTestSuiteSectionsWithReportsIntoExistingTreeUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);

        assertThat(sections).hasSize(1);
        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(1 + EXPECTED_NUMBER_OF_SECTIONS)
            .associatedReport()
            .wholeExecutionReportTreeConsistOfGeneratedReports(TestSuiteReport.class);

        verifyAllSectionsAreProcessed(sections);

    }

    @Test
    public void testAddingTestSuiteConfigurationSectionsWithReportsIntoExistingTreeUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(
            feedWithTestSuiteConfigurationSections(executionReport,
                                                   getSubsectionsOfSomeSection(ExecutionSection.class, sections)));

        int parentCount = 1 + EXPECTED_NUMBER_OF_SECTIONS;
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        assertThat(sections).hasSize(parentCount);
        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport()
            .wholeExecutionReportTreeConsistOfGeneratedReports(TestSuiteReport.class, ConfigurationReport.class);
        verifyAllSectionsAreProcessed(sections);
    }

    @Test
    public void testAddTestSuiteConfigurationToNonExistingSectionInEmptyTreeUsingEventManager()
        throws InstantiationException, IllegalAccessException {
        ExecutionReport executionReport = new ExecutionReport();

        TestSuiteConfigurationSection testSuiteConfigSection = createTestSuiteConfigSectionInNonExistingSection();

        SectionEventManager.processEvent(testSuiteConfigSection, executionReport);

        Identifier suiteIdentifier = getSectionIdentifier(new TestSuiteSection("non-existing-section"));
        assertThatSectionTree(executionReport.getSectionTree())
            .hasSubtreeWithIdentifier(suiteIdentifier)
            .hasNumberOfSubTrees(1)
            .wholeTreeHasNumberOfTreeNodes(3);

        SectionTree suiteTree = (SectionTree) executionReport.getSectionTree().getSubtrees().get(0);
        verifyNonExistingSuitTree(suiteTree, testSuiteConfigSection, executionReport);

    }

    @Test
    public void testAddTestSuiteConfigurationToNonExistingSectionInNonEmptyTreeUsingEventManager()
        throws InstantiationException, IllegalAccessException {
        ExecutionReport executionReport = new ExecutionReport();

        prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        TestSuiteConfigurationSection testSuiteConfigSection = createTestSuiteConfigSectionInNonExistingSection();

        SectionEventManager.processEvent(testSuiteConfigSection, executionReport);

        SectionTree executionTree = executionReport.getSectionTree();
        Identifier suiteIdentifier = getSectionIdentifier(new TestSuiteSection("non-existing-section"));
        assertThatSectionTree(executionTree)
            .hasSubtreeWithIdentifier(suiteIdentifier)
            .hasNumberOfSubTrees(EXPECTED_NUMBER_OF_SECTIONS + 1)
            .wholeTreeHasNumberOfTreeNodes(TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE + 2);

        SectionTree suiteTree =
            (SectionTree) getTreeWithIdentifierFromList(executionTree.getSubtrees(), suiteIdentifier).get();
        verifyNonExistingSuitTree(suiteTree, testSuiteConfigSection, executionReport);
    }

    @Test
    public void testMergeTestSuiteSectionInComplexTreeUsingEventManager()
        throws InstantiationException, IllegalAccessException {
        ExecutionReport executionReport = new ExecutionReport();

        Map<SectionEvent, List<? extends SectionEvent>> sections =
            prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        int sectionIndex =
            EXPECTED_NUMBER_OF_SECTIONS > 1 ? EXPECTED_NUMBER_OF_SECTIONS - 1 : EXPECTED_NUMBER_OF_SECTIONS;
        TestSuiteReport testSuiteReport =
            prepareReport(TestSuiteReport.class, "report-to-merge", sectionIndex + 10, sectionIndex + 20);
        TestSuiteSection toMerge = new TestSuiteSection(testSuiteReport, getTestSuiteSectionName(sectionIndex));
        SectionEventManager.processEvent(toMerge, executionReport);

        Optional<SectionTree> merged =
            getTreeWithIdAndReportNameFromWholeTree(executionReport.getSectionTree(),
                                                    getSectionIdentifier(toMerge),
                                                    getTestSuiteReportName(sectionIndex));

        assertThat(merged).isPresent();

        assertThat(sections).hasSize(PARENT_COUNT_OF_COMPLEX_PREPARED_TREE);
        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE)
            .associatedReport()
            .wholeExecutionReportTreeConsistOfAllGeneratedReports(merged.get().getAssociatedReport());

        assertThatReport(merged.get().getAssociatedReport())
            .hasGeneratedSubreportsAndEntries(sectionIndex + 1, sectionIndex + 20)
            .hasNumberOfSubreportsAndEntries(19);
    }

    private void verifyNonExistingSuitTree(
        SectionTree suiteTree,
        TestSuiteConfigurationSection testSuiteConfigSection,
        ExecutionReport executionReport) {

        assertThatSectionTree(suiteTree)
            .hasNumberOfSubTrees(1)
            .hasSubtreeMatchingSection(testSuiteConfigSection)
            .associatedReport()
            .hasName("non-existing-section");

        TestSuiteReport testSuiteReport = executionReport
            .getTestSuiteReports()
            .stream()
            .filter(section -> section.equals(suiteTree.getAssociatedReport()))
            .findFirst()
            .get();

        ConfigurationReport configuration = testSuiteReport.getConfiguration();
        assertThatReport(configuration)
            .hasNumberOfSubreports(1);

        assertThatReport(configuration.getSubReports().get(0))
            .hasName("config-in-non-existing-section")
            .hasGeneratedSubreportsAndEntriesWithDefaults();
    }

    private ConfigurationReport createReportInNonExistingSection()
        throws InstantiationException, IllegalAccessException {
        return prepareReportWithDefaults(ConfigurationReport.class, "config-in-non-existing-section");
    }

    private TestSuiteConfigurationSection createTestSuiteConfigSectionInNonExistingSection()
        throws IllegalAccessException, InstantiationException {
        return new TestSuiteConfigurationSection(
            createReportInNonExistingSection(),
            "non-existing-section",
            "config-in-non-existing-section");
    }

}
