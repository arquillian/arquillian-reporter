package org.arquillian.reporter.impl.section.subreport;

import java.util.List;
import java.util.Map;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassConfigurationSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.ExecutionStore;
import org.arquillian.reporter.impl.SectionEventManager;
import org.arquillian.reporter.impl.utils.ReportGeneratorUtils;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ExecutionReportAssert.assertThatExecutionReport;
import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestClassConfigurationSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestClassSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestSuiteSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getSubsectionsOfSomeSection;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestClassConfigSectionName;
import static org.assertj.core.api.Assertions.assertThat;

public class TestClassSubReportSectionTest {

    @Test
    public void testAddingConfigurationSubReportToTestClassConfigurationSectionsWithReportsUsingEventManager()
        throws Exception {
        ExecutionStore executionStore = new ExecutionStore();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionStore);
        sections.putAll(
            feedWithTestClassSections(executionStore, getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(
            feedWithTestClassConfigurationSections(executionStore,
                getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));

        ConfigurationReport configurationReport =
            ReportGeneratorUtils.prepareReport(ConfigurationReport.class, "Configuration Sub Report", 0, 10);

        TestClassConfigurationSection testClassConfigurationSection =
            new TestClassConfigurationSection(configurationReport,
                getTestClassConfigSectionName(EXPECTED_NUMBER_OF_SECTIONS - 1));
        testClassConfigurationSection.setContainsSubReport(true);

        SectionEventManager.processEvent(testClassConfigurationSection, executionStore);

        int parentCount = 1 + EXPECTED_NUMBER_OF_SECTIONS * 2;
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        assertThat(sections).hasSize(parentCount);

        assertThatExecutionReport(executionStore.getExecutionReport())
            .reportSubtreeConsistOfGeneratedReports(TestSuiteReport.class, TestClassReport.class);

        TestClassReport testClassReport = getTestClassReport(executionStore);
        assertThatReport(testClassReport.getConfiguration()).hasNumberOfSubReports(4);

        Object configurationSubReport = testClassReport.getConfiguration().getSubReports().get(3).getSubReports().get(9);
        assertThat(configurationSubReport).isEqualTo(configurationReport);

        assertThatSectionTree(executionStore.getSectionTree())
            .wholeTreeConsistOfCouplesMatching(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport();
    }

    private TestClassReport getTestClassReport(ExecutionStore executionStore) {
        List<TestSuiteReport> testSuiteReports = executionStore.getExecutionReport().getTestSuiteReports();
        TestSuiteReport testSuiteReport = testSuiteReports.get(testSuiteReports.size() - 1);
        List<TestClassReport> testClassReports = testSuiteReport.getTestClassReports();
        TestClassReport testClassReport = testClassReports.get(testClassReports.size() - 1);
        return testClassReport;
    }
}
