package org.arquillian.reporter.impl.model.report;

import java.text.ParseException;
import java.util.List;

import org.arquillian.reporter.api.builder.BuilderLoader;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.utils.Utils;
import org.junit.Test;

import static org.arquillian.reporter.impl.utils.DummyStringKeys.TEST_CLASS_NAME;
import static org.arquillian.reporter.impl.utils.DummyStringKeys.TEST_SUITE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReportTest {

    @Test
    public void testAddNewReportToTestSuiteReport() throws InstantiationException, IllegalAccessException {
        TestSuiteReport testSuiteReport =
            Utils.prepareReport(TestSuiteReport.class, TEST_SUITE_NAME, 1, 5);

        // add test class report - should be added into List of test class reports
        TestClassReport testClassReportToAdd =
            Utils.prepareReport(TestClassReport.class, TEST_CLASS_NAME, 3, 8);
        testSuiteReport.addNewReport(testClassReportToAdd);

        // add configuration report - should be added into list of configs
        ConfigurationReport configurationReportToAdd =
            Utils.prepareReport(ConfigurationReport.class, "test suite config", 1, 5);
        testSuiteReport.addNewReport(configurationReportToAdd);

        // add a normal report - should be added into List of subReports
        Report basicReport = Utils.prepareReport(Report.class, "report", 5, 10);
        testSuiteReport.addNewReport(basicReport);

        // add another test class report - should be added into List of test class reports
        TestClassReport secondTestClassReportToAdd =
            Utils.prepareReport(TestClassReport.class, "second test class", 3, 8);
        testSuiteReport.addNewReport(secondTestClassReportToAdd);

        // add first test class report for the second time - should be added into List of test class reports
        testSuiteReport.addNewReport(testClassReportToAdd);

        // verify
        assertThat(testSuiteReport.getSubReports())
            .doesNotContain(testClassReportToAdd, configurationReportToAdd, secondTestClassReportToAdd);
        assertThat(testSuiteReport.getSubReports()).last().isEqualTo(basicReport);
        assertThat(testSuiteReport.getConfiguration().getSubReports()).containsExactly(configurationReportToAdd);
        assertThat(testSuiteReport.getTestClassReports())
            .containsExactly(testClassReportToAdd, secondTestClassReportToAdd, testClassReportToAdd);
        Utils.defaultReportVerificationAfterMerge(testSuiteReport, TEST_SUITE_NAME, 1, 5, 5);
    }

    @Test
    public void testNewTestSuiteReportShouldContainStartDate() throws ParseException {
        new DateChecker() {
            @Override
            public String getDateFromReport() {
                return new TestSuiteReport().getStart();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void testStopDateUsingTestSuiteBuilder() throws ParseException {
        BuilderLoader.load();
        TestSuiteReport testSuiteReport = new TestSuiteReport();
        assertThat(testSuiteReport.getStop()).isNull();

        new DateChecker() {
            @Override
            public String getDateFromReport() {
                return Reporter.createReport(testSuiteReport).stop().build().getStop();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void testMergeReports() throws InstantiationException, IllegalAccessException {
        // prepare main
        TestSuiteReport mainTestSuiteReport = Utils.prepareReport(TestSuiteReport.class, "main test suite", 1, 5);
        // add test classes
        List<TestClassReport> firstTestClasses =
            Utils.prepareSetOfReports(TestClassReport.class, 5, "first classes", 1, 5);
        mainTestSuiteReport.getTestClassReports().addAll(firstTestClasses);
        // and config
        List<ConfigurationReport> firstConfigs =
            Utils.prepareSetOfReports(ConfigurationReport.class, 5, "first config", 1, 5);
        mainTestSuiteReport.getConfiguration().getSubReports().addAll(firstConfigs);

        // prepare report to merge
        TestSuiteReport testSuiteReportToMerge = Utils.prepareReport(TestSuiteReport.class, "to merge", 5, 10);
        // add test classes
        List<TestClassReport> classesToMerge =
            Utils.prepareSetOfReports(TestClassReport.class, 5, "second classes", 5, 10);
        testSuiteReportToMerge.getTestClassReports().addAll(classesToMerge);
        // and config
        List<ConfigurationReport> configsToMerge =
            Utils.prepareSetOfReports(ConfigurationReport.class, 5, "second config", 5, 10);
        testSuiteReportToMerge.getConfiguration().getSubReports().addAll(configsToMerge);

        //merge
        mainTestSuiteReport.merge(testSuiteReportToMerge);

        // the report that has been merged is still same
        assertThat(testSuiteReportToMerge.getTestClassReports())
            .containsExactly(classesToMerge.stream().toArray(TestClassReport[]::new));
        assertThat(testSuiteReportToMerge.getConfiguration().getSubReports())
            .containsExactly(configsToMerge.stream().toArray(ConfigurationReport[]::new));
        Utils.defaultReportVerificationAfterMerge(testSuiteReportToMerge, "to merge", 5, 10);

        // the main report should contain all information
        firstTestClasses.addAll(classesToMerge);
        firstConfigs.addAll(configsToMerge);
        assertThat(mainTestSuiteReport.getTestClassReports())
            .containsExactly(firstTestClasses.stream().toArray(TestClassReport[]::new));
        assertThat(mainTestSuiteReport.getConfiguration().getSubReports())
            .containsExactly(firstConfigs.stream().toArray(ConfigurationReport[]::new));
        Utils.defaultReportVerificationAfterMerge(mainTestSuiteReport, "main test suite", 1, 10);
    }
}
