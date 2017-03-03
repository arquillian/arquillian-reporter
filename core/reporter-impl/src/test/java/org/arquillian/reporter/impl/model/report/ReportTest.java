package org.arquillian.reporter.impl.model.report;

import java.util.List;

import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.impl.utils.ReportGeneratorUtils;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportTest {

    @Test
    public void testAddNewReportToConfigurationReport() throws Exception {
        BasicReport report = ReportGeneratorUtils.prepareReport(BasicReport.class, "report name", 1, 5);

        // add a normal report - should be added into List of subReports
        BasicReport basicReport = ReportGeneratorUtils.prepareReport(BasicReport.class, "report", 5, 10);
        report.addNewReport(basicReport, BasicReport.class);

        // verify
        assertThatReport(report)
            .hasSubReportsEndingWith(basicReport)
            .hasName("report name")
            .hasGeneratedSubReportsAndEntries(1, 5)
            .hasNumberOfEntries(4)
            .hasNumberOfSubReports(5);

    }

    @Test
    public void testMergeReports() throws Exception {
        BasicReport mainReport = ReportGeneratorUtils.prepareReport(BasicReport.class, "report", 1, 5);
        List<BasicReport> firstReports = ReportGeneratorUtils.prepareSetOfReports(BasicReport.class, 5, "first", 1, 5);
        mainReport.getSubReports().addAll(firstReports);

        BasicReport reportToMerge = ReportGeneratorUtils.prepareReport(BasicReport.class, "to merge", 5, 10);
        List<BasicReport> secondReports =
            ReportGeneratorUtils.prepareSetOfReports(BasicReport.class, 5, "second", 5, 10);
        reportToMerge.getSubReports().addAll(secondReports);

        //merge
        mainReport.merge(reportToMerge);

        // the report that has been merged is still same
        assertThatReport(reportToMerge)
            .hasSubReportsEndingWith(secondReports.stream().toArray(BasicReport[]::new))
            .hasName("to merge")
            .hasGeneratedSubReportsAndEntries(5, 10)
            .hasNumberOfSubReports(10)
            .hasNumberOfEntries(5);

        // the main report should contain all information
        assertThatReport(mainReport)
            .hasSubReportsContaining(firstReports.stream().toArray(BasicReport[]::new))
            .hasSubReportsEndingWith(secondReports.stream().toArray(BasicReport[]::new))
            .hasName("report")
            .hasGeneratedSubReportsAndEntries(1, 10)
            .hasNumberOfSubReports(19)
            .hasNumberOfEntries(9);
    }
}
