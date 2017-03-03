package org.arquillian.reporter.impl.model.report;

import java.text.ParseException;
import java.util.List;

import org.arquillian.reporter.api.builder.BuilderLoader;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.impl.utils.ReportGeneratorUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.TestMethodReportAssert.assertThatTestMethodReport;
import static org.arquillian.reporter.impl.utils.dummy.DummyStringKeys.TEST_METHOD_NAME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodTest {

    @Test
    public void testAddNewReportToTestMethodReport() throws Exception {
        TestMethodReport testMethodReport = ReportGeneratorUtils
            .prepareReport(TestMethodReport.class, TEST_METHOD_NAME, 1, 5);

        // add configuration report - should be added into list of configs
        ConfigurationReport configurationReportToAdd =
            ReportGeneratorUtils.prepareReport(ConfigurationReport.class, "method config", 5, 10);
        testMethodReport.addNewReport(configurationReportToAdd, ConfigurationReport.class);

        // add failur method report - should be added into list of failures
        FailureReport failureReportToAdd = ReportGeneratorUtils
            .prepareReport(FailureReport.class, "test method failure", 3, 8);
        testMethodReport.addNewReport(failureReportToAdd, FailureReport.class);

        // add a normal report - should be added into List of subReports
        BasicReport basicReport = ReportGeneratorUtils.prepareReport(BasicReport.class, "report", 5, 10);
        testMethodReport.addNewReport(basicReport, BasicReport.class);

        // verify
        SoftAssertions.assertSoftly(softly -> {
            assertThatTestMethodReport(testMethodReport)
                .hasName(TEST_METHOD_NAME)
                .hasConfigSubReportsContainingExactly(configurationReportToAdd)
                .hasFailureSubReportsContainingExactly(failureReportToAdd)
                .hasSubReportsWithout(configurationReportToAdd, failureReportToAdd)
                .hasSubReportsEndingWith(basicReport)
                .hasGeneratedSubReportsAndEntries(1, 5)
                .hasNumberOfSubReports(5)
                .hasNumberOfEntries(4);
        });
    }

    @Test
    public void testNewTestMethodReportShouldContainStartDate() throws ParseException {
        new DateChecker() {
            @Override
            public String getDateFromReport() {
                return new TestMethodReport().getStart();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void testStopDateUsingTestClassBuilder() throws ParseException {
        BuilderLoader.load();
        TestMethodReport testMethodReport = new TestMethodReport();
        assertThat(testMethodReport.getStop()).isNull();

        new DateChecker() {
            @Override
            public String getDateFromReport() {
                return Reporter.createReport(testMethodReport).stop().build().getStop();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void testMergeReports() throws Exception {
        // prepare main
        TestMethodReport mainTestMethodReport = ReportGeneratorUtils
            .prepareReport(TestMethodReport.class, TEST_METHOD_NAME, 1, 5);
        // add failure report
        List<FailureReport> firstFailure = ReportGeneratorUtils
            .prepareSetOfReports(FailureReport.class, 5, "first failure", 1, 5);
        mainTestMethodReport.getFailureReport().getSubReports().addAll(firstFailure);
        // and config
        List<ConfigurationReport> firstConfigs =
            ReportGeneratorUtils.prepareSetOfReports(ConfigurationReport.class, 5, "first config", 1, 5);
        mainTestMethodReport.getConfiguration().getSubReports().addAll(firstConfigs);

        // prepare report to merge
        TestMethodReport testMethodToMerge = ReportGeneratorUtils
            .prepareReport(TestMethodReport.class, "to merge", 5, 10);
        // add failure reports
        List<FailureReport> failuresToMerge =
            ReportGeneratorUtils.prepareSetOfReports(FailureReport.class, 5, "second failure", 5, 10);
        testMethodToMerge.getFailureReport().getSubReports().addAll(failuresToMerge);
        // and config
        List<ConfigurationReport> configsToMerge =
            ReportGeneratorUtils.prepareSetOfReports(ConfigurationReport.class, 5, "second config", 5, 10);
        testMethodToMerge.getConfiguration().getSubReports().addAll(configsToMerge);

        //merge
        mainTestMethodReport.merge(testMethodToMerge);

        // the report that has been merged is still same
        assertThatTestMethodReport(testMethodToMerge)
            .hasName("to merge")
            .hasFailureSubReportListEqualTo(failuresToMerge)
            .hasConfigSubReportListEqualTo(configsToMerge)
            .hasGeneratedSubReportsAndEntries(5, 10)
            .hasNumberOfSubReportsAndEntries(5);

        // the main report should contain all information
        firstFailure.addAll(failuresToMerge);
        firstConfigs.addAll(configsToMerge);

        //verify
        assertThatTestMethodReport(mainTestMethodReport)
            .hasName(TEST_METHOD_NAME)
            .hasFailureSubReportListEqualTo(firstFailure)
            .hasConfigSubReportListEqualTo(firstConfigs)
            .hasGeneratedSubReportsAndEntries(1, 10)
            .hasNumberOfSubReportsAndEntries(9);
    }

}
