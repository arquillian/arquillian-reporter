package org.arquillian.reporter.impl.model.report;

import java.util.List;

import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.api.event.Identifier;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.arquillian.reporter.impl.utils.Utils;
import org.junit.Test;

import static org.arquillian.reporter.impl.ExecutionReport.EXECUTION_REPORT_NAME;
import static org.arquillian.reporter.impl.ExecutionSection.EXECUTION_SECTION_ID;
import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThat;

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
        Report basicReport = Utils.prepareReport(Report.class, "report", 5, 10);
        executionReport.addNewReport(basicReport);

        // add another test suite report - should be added into List of test suite reports
        TestSuiteReport secondTestSuiteReportToAdd = Utils.prepareReport(TestSuiteReport.class, "second", 5, 10);
        executionReport.addNewReport(secondTestSuiteReportToAdd);

        // add first test suite report for the second time - should be added into List of test suite reports
        executionReport.addNewReport(firstTestSuiteReportToAdd);

        // verify
        assertThat(executionReport).hasNumberOfSubreports(5)
            
//            .subReportsDoesNotContain()
        assertThat(executionReport.getSubReports())
            .doesNotContain(firstTestSuiteReportToAdd, secondTestSuiteReportToAdd);
        assertThat(executionReport.getSubReports()).last().isEqualTo(basicReport);
        assertThat(executionReport.getTestSuiteReports())
            .containsExactly(firstTestSuiteReportToAdd, secondTestSuiteReportToAdd, firstTestSuiteReportToAdd);
        assertThat(executionReport.getName()).isEqualTo(new UnknownStringKey(EXECUTION_REPORT_NAME));
        Utils.defaultReportVerificationAfterMerge(executionReport, EXECUTION_REPORT_NAME, 1, 5, 5);
    }

    @Test
    public void testNewExecutionReportShouldContainSectionWithTheSameReportItself() {
        ExecutionReport executionReport = new ExecutionReport();

        assertThat(executionReport.getExecutionSection().getSectionId()).isEqualTo(EXECUTION_SECTION_ID);
        assertThat(executionReport.getExecutionSection().getReportTypeClass()).isAssignableFrom(ExecutionReport.class);
        assertThat(executionReport.getExecutionSection().getReport()).isEqualTo(executionReport);
    }

    @Test
    public void testNewExecutionReportShouldContainExecutionSectionTree() {
        ExecutionReport executionReport = new ExecutionReport();
        Identifier identifier = new Identifier<>(ExecutionSection.class, EXECUTION_SECTION_ID);

        assertThat(executionReport.getSectionTree().getRootIdentifier()).isEqualTo(identifier);
        assertThat(executionReport.getSectionTree().getAssociatedReport()).isEqualTo(executionReport);

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
        assertThat(executionReportToMerge.getTestSuiteReports())
            .containsExactly(secondTestSuites.stream().toArray(TestSuiteReport[]::new));
        Utils.defaultReportVerificationAfterMerge(executionReportToMerge, "to merge", 5, 10);

        // the main report should contain all information
        firstTestSuites.addAll(secondTestSuites);
        assertThat(mainExecutionReport.getTestSuiteReports())
            .containsExactly(firstTestSuites.stream().toArray(TestSuiteReport[]::new));
        Utils.defaultReportVerificationAfterMerge(mainExecutionReport, EXECUTION_REPORT_NAME, 1, 10);
    }
}
