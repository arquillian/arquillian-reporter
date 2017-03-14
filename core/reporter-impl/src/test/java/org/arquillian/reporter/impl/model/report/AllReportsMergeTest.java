package org.arquillian.reporter.impl.model.report;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.utils.ReportGeneratorUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
@RunWith(Parameterized.class)
public class AllReportsMergeTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { ExecutionReport.class },
            { BasicReport.class },
            { TestSuiteReport.class },
            { ConfigurationReport.class },
            { TestClassReport.class },
            { TestMethodReport.class },
            { FailureReport.class }
        });
    }

    private Class<AbstractReport> reportClass;

    public AllReportsMergeTest(Class<AbstractReport> reportClass) {
        this.reportClass = reportClass;
    }

    @Test
    public void testDefaultMerge() throws Exception {

        // create main report
        AbstractReport mainReport = ReportGeneratorUtils
            .prepareReport(reportClass, "main report", 1, 5);
        List<BasicReport> firstReports = ReportGeneratorUtils.prepareSetOfReports(BasicReport.class, 5, "first", 1, 5);
        mainReport.getSubReports().addAll(firstReports);

        // create report to merge
        AbstractReport reportToMerge = ReportGeneratorUtils
            .prepareReport(reportClass, "to merge", 5, 10);
        List<BasicReport> secondReports =
            ReportGeneratorUtils.prepareSetOfReports(BasicReport.class, 5, "second", 5, 10);
        reportToMerge.getSubReports().addAll(secondReports);


        mainReport.merge(reportToMerge);

        // verify
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
            .hasName("main report")
            .hasGeneratedSubReportsAndEntries(1, 10)
            .hasNumberOfSubReports(19)
            .hasNumberOfEntries(9);
    }

    @Test
    public void testDefaultMergeWhenFirstNameIsEmpty() throws Exception {

        // create main report
        AbstractReport mainReport = ReportGeneratorUtils
            .prepareReport(reportClass, "", 1, 5);

        // create report to merge
        AbstractReport reportToMerge = ReportGeneratorUtils
            .prepareReport(reportClass, "this name should be stored", 5, 10);


        mainReport.merge(reportToMerge);

        // the main report should contain all information
        assertThatReport(mainReport)
            .hasName("this name should be stored")
            .hasGeneratedSubReportsAndEntries(1, 10)
            .hasNumberOfSubReports(9)
            .hasNumberOfEntries(9);
    }

    @Test
    public void testDefaultMergeWhenFirstNameIsNull() throws Exception {

        // create main report
        AbstractReport mainReport = ReportGeneratorUtils
            .prepareReport(reportClass, "", 1, 5);
        mainReport.setName(null);

        // create report to merge
        AbstractReport reportToMerge = ReportGeneratorUtils
            .prepareReport(reportClass, "this name should be stored", 5, 10);


        mainReport.merge(reportToMerge);

        // the main report should contain all information
        assertThatReport(mainReport)
            .hasName("this name should be stored")
            .hasGeneratedSubReportsAndEntries(1, 10)
            .hasNumberOfSubReports(9)
            .hasNumberOfEntries(9);
    }

    @Test
    public void testAddNewReportToConfigurationReport() throws Exception {
        AbstractReport report = ReportGeneratorUtils.prepareReport(reportClass, "main report name", 1, 5);

        // add a normal report - should be added into List of subReports
        AbstractReport basicReport = ReportGeneratorUtils.prepareReport(reportClass, "report", 5, 10);
        report.addNewReport(basicReport, BasicReport.class);

        // verify
        assertThatReport(report)
            .hasSubReportsEndingWith(basicReport)
            .hasName("main report name")
            .hasGeneratedSubReportsAndEntries(1, 5)
            .hasNumberOfEntries(4)
            .hasNumberOfSubReports(5);

    }

}
