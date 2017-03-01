package org.arquillian.reporter.impl.asserts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;

import static org.arquillian.reporter.impl.asserts.TestSuiteReportAssert.assertThatTestSuiteReport;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.ALL_REPORT_TYPES_TO_VERIFY;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteReportName;
import static org.arquillian.reporter.impl.utils.SectionGeneratorUtils.getTestSuiteSectionName;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionReportAssert extends ReportAssert<ExecutionReportAssert, ExecutionReport> {

    public ExecutionReportAssert(ExecutionReport actual) {
        super(actual, ExecutionReportAssert.class);
    }

    public static ExecutionReportAssert assertThatExecutionReport(ExecutionReport actual) {
        return new ExecutionReportAssert(actual);
    }

    /**
     * Go through the whole reporter subtree (all sub-reports regardless their type - eg. also TestSuiteReport for TestExecutionReport)
     * and checks that it contains only generated sub-reports and entries
     */
    public ExecutionReportAssert reportSubtreeConsistOfGeneratedReports() {
        return reportSubtreeConsistOfGeneratedReports(new ArrayList<Report>());
    }

    /**
     * Go through the whole reporter subtree (all sub-reports regardless their type - eg. also TestSuiteReport for TestExecutionReport)
     * and checks that it contains only generated sub-reports and entries, except for the given set of merged reports.
     * It is also checked that all given merged reports are contained in the report tree
     */
    public ExecutionReportAssert reportSubtreeConsistOfGeneratedReports(ArrayList<Report> merged) {
        return reportSubtreeConsistOfGeneratedReports(merged, ALL_REPORT_TYPES_TO_VERIFY);
    }

    /**
     * Go through the whole reporter subtree (all sub-reports regardless their type - eg. also TestSuiteReport for TestExecutionReport)
     * according to the given set of report types. This tells which reports should be verified for their presence. The order
     * in the set is important. Start with the TestSuiteReport.class and then continue with its sub-reports. If you want to
     * verify also the ConfigurationReport then you need to specify this type after each type where the config report should
     * be present. to verify config reports in both TestSuiteReport and TestClassReport, provide this set:
     * <p>
     * TestSuiteReport.class, ConfigurationReport.class, TestClassReport.class, ConfigurationReport.class,
     * </p>
     * When the report types are matched it checks that it contains only generated sub-reports and entries
     */
    public ExecutionReportAssert reportSubtreeConsistOfGeneratedReports(Class<? extends Report>... reportTypes) {
        return reportSubtreeConsistOfGeneratedReports(new ArrayList<Report>(), reportTypes);
    }

    /**
     * Go through the whole reporter subtree (all sub-reports regardless their type - eg. also TestSuiteReport for TestExecutionReport)
     * according to the given set of report types. This tells which reports should be verified for their presence. The order
     * in the set is important. Start with the TestSuiteReport.class and then continue with its sub-reports. If you want to
     * verify also the ConfigurationReport then you need to specify this type after each type where the config report should
     * be present. to verify config reports in both TestSuiteReport and TestClassReport, provide this set:
     * <p>
     * TestSuiteReport.class, ConfigurationReport.class, TestClassReport.class, ConfigurationReport.class,
     * </p>
     * When the report types are matched it checks that it contains only generated sub-reports and entries, except for
     * the given set of merged reports. It is also checked that all given merged reports are contained in the report tree
     */
    public ExecutionReportAssert reportSubtreeConsistOfGeneratedReports(ArrayList<Report> merged,
        Class<? extends Report>... reportTypes) {
        assertThatExecutionReport(actual)
            .as("All sub-reports (regardless their type) in the execution report (with name: <%s>) subtree defined by "
                    + "report types <%s> should consist of generated reports and entries except for the merged ones <%s>",
                actual.getName(), reportTypes, merged)
            .verifyReportSubtreeConsistOfGeneratedReports(merged, reportTypes);
        return this;
    }

    protected ExecutionReportAssert verifyReportSubtreeConsistOfGeneratedReports(ArrayList<Report> merged,
        Class<? extends Report>... reportTypes) {

        List<Class<? extends Report>> types = Arrays.asList(reportTypes);

        // check if TestSuiteReport sub-reports should be verified
        if (types.contains(TestSuiteReport.class)) {
            List<TestSuiteReport> testSuiteReports = actual.getTestSuiteReports();

            // check the expected number of test suite reports - should be same as the number of generated sections
            assertThat(testSuiteReports)
                .as("Execution report should contain an exact number of test suite reports")
                .hasSize(EXPECTED_NUMBER_OF_SECTIONS);

            // for each test suite report
            IntStream.range(0, EXPECTED_NUMBER_OF_SECTIONS).forEach(index -> {
                TestSuiteReport reportOnIndex = testSuiteReports.get(index);

                defaultCheckOfIfMergedOrContainsGeneratedSubReports(merged, index, reportOnIndex);

                assertThatTestSuiteReport(reportOnIndex)
                    .hasName(getTestSuiteReportName(index))
                    .reportSubtreeConsistOfGeneratedReports(getTestSuiteSectionName(index),
                                                            merged,
                                                            reportTypes);
            });
        }
        return this;
    }

    public ExecutionReportAssert hasTestSuiteReportsExactly(TestSuiteReport... testSuiteReports) {
        assertThat(actual.getTestSuiteReports())
            .as("List of test suites reports stored in execution report with name <%s> should contain exactly the given set of test suites",
                actual.getName())
            .containsExactly(testSuiteReports);
        return this;
    }

    public ExecutionReportAssert hasTestSuiteReportListEqualTo(List<TestSuiteReport> testSuiteReportList) {
        assertThat(actual.getTestSuiteReports())
            .as("Test Suite Report list stored in execution report with name <%s> should be equal to the given one",
                actual.getName()).isEqualTo(testSuiteReportList);
        return this;
    }

    public SectionTreeAssert sectionTree() {
        return new SectionTreeAssert(actual.getSectionTree());
    }
}
