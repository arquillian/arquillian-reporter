package org.arquillian.reporter.impl.section;

import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.impl.ExecutionReport;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.PARENT_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.prepareSectionTreeWithReporterCoreSectionsAndReports;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.verifyAllSectionsAreProcessed;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ComplexTreeEventManagerTest {

    @Test
    public void testAddAllReporterCoreSectionsIntoTreeUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();

        Map<SectionEvent, List<? extends SectionEvent>> sections =
            prepareSectionTreeWithReporterCoreSectionsAndReports(executionReport);
        // verify

        assertThat(sections).hasSize(PARENT_COUNT_OF_COMPLEX_PREPARED_TREE);
        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(TREE_NODES_COUNT_OF_COMPLEX_PREPARED_TREE);
        verifyAllSectionsAreProcessed(sections);
    }
}
