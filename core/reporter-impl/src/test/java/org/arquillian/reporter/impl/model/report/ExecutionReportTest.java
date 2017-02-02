package org.arquillian.reporter.impl.model.report;

import java.util.List;

import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.utils.Utils;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import static org.arquillian.reporter.impl.ExecutionReport.EXECUTION_REPORT_NAME;
import static org.arquillian.reporter.impl.ExecutionSection.EXECUTION_SECTION_ID;
import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.asserts.SectionAssert.assertThatSection;
import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionReportTest {

    @Test
    public void testAddNewReportToExecutionReport() throws InstantiationException, IllegalAccessException {
        ExecutionReport executionReport =
            Utils.prepareReport(ExecutionReport.class, EXECUTION_REPORT_NAME, 1, 5);

        // add test suite report - should be added into List of test suite reports
        TestSuiteReport firstTestSuiteReportToAdd = Utils.prepareReport(TestSuiteReport.class, "first", 5, 10);
        executionReport.addNewReport(firstTestSuiteReportToAdd);

        // add a normal report - should be added into List of subReports
        BasicReport basicReport = Utils.prepareReport(BasicReport.class, "report", 5, 10);
        executionReport.addNewReport(basicReport);

        // add another test suite report - should be added into List of test suite reports
        TestSuiteReport secondTestSuiteReportToAdd = Utils.prepareReport(TestSuiteReport.class, "second", 5, 10);
        executionReport.addNewReport(secondTestSuiteReportToAdd);

        // add first test suite report for the second time - should be added into List of test suite reports
        executionReport.addNewReport(firstTestSuiteReportToAdd);

        // verify
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(executionReport)
                .hasName(EXECUTION_REPORT_NAME)
                .hasNumberOfSubreports(5)
                .hasSubReportsEndingWith(basicReport)
                .hasSubReportsWithout(firstTestSuiteReportToAdd, secondTestSuiteReportToAdd)
                .hasGeneratedSubreportsAndEntries(1, 5)
                .hasNumberOfEntries(4);

            assertThat(executionReport.getTestSuiteReports())
                .containsExactly(firstTestSuiteReportToAdd, secondTestSuiteReportToAdd, firstTestSuiteReportToAdd);
        });
    }

    @Test
    public void testNewExecutionReportShouldContainSectionWithTheSameReportItself() {
        ExecutionReport executionReport = new ExecutionReport();

        assertThatSection(executionReport.getExecutionSection())
            .hasSectionId(EXECUTION_SECTION_ID)
            .hasReportOfTypeThatIsAssignableFrom(ExecutionReport.class)
            .hasReportEqualTo(executionReport);
    }

    @Test
    public void testNewExecutionReportShouldContainExecutionSectionTree() {
        ExecutionReport executionReport = new ExecutionReport();
        Identifier identifier = new Identifier<>(ExecutionSection.class, EXECUTION_SECTION_ID);

        assertThatSectionTree(executionReport.getSectionTree())
            .hasRootIdentifier(identifier)
            .hasAssociatedReport(executionReport);

    }

    @Test
    public void testMergeReports() throws InstantiationException, IllegalAccessException {
        ExecutionReport mainExecutionReport = Utils.prepareReport(ExecutionReport.class, EXECUTION_REPORT_NAME, 1, 5);
        List<TestSuiteReport> firstTestSuites = Utils.prepareSetOfReports(TestSuiteReport.class, 5, "first", 1, 5);
        mainExecutionReport.getTestSuiteReports().addAll(firstTestSuites);

        ExecutionReport executionReportToMerge = Utils.prepareReport(ExecutionReport.class, "to merge", 5, 10);
        List<TestSuiteReport> secondTestSuites = Utils.prepareSetOfReports(TestSuiteReport.class, 5, "second", 5, 10);
        executionReportToMerge.getTestSuiteReports().addAll(secondTestSuites);

        //merge
        mainExecutionReport.merge(executionReportToMerge);

        // the report that has been merged is still same
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(executionReportToMerge)
                .hasName("to merge")
                .hasGeneratedSubreportsAndEntries(5, 10)
                .hasNumberOfSubreportsAndEntries(5);

            assertThat(executionReportToMerge.getTestSuiteReports()).isEqualTo(secondTestSuites);
        });

        // the main report should contain all information
        firstTestSuites.addAll(secondTestSuites);
        // verify
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(mainExecutionReport)
                .hasName(EXECUTION_REPORT_NAME)
                .hasGeneratedSubreportsAndEntries(1, 10)
                .hasNumberOfSubreportsAndEntries(9);

            assertThat(mainExecutionReport.getTestSuiteReports()).isEqualTo(firstTestSuites);
        });
    }
}
