package org.arquillian.reporter.impl.section;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.SectionEventManager;
import org.arquillian.reporter.impl.SectionTree;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.prepareReport;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestClassConfigurationSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestClassSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestSuiteSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getSubsectionsOfSomeSection;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteSectionName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.prepareSectionTreeWithReporterCoreSectionsAndReports;
import static org.arquillian.reporter.impl.utils.SectionGeneratorVerificationHelper.PARENT_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.arquillian.reporter.impl.utils.SectionGeneratorVerificationHelper.TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.arquillian.reporter.impl.utils.SectionGeneratorVerificationHelper.verifyAllSectionsAreProcessed;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getSectionIdentifier;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTreeWithIdAndReportNameFromWholeTree;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassSectionTreeEventManagerTest {

    @Test
    public void testAddingTestClassSectionsWithReportsIntoExistingTreeUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(
            feedWithTestClassSections(executionReport, getSubsectionsOfSomeSection(ExecutionSection.class, sections)));

        int parentCount = 1 + EXPECTED_NUMBER_OF_SECTIONS;
        int treeNodesCount = parentCount + EXPECTED_NUMBER_OF_SECTIONS;
        assertThat(sections).hasSize(parentCount);
        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport()
            .wholeExecutionReportTreeConsistOfGeneratedReports(TestSuiteReport.class, TestClassReport.class);
        verifyAllSectionsAreProcessed(sections);
    }

    @Test
    public void testAddTestClassConfigurationSectionsWithReportsToExistingTreeUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(
            feedWithTestClassSections(executionReport, getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(
            feedWithTestClassConfigurationSections(executionReport,
                                                   getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));

        int parentCount = 1 + EXPECTED_NUMBER_OF_SECTIONS * 2;
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        assertThat(sections).hasSize(parentCount);
        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport()
            .wholeExecutionReportTreeConsistOfGeneratedReports(TestSuiteReport.class,
                                                               TestClassReport.class,
                                                               ConfigurationReport.class);
        verifyAllSectionsAreProcessed(sections);
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

}
