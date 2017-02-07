package org.arquillian.reporter.impl.section.merge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.SectionEventManager;
import org.arquillian.reporter.impl.SectionTree;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.prepareReport;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.prepareSectionTreeWithReporterCoreSectionsAndReports;
import static org.arquillian.reporter.impl.utils.SectionGeneratorVerificationHelper.PARENT_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.arquillian.reporter.impl.utils.SectionGeneratorVerificationHelper.TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getSectionIdentifier;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getTreeWithIdAndReportNameFromWholeTree;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class AbstractMergeTest {

    protected int SECTION_MERGE_INDEX =
        EXPECTED_NUMBER_OF_SECTIONS > 1 ? EXPECTED_NUMBER_OF_SECTIONS - 2 : EXPECTED_NUMBER_OF_SECTIONS - 1;

    protected String REPORT_TO_MERGE_NAME = "report-to-merge";

    protected <T extends Report> T getPreparedReportToMerge(Class<T> reportTypeClass) throws Exception {
        return prepareReport(reportTypeClass, REPORT_TO_MERGE_NAME, SECTION_MERGE_INDEX + 10, SECTION_MERGE_INDEX + 20);
    }

    protected void verifyMergeSectionUsingIdInComplexTreeUsingEventManager(SectionEvent sectionToMerge,
        String reportName) throws Exception {
        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(sectionToMerge, reportName, 19);
    }

    protected void verifyMergeSectionUsingIdInComplexTreeUsingEventManager(SectionEvent sectionToMerge,
        String reportName, int numberOfEntriesAndReports) throws Exception {

        ExecutionReport executionReport = new ExecutionReport();

        Map<SectionEvent, List<? extends SectionEvent>> sections =
            prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);

        SectionEventManager.processEvent(sectionToMerge, executionReport);

        Optional<SectionTree> merged =
            getTreeWithIdAndReportNameFromWholeTree(executionReport.getSectionTree(),
                                                    getSectionIdentifier(sectionToMerge),
                                                    reportName);

        ArrayList<Report> mergedReports = new ArrayList<>(Arrays.asList(merged.get().getAssociatedReport()));

        assertThat(merged).isPresent();
        assertThat(sections).hasSize(PARENT_COUNT_OF_COMPLEX_PREPARED_TREE);
        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE)
            .associatedReport()
            .wholeExecutionReportTreeConsistOfAllGeneratedReports(mergedReports);

        assertThat(mergedReports).isEmpty();

        assertThatReport(merged.get().getAssociatedReport())
            .hasGeneratedSubreportsAndEntries(SECTION_MERGE_INDEX + 1, SECTION_MERGE_INDEX + 20)
            .hasNumberOfSubreportsAndEntries(numberOfEntriesAndReports);
    }
}
