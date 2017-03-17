package org.arquillian.reporter.impl.section;

import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.impl.ExecutionStore;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ExecutionReportAssert.assertThatExecutionReport;
import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.prepareSectionTreeWithReporterCoreSectionsAndReports;
import static org.arquillian.reporter.impl.utils.SectionGeneratorVerificationHelper.PARENT_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.arquillian.reporter.impl.utils.SectionGeneratorVerificationHelper.TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ComplexTreeEventManagerTest {

    @Test
    public void testAddAllReporterCoreSectionsIntoTreeUsingEventManager() {
        ExecutionStore executionStore = new ExecutionStore();

        Map<SectionEvent, List<? extends SectionEvent>> sections =
            prepareSectionTreeWithReporterCoreSectionsAndReports(executionStore);
        // verify
        assertThat(sections).hasSize(PARENT_COUNT_OF_COMPLEX_PREPARED_TREE);

        assertThatExecutionReport(executionStore.getExecutionReport())
            .reportSubtreeConsistOfGeneratedReports();

        assertThatSectionTree(executionStore.getSectionTree())
            .wholeTreeConsistOfCouplesMatching(sections)
            .wholeTreeHasNumberOfTreeNodes(TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE)
            .associatedReport();
    }
}
