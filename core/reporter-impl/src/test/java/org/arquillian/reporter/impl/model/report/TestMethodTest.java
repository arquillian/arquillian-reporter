package org.arquillian.reporter.impl.model.report;

import java.text.ParseException;
import java.util.List;

import org.arquillian.reporter.api.builder.BuilderLoader;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.impl.utils.Utils;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.utils.dummy.DummyStringKeys.TEST_METHOD_NAME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodTest {

    @Test
    public void testAddNewReportToTestMethodReport() throws InstantiationException, IllegalAccessException {
        TestMethodReport testMethodReport = Utils.prepareReport(TestMethodReport.class, TEST_METHOD_NAME, 1, 5);

        // add configuration report - should be added into list of configs
        ConfigurationReport configurationReportToAdd =
            Utils.prepareReport(ConfigurationReport.class, "method config", 5, 10);
        testMethodReport.addNewReport(configurationReportToAdd);

        // add failur method report - should be added into list of failures
        FailureReport failureReportToAdd = Utils.prepareReport(FailureReport.class, "test method failure", 3, 8);
        testMethodReport.addNewReport(failureReportToAdd);

        // add a normal report - should be added into List of subReports
        Report basicReport = Utils.prepareReport(Report.class, "report", 5, 10);
        testMethodReport.addNewReport(basicReport);

        // verify
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(testMethodReport)
                .hasName(TEST_METHOD_NAME)
                .hasSubReportsWithout(configurationReportToAdd, failureReportToAdd)
                .hasSubReportsEndingWith(basicReport)
                .hasGeneratedSubreportsAndEntries(1, 5)
                .hasNumberOfSubreports(5)
                .hasNumberOfEntries(4);

            assertThat(testMethodReport.getConfiguration().getSubReports()).containsExactly(configurationReportToAdd);
            assertThat(testMethodReport.getFailureReport().getSubReports()).containsExactly(failureReportToAdd);
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
    public void testMergeReports() throws InstantiationException, IllegalAccessException {
        // prepare main
        TestMethodReport mainTestMethodReport = Utils.prepareReport(TestMethodReport.class, TEST_METHOD_NAME, 1, 5);
        // add failure report
        List<FailureReport> firstFailure = Utils.prepareSetOfReports(FailureReport.class, 5, "first failure", 1, 5);
        mainTestMethodReport.getFailureReport().getSubReports().addAll(firstFailure);
        // and config
        List<ConfigurationReport> firstConfigs =
            Utils.prepareSetOfReports(ConfigurationReport.class, 5, "first config", 1, 5);
        mainTestMethodReport.getConfiguration().getSubReports().addAll(firstConfigs);

        // prepare report to merge
        TestMethodReport testMethodToMerge = Utils.prepareReport(TestMethodReport.class, "to merge", 5, 10);
        // add failure reports
        List<FailureReport> failuresToMerge =
            Utils.prepareSetOfReports(FailureReport.class, 5, "second failure", 5, 10);
        testMethodToMerge.getFailureReport().getSubReports().addAll(failuresToMerge);
        // and config
        List<ConfigurationReport> configsToMerge =
            Utils.prepareSetOfReports(ConfigurationReport.class, 5, "second config", 5, 10);
        testMethodToMerge.getConfiguration().getSubReports().addAll(configsToMerge);

        //merge
        mainTestMethodReport.merge(testMethodToMerge);

        // the report that has been merged is still same
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(testMethodToMerge)
                .hasName("to merge")
                .hasGeneratedSubreportsAndEntries(5, 10)
                .hasNumberOfSubreportsAndEntries(5);

            assertThat(testMethodToMerge.getFailureReport().getSubReports()).isEqualTo(failuresToMerge);
            assertThat(testMethodToMerge.getConfiguration().getSubReports()).isEqualTo(configsToMerge);
        });

        // the main report should contain all information
        firstFailure.addAll(failuresToMerge);
        firstConfigs.addAll(configsToMerge);

        //verify
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(mainTestMethodReport)
                .hasName(TEST_METHOD_NAME)
                .hasGeneratedSubreportsAndEntries(1, 10)
                .hasNumberOfSubreportsAndEntries(9);

            assertThat(mainTestMethodReport.getFailureReport().getSubReports()).isEqualTo(firstFailure);
            assertThat(mainTestMethodReport.getConfiguration().getSubReports()).isEqualTo(firstConfigs);
        });
    }

}
