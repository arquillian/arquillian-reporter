package org.arquillian.reporter.impl.model.report;

import java.util.List;

import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.impl.utils.Utils;
import org.junit.Test;

import static org.arquillian.reporter.impl.utils.DummyStringKeys.FAILURE_REPORT_NAME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FailureReportTest {

    @Test
    public void testAddNewReportToFailureReport() throws InstantiationException, IllegalAccessException {
        FailureReport failureReport = Utils.prepareReport(FailureReport.class, FAILURE_REPORT_NAME, 1, 5);

        // add a normal report - should be added into List of subReports
        Report basicReport = Utils.prepareReport(Report.class, "report", 5, 10);
        failureReport.addNewReport(basicReport);

        // verify
        assertThat(failureReport.getSubReports()).last().isEqualTo(basicReport);
        assertThat(failureReport.getSubReports()).hasSize(5);

        // add failure report - should be added into List of subReports
        FailureReport failureToAdd = Utils.prepareReport(FailureReport.class, FAILURE_REPORT_NAME, 5, 10);
        failureReport.addNewReport(failureToAdd);

        // verify
        assertThat(failureReport.getSubReports()).endsWith(basicReport, failureToAdd);
        Utils.defaultReportVerificationAfterMerge(failureReport, FAILURE_REPORT_NAME, 1, 5, 6);
    }

    @Test
    public void testMergeReports() throws InstantiationException, IllegalAccessException {
        FailureReport mainFailureReport = Utils.prepareReport(FailureReport.class, FAILURE_REPORT_NAME, 1, 5);
        List<FailureReport> firstFailure = Utils.prepareSetOfReports(FailureReport.class, 5, "first", 1, 5);
        mainFailureReport.getSubReports().addAll(firstFailure);

        FailureReport failureToMerge = Utils.prepareReport(FailureReport.class, "to merge", 5, 10);
        List<FailureReport> secondFailure = Utils.prepareSetOfReports(FailureReport.class, 5, "second", 5, 10);
        failureToMerge.getSubReports().addAll(secondFailure);

        //merge
        mainFailureReport.merge(failureToMerge);

        // the report that has been merged is still same
        assertThat(failureToMerge.getSubReports()).endsWith(secondFailure.stream().toArray(FailureReport[]::new));
        Utils.defaultReportVerificationAfterMerge(failureToMerge, "to merge", 5, 10, 10);

        // the main report should contain all information
        assertThat(mainFailureReport.getSubReports())
            .contains(firstFailure.stream().toArray(FailureReport[]::new));
        assertThat(mainFailureReport.getSubReports())
            .endsWith(secondFailure.stream().toArray(FailureReport[]::new));
        Utils.defaultReportVerificationAfterMerge(mainFailureReport, FAILURE_REPORT_NAME, 1, 10, 19);
    }
}
