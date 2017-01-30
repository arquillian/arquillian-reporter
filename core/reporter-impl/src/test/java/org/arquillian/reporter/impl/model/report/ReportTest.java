package org.arquillian.reporter.impl.model.report;

import java.util.List;

import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.impl.utils.Utils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportTest {

    @Test
    public void testAddNewReportToConfigurationReport() throws InstantiationException, IllegalAccessException {
        Report report = Utils.prepareReport(Report.class, "report name", 1, 5);

        // add a normal report - should be added into List of subReports
        Report basicReport = Utils.prepareReport(Report.class, "report", 5, 10);
        report.addNewReport(basicReport);

        // verify
        assertThat(report.getSubReports()).last().isEqualTo(basicReport);
        Utils.defaultReportVerificationAfterMerge(report, "report name", 1, 5, 5);

    }

    @Test
    public void testMergeReports() throws InstantiationException, IllegalAccessException {
        Report mainReport = Utils.prepareReport(Report.class, "report", 1, 5);
        List<Report> firstReports = Utils.prepareSetOfReports(Report.class, 5, "first", 1, 5);
        mainReport.getSubReports().addAll(firstReports);

        Report reportToMerge = Utils.prepareReport(Report.class, "to merge", 5, 10);
        List<Report> secondReports = Utils.prepareSetOfReports(Report.class, 5, "second", 5, 10);
        reportToMerge.getSubReports().addAll(secondReports);

        //merge
        mainReport.merge(reportToMerge);

        // the report that has been merged is still same
        assertThat(reportToMerge.getSubReports()).endsWith(secondReports.stream().toArray(Report[]::new));
        Utils.defaultReportVerificationAfterMerge(reportToMerge, "to merge", 5, 10, 10);

        // the main report should contain all information
        assertThat(mainReport.getSubReports()).contains(firstReports.stream().toArray(Report[]::new));
        assertThat(mainReport.getSubReports()).endsWith(secondReports.stream().toArray(Report[]::new));
        Utils.defaultReportVerificationAfterMerge(mainReport, "report", 1, 10, 19);
    }
}
