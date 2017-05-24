package org.arquillian.reporter.impl.section.subreport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestClassSection;
import org.arquillian.reporter.api.event.TestMethodConfigurationSection;
import org.arquillian.reporter.api.event.TestMethodFailureSection;
import org.arquillian.reporter.api.event.TestMethodSection;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.ExecutionStore;
import org.arquillian.reporter.impl.SectionEventManager;
import org.arquillian.reporter.impl.utils.ReportGeneratorUtils;
import org.arquillian.reporter.impl.utils.dummy.SectionUnderTestMethodConfigSection;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ExecutionReportAssert.assertThatExecutionReport;
import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.arquillian.reporter.impl.utils.ReportGeneratorUtils.prepareReport;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestClassSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestMethodConfigurationSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestMethodFailureSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestMethodSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.feedWithTestSuiteSections;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getSubsectionsOfSomeSection;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodConfigSectionName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestMethodFailureSectionName;
import static org.assertj.core.api.Assertions.assertThat;

public class TestMethodSubReportSectionTest {

    @Test
    public void testAddingConfigurationSubReportToTestMethodConfigurationSectionsWithReportsUsingEventManager()
        throws Exception {
        ExecutionStore executionStore = new ExecutionStore();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionStore);
        sections.putAll(feedWithTestClassSections(executionStore,
            getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(feedWithTestMethodSections(executionStore,
            getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));
        sections.putAll(feedWithTestMethodConfigurationSections(executionStore,
            getSubsectionsOfSomeSection(TestClassSection.class,
                sections)));

        ConfigurationReport configurationReport =
            ReportGeneratorUtils.prepareReport(ConfigurationReport.class, "Configuration Sub Report", 0, 10);

        TestMethodConfigurationSection testMethodConfigurationSection =
            new TestMethodConfigurationSection(configurationReport,
                getTestMethodConfigSectionName(EXPECTED_NUMBER_OF_SECTIONS - 1));
        testMethodConfigurationSection.setContainsSubReport(true);

        SectionEventManager.processEvent(testMethodConfigurationSection, executionStore);

        int parentCount = (int) (1 + (EXPECTED_NUMBER_OF_SECTIONS * 2) + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 3));
        assertThat(sections).hasSize(parentCount);

        assertThatExecutionReport(executionStore.getExecutionReport())
            .reportSubtreeConsistOfGeneratedReports(TestSuiteReport.class, TestClassReport.class,
                TestMethodReport.class);

        TestMethodReport testMethodReport = getTestMethodReport(executionStore);
        assertThatReport(testMethodReport.getConfiguration()).hasNumberOfSubReports(4);

        Object configurationSubReport = testMethodReport.getConfiguration().getSubReports().get(3).getSubReports().get(9);
        assertThat(configurationSubReport).isEqualTo(configurationReport);

        assertThatSectionTree(executionStore.getSectionTree())
            .wholeTreeConsistOfCouplesMatching(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport();
    }

    @Test
    public void testAddingBasicSubReportToSectionUnderTestMethodConfigSectionWithReportsUsingEventManager()
        throws Exception {
        ExecutionStore executionStore = new ExecutionStore();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionStore);
        sections.putAll(feedWithTestClassSections(executionStore,
            getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(feedWithTestMethodSections(executionStore,
            getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));
        sections.putAll(feedWithTestMethodConfigurationSections(executionStore,
            getSubsectionsOfSomeSection(TestClassSection.class,
                sections)));
        sections.putAll(feedWithSectionUnderTestMethodConfigSections(executionStore,
            getSubsectionsOfSomeSection(TestMethodSection.class,
                sections)));

        BasicReport basicConfigurationReport =
            ReportGeneratorUtils.prepareReport(BasicReport.class, "Basic Configuration Sub Report", 0, 10);

        SectionUnderTestMethodConfigSection sectionUnderTestMethodConfigSection =
            new SectionUnderTestMethodConfigSection(basicConfigurationReport);
        sectionUnderTestMethodConfigSection.setContainsSubReport(true);

        SectionEventManager.processEvent(sectionUnderTestMethodConfigSection, executionStore);

        int parentCount = (int) (1 + (EXPECTED_NUMBER_OF_SECTIONS * 2) + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2)
            + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 3));
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 3));
        assertThat(sections).hasSize(parentCount);

        assertThatExecutionReport(executionStore.getExecutionReport())
            .reportSubtreeConsistOfGeneratedReports(TestSuiteReport.class, TestClassReport.class,
                TestMethodReport.class);

        TestMethodReport testMethodReport = getTestMethodReport(executionStore);
        assertThatReport(testMethodReport.getConfiguration()).hasNumberOfSubReports(4);

        Report configurationSubReport =
            (Report) testMethodReport.getConfiguration().getSubReports().get(3).getSubReports().get(18);
        Object basicSubReport = configurationSubReport.getSubReports().get(9);

        assertThat(basicSubReport).isEqualTo(basicConfigurationReport);

        assertThatSectionTree(executionStore.getSectionTree())
            .wholeTreeConsistOfCouplesMatching(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport();
    }

    public Map<SectionEvent, List<? extends SectionEvent>> feedWithSectionUnderTestMethodConfigSections(
        ExecutionStore executionStore, List<SectionEvent> parentTestMethodConfigSections) {

        Map<SectionEvent, List<? extends SectionEvent>> sections = new HashMap<>();

        parentTestMethodConfigSections.stream().forEach(tmc -> {
            TestMethodConfigurationSection testMethodConfigSection = (TestMethodConfigurationSection) tmc;
            SectionUnderTestMethodConfigSection dummySection = null;
            try {
                dummySection = new SectionUnderTestMethodConfigSection(
                    prepareReport(
                        BasicReport.class,
                        "my-dummy-basic-report",
                        1,
                        10),
                    "my-dummy-section",
                    testMethodConfigSection);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SectionEventManager.processEvent(dummySection, executionStore);
            sections.put(testMethodConfigSection, Arrays.asList(dummySection));
        });

        return sections;
    }

    @Test
    public void testAddingFailureSubReportToTestMethodFailureSectionsWithReportsUsingEventManager() throws Exception {
        ExecutionStore executionStore = new ExecutionStore();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionStore);
        sections.putAll(feedWithTestClassSections(executionStore,
            getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(feedWithTestMethodSections(executionStore,
            getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));
        sections.putAll(
            feedWithTestMethodFailureSections(executionStore,
                getSubsectionsOfSomeSection(TestClassSection.class, sections)));

        FailureReport failureReport =
            ReportGeneratorUtils.prepareReport(FailureReport.class, "Failure Sub Report", 0, 10);

        TestMethodFailureSection testMethodFailureSection =
            new TestMethodFailureSection(failureReport, getTestMethodFailureSectionName(EXPECTED_NUMBER_OF_SECTIONS - 1));
        testMethodFailureSection.setContainsSubReport(true);

        SectionEventManager.processEvent(testMethodFailureSection, executionStore);

        int parentCount = (int) (1 + (EXPECTED_NUMBER_OF_SECTIONS * 2) + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 3));
        assertThat(sections).hasSize(parentCount);

        assertThatExecutionReport(executionStore.getExecutionReport())
            .reportSubtreeConsistOfGeneratedReports(TestSuiteReport.class, TestClassReport.class,
                TestMethodReport.class);

        TestMethodReport testMethodReport = getTestMethodReport(executionStore);
        assertThatReport(testMethodReport.getFailureReport()).hasNumberOfSubReports(4);

        Object failureSubReport = testMethodReport.getFailureReport().getSubReports().get(3).getSubReports().get(9);
        assertThat(failureSubReport).isEqualTo(failureReport);

        assertThatSectionTree(executionStore.getSectionTree())
            .wholeTreeConsistOfCouplesMatching(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport();
    }

    private TestMethodReport getTestMethodReport(ExecutionStore executionStore) {
        List<TestSuiteReport> testSuiteReports = executionStore.getExecutionReport().getTestSuiteReports();
        TestSuiteReport testSuiteReport = testSuiteReports.get(testSuiteReports.size() - 1);
        List<TestClassReport> testClassReports = testSuiteReport.getTestClassReports();
        TestClassReport testClassReport = testClassReports.get(testClassReports.size() - 1);
        List<TestMethodReport> testMethodReports = testClassReport.getTestMethodReports();
        TestMethodReport testMethodReport = testMethodReports.get(testMethodReports.size() - 1);
        return testMethodReport;
    }
}
