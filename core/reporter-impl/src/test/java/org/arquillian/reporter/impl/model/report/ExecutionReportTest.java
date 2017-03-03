package org.arquillian.reporter.impl.model.report;

import java.util.List;

import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.utils.ReportGeneratorUtils;
import org.junit.Test;

import static org.arquillian.reporter.impl.ExecutionReport.EXECUTION_REPORT_NAME;
import static org.arquillian.reporter.impl.ExecutionSection.EXECUTION_SECTION_ID;
import static org.arquillian.reporter.impl.asserts.ExecutionReportAssert.assertThatExecutionReport;
import static org.arquillian.reporter.impl.asserts.SectionAssert.assertThatSection;
import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionReportTest {

    @Test
    public void testAddNewReportToExecutionReport() throws Exception {
        ExecutionReport executionReport =
            ReportGeneratorUtils.prepareReport(ExecutionReport.class, EXECUTION_REPORT_NAME, 1, 5);

        // add test suite report - should be added into List of test suite reports
        TestSuiteReport firstTestSuiteReportToAdd = ReportGeneratorUtils
            .prepareReport(TestSuiteReport.class, "first", 5, 10);
        executionReport.addNewReport(firstTestSuiteReportToAdd, TestSuiteReport.class);

        // add a normal report - should be added into List of subReports
        BasicReport basicReport = ReportGeneratorUtils.prepareReport(BasicReport.class, "report", 5, 10);
        executionReport.addNewReport(basicReport, BasicReport.class);

        // add another test suite report - should be added into List of test suite reports
        TestSuiteReport secondTestSuiteReportToAdd = ReportGeneratorUtils
            .prepareReport(TestSuiteReport.class, "second", 5, 10);
        executionReport.addNewReport(secondTestSuiteReportToAdd, TestSuiteReport.class);

        // add first test suite report for the second time - should be added into List of test suite reports
        executionReport.addNewReport(firstTestSuiteReportToAdd, TestSuiteReport.class);

        // verify
        assertThatExecutionReport(executionReport)
            .hasName(EXECUTION_REPORT_NAME)
            .hasTestSuiteReportsExactly(firstTestSuiteReportToAdd, secondTestSuiteReportToAdd,
                                        firstTestSuiteReportToAdd)
            .hasNumberOfSubReports(5)
            .hasSubReportsEndingWith(basicReport)
            .hasSubReportsWithout(firstTestSuiteReportToAdd, secondTestSuiteReportToAdd)
            .hasGeneratedSubReportsAndEntries(1, 5)
            .hasNumberOfEntries(4);
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
    public void testMergeReports() throws Exception {
        ExecutionReport mainExecutionReport = ReportGeneratorUtils
            .prepareReport(ExecutionReport.class, EXECUTION_REPORT_NAME, 1, 5);
        List<TestSuiteReport> firstTestSuites = ReportGeneratorUtils
            .prepareSetOfReports(TestSuiteReport.class, 5, "first", 1, 5);
        mainExecutionReport.getTestSuiteReports().addAll(firstTestSuites);

        ExecutionReport executionReportToMerge = ReportGeneratorUtils
            .prepareReport(ExecutionReport.class, "to merge", 5, 10);
        List<TestSuiteReport> secondTestSuites = ReportGeneratorUtils
            .prepareSetOfReports(TestSuiteReport.class, 5, "second", 5, 10);
        executionReportToMerge.getTestSuiteReports().addAll(secondTestSuites);

        //merge
        mainExecutionReport.merge(executionReportToMerge);

        // the report that has been merged is still same
        assertThatExecutionReport(executionReportToMerge)
            .hasName("to merge")
            .hasTestSuiteReportListEqualTo(secondTestSuites)
            .hasGeneratedSubReportsAndEntries(5, 10)
            .hasNumberOfSubReportsAndEntries(5);

        // the main report should contain all information
        firstTestSuites.addAll(secondTestSuites);

        // verify
        assertThatExecutionReport(mainExecutionReport)
            .hasName(EXECUTION_REPORT_NAME)
            .hasTestSuiteReportListEqualTo(firstTestSuites)
            .hasGeneratedSubReportsAndEntries(1, 10)
            .hasNumberOfSubReportsAndEntries(9);
    }
}
