package org.arquillian.reporter.impl.model.report;

import java.util.List;

import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.impl.utils.Utils;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;

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
        assertThatReport(report)
            .hasSubReportsEndingWith(basicReport)
            .hasName("report name")
            .hasGeneratedSubreportsAndEntries(1, 5)
            .hasNumberOfEntries(4)
            .hasNumberOfSubreports(5);

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
        assertThatReport(reportToMerge)
            .hasSubReportsEndingWith(secondReports.stream().toArray(Report[]::new))
            .hasName("to merge")
            .hasGeneratedSubreportsAndEntries(5, 10)
            .hasNumberOfSubreports(10)
            .hasNumberOfEntries(5);

        // the main report should contain all information
        assertThatReport(mainReport)
            .hassSubReportsContaining(firstReports.stream().toArray(Report[]::new))
            .hasSubReportsEndingWith(secondReports.stream().toArray(Report[]::new))
            .hasName("report")
            .hasGeneratedSubreportsAndEntries(1, 10)
            .hasNumberOfSubreports(19)
            .hasNumberOfEntries(9);
    }
}
