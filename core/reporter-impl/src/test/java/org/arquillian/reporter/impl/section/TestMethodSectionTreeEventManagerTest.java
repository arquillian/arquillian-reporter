package org.arquillian.reporter.impl.section;

import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.feedWithTestClassSections;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.feedWithTestMethodConfigurationSections;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.feedWithTestMethodFailureSections;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.feedWithTestMethodSections;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.feedWithTestSuiteSections;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getSubsectionsOfSomeSection;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodSectionTreeEventManagerTest {

    @Test
    public void testAddTestMethodSectionsWithReportsToExistingTreeUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(feedWithTestClassSections(executionReport, getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(feedWithTestMethodSections(executionReport, getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));

        int parentCount = (1 + EXPECTED_NUMBER_OF_SECTIONS * 2);
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        assertThat(sections).hasSize(parentCount);

        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport()
            .wholeExecutionReportTreeConsistOfGeneratedReports(TestSuiteReport.class,
                                                               TestClassReport.class,
                                                               TestMethodReport.class);

        SectionTreeEventManagerUtils.verifyAllSectionsAreProcessed(sections);
    }

    @Test
    public void testAddTestMethodConfigurationSectionsWithReportsToExistingTreeUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(feedWithTestClassSections(executionReport, getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(feedWithTestMethodSections(executionReport, getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));
        sections.putAll(feedWithTestMethodConfigurationSections(executionReport,
                                                                getSubsectionsOfSomeSection(TestClassSection.class, sections)));

        int parentCount = (int) (1 + (EXPECTED_NUMBER_OF_SECTIONS * 2) + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 3));
        assertThat(sections).hasSize(parentCount);
        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport()
            .wholeExecutionReportTreeConsistOfGeneratedReports(TestSuiteReport.class,
                                                               TestClassReport.class,
                                                               TestMethodReport.class,
                                                               ConfigurationReport.class);
        SectionTreeEventManagerUtils.verifyAllSectionsAreProcessed(sections);
    }

    @Test
    public void testAddTestMethodFailureSectionsWithReportsToExistingTreeUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(feedWithTestClassSections(executionReport, getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(feedWithTestMethodSections(executionReport, getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));
        sections.putAll(
            feedWithTestMethodFailureSections(executionReport, getSubsectionsOfSomeSection(TestClassSection.class, sections)));

        int parentCount = (int) (1 + (EXPECTED_NUMBER_OF_SECTIONS * 2) + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 3));
        assertThat(sections).hasSize(parentCount);
        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport()
            .wholeExecutionReportTreeConsistOfGeneratedReports(TestSuiteReport.class,
                                                               TestClassReport.class,
                                                               TestMethodReport.class,
                                                               FailureReport.class);
        SectionTreeEventManagerUtils.verifyAllSectionsAreProcessed(sections);
    }
}
