package org.arquillian.reporter.impl.section.merge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.impl.ExecutionStore;
import org.arquillian.reporter.impl.SectionEventManager;
import org.arquillian.reporter.impl.SectionTree;
import org.assertj.core.api.SoftAssertions;

import static org.arquillian.reporter.impl.asserts.ExecutionReportAssert.assertThatExecutionReport;
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

    protected int LATEST_SECTION_INDEX = EXPECTED_NUMBER_OF_SECTIONS - 1;

    protected String REPORT_TO_MERGE_NAME = "report-to-merge";

    protected <T extends Report> T getPreparedReportToMergeOnIndex(Class<T> reportTypeClass) throws Exception {
        return prepareReport(reportTypeClass, REPORT_TO_MERGE_NAME, SECTION_MERGE_INDEX + 10, SECTION_MERGE_INDEX + 20);
    }

    protected <T extends Report> T getPreparedReportToMergeLatest(Class<T> reportTypeClass) throws Exception {
        return prepareReport(reportTypeClass, REPORT_TO_MERGE_NAME, LATEST_SECTION_INDEX + 10,
                             LATEST_SECTION_INDEX + 20);
    }

    protected void verifyMergeSectionUsingIdInComplexTreeUsingEventManager(SectionEvent sectionToMerge,
        String reportName) throws Exception {
        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(sectionToMerge, reportName, 19);
    }

    protected void verifyMergeLatestSectionInComplexTreeUsingEventManager(SectionEvent sectionToMerge,
        String idOfLatestSection, String reportName) throws Exception {
        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(sectionToMerge, idOfLatestSection, reportName, 19);
    }

    protected void verifyMergeLatestSectionInComplexTreeUsingEventManager(SectionEvent sectionToMerge,
        String idOfLatestSection, String reportName, int numberOfEntriesAndReports) throws Exception {
        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(sectionToMerge, idOfLatestSection, reportName,
                                                                numberOfEntriesAndReports);
    }

    protected void verifyMergeSectionUsingIdInComplexTreeUsingEventManager(SectionEvent sectionToMerge,
        String reportName, int numberOfEntriesAndReports) throws Exception {
        verifyMergeSectionUsingIdInComplexTreeUsingEventManager(sectionToMerge, null, reportName,
                                                                numberOfEntriesAndReports);
    }

    protected void verifyMergeSectionUsingIdInComplexTreeUsingEventManager(SectionEvent sectionToMerge,
        String idOfLatestSection, String reportName, int numberOfEntriesAndReports) throws Exception {

        ExecutionStore executionStore = new ExecutionStore();

        Map<SectionEvent, List<? extends SectionEvent>> sections =
            prepareSectionTreeWithReporterCoreSectionsAndReports(executionStore);

        SectionEventManager.processEvent(sectionToMerge, executionStore);

        Identifier identifier = null;
        int reportIndex;
        if (idOfLatestSection == null) {
            identifier = getSectionIdentifier(sectionToMerge);
            reportIndex = SECTION_MERGE_INDEX;
        } else {
            identifier = new Identifier(sectionToMerge.getClass(), idOfLatestSection);
            reportIndex = LATEST_SECTION_INDEX;
        }

        Optional<SectionTree> merged =
            getTreeWithIdAndReportNameFromWholeTree(executionStore.getSectionTree(), identifier, reportName);

        assertThat(merged).as("The section-tree-node with identifier: <%s> should be present.", identifier).isPresent();

        ArrayList<Report> mergedReports = new ArrayList<>(Arrays.asList(merged.get().getAssociatedReport()));

        SoftAssertions.assertSoftly(softly -> {
            assertThat(merged).isPresent();
            assertThat(sections).hasSize(PARENT_COUNT_OF_COMPLEX_PREPARED_TREE);

            assertThatExecutionReport(executionStore.getExecutionReport())
                .reportSubtreeConsistOfGeneratedReports(mergedReports);

            assertThatSectionTree(executionStore.getSectionTree())
                .wholeTreeConsistOfCouplesMatching(sections)
                .wholeTreeHasNumberOfTreeNodes(TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE);

            assertThat(mergedReports).isEmpty();

            assertThatReport(merged.get().getAssociatedReport())
                .hasGeneratedSubReportsAndEntries(reportIndex + 1, reportIndex + 20)
                .hasNumberOfSubReportsAndEntries(numberOfEntriesAndReports);
        });
    }
}
