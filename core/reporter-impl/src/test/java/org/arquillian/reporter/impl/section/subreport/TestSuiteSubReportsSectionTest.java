package org.arquillian.reporter.impl.section.subreport;

import java.util.List;
import java.util.Map;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestSuiteConfigurationSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
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
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestSuiteConfigurationSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestSuiteSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getSubsectionsOfSomeSection;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteConfigSectionName;
import static org.assertj.core.api.Assertions.assertThat;

public class TestSuiteSubReportsSectionTest {

    @Test
    public void testAddingConfigurationSubReportToTestSuiteConfigurationSectionsWithReportsUsingEventManager()
        throws Exception {
        ExecutionStore executionStore = new ExecutionStore();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionStore);
        sections.putAll(feedWithTestSuiteConfigurationSections(executionStore,
            getSubsectionsOfSomeSection(ExecutionSection.class, sections)));

        ConfigurationReport configurationReport =
            ReportGeneratorUtils.prepareReport(ConfigurationReport.class, "Configuration Sub Report", 0, 10);

        TestSuiteConfigurationSection testSuiteConfigurationSection =
            new TestSuiteConfigurationSection(configurationReport,
                getTestSuiteConfigSectionName(EXPECTED_NUMBER_OF_SECTIONS - 1));

        testSuiteConfigurationSection.setContainsSubReport(true);

        SectionEventManager.processEvent(testSuiteConfigurationSection, executionStore);

        int parentCount = 1 + EXPECTED_NUMBER_OF_SECTIONS;
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        assertThat(sections).hasSize(parentCount);

        assertThatExecutionReport(executionStore.getExecutionReport())
            .reportSubtreeConsistOfGeneratedReports(TestSuiteReport.class);

        List<TestSuiteReport> testSuiteReports = executionStore.getExecutionReport().getTestSuiteReports();
        TestSuiteReport testSuiteReport = testSuiteReports.get(testSuiteReports.size() - 1);
        assertThatReport(testSuiteReport.getConfiguration()).hasNumberOfSubReports(4);

        Object configurationSubReport = testSuiteReport.getConfiguration().getSubReports().get(3).getSubReports().get(9);
        assertThat(configurationSubReport).isEqualTo(configurationReport);

        assertThatSectionTree(executionStore.getSectionTree())
            .wholeTreeConsistOfCouplesMatching(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport();
    }
}
