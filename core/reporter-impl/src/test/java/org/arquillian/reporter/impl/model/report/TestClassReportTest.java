package org.arquillian.reporter.impl.model.report;

import java.text.ParseException;
import java.util.List;

import org.arquillian.reporter.api.builder.BuilderLoader;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.impl.utils.ReportGeneratorUtils;
import org.arquillian.reporter.impl.utils.dummy.DummyStringKeys;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.TestClassReportAssert.assertThatTestClassReport;
import static org.arquillian.reporter.impl.utils.dummy.DummyStringKeys.TEST_CLASS_NAME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReportTest {

    @Test
    public void testAddNewReportToTestClassReport() throws Exception {
        TestClassReport testClassReport = ReportGeneratorUtils
            .prepareReport(TestClassReport.class, "main test class", 1, 5);

        // add configuration report - should be added into list of configs
        ConfigurationReport configurationReportToAdd =
            ReportGeneratorUtils
                .prepareReport(ConfigurationReport.class, DummyStringKeys.TEST_CLASS_CONFIG_NAME, 5, 10);
        testClassReport.addNewReport(configurationReportToAdd, ConfigurationReport.class);

        // add test method report - should be added into list of set method reports
        TestMethodReport testMethodReportToAdd = ReportGeneratorUtils
            .prepareReport(TestMethodReport.class, "test method name", 3, 8);
        testClassReport.addNewReport(testMethodReportToAdd, TestMethodReport.class);

        // add a normal report - should be added into List of subReports
        BasicReport basicReport = ReportGeneratorUtils.prepareReport(BasicReport.class, "report", 5, 10);
        testClassReport.addNewReport(basicReport, BasicReport.class);

        // add test method report - should be added into list of set method reports
        TestMethodReport secondTestMethodReportToAdd =
            ReportGeneratorUtils.prepareReport(TestMethodReport.class, "test method name", 3, 8);
        testClassReport.addNewReport(secondTestMethodReportToAdd, TestMethodReport.class);

        // add test method report - should be added into list of set method reports
        testClassReport.addNewReport(testMethodReportToAdd, TestMethodReport.class);

        // verify
        assertThatTestClassReport(testClassReport)
            .hasName("main test class")
            .hasConfigSubReportsContainingExactly(configurationReportToAdd)
            .hasTestMethodReportsExactly(testMethodReportToAdd, secondTestMethodReportToAdd, testMethodReportToAdd)
            .hasSubReportsWithout(configurationReportToAdd, testMethodReportToAdd, secondTestMethodReportToAdd)
            .hasSubReportsEndingWith(basicReport)
            .hasGeneratedSubReportsAndEntries(1, 5)
            .hasNumberOfSubReports(5)
            .hasNumberOfEntries(4);
    }

    @Test
    public void testNewTestClassReportShouldContainStartDate() throws ParseException {
        new DateChecker() {
            @Override
            public String getDateFromReport() {
                return new TestClassReport().getExecutionStartTime();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void testStopDateUsingTestClassBuilder() throws ParseException {
        BuilderLoader.load();
        TestClassReport testClassReport = new TestClassReport();
        assertThat(testClassReport.getExecutionStopTime()).isNull();

        new DateChecker() {
            @Override
            public String getDateFromReport() {
                return Reporter.createReport(testClassReport).stop().build().getExecutionStopTime();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void testMergeReports() throws Exception {
        // prepare main
        TestClassReport mainTestClassReport = ReportGeneratorUtils
            .prepareReport(TestClassReport.class, TEST_CLASS_NAME, 1, 5);
        // add test methods
        List<TestMethodReport> firstMethods =
            ReportGeneratorUtils.prepareSetOfReports(TestMethodReport.class, 5, "first method", 1, 5);
        mainTestClassReport.getTestMethodReports().addAll(firstMethods);
        // and config
        List<ConfigurationReport> firstConfigs =
            ReportGeneratorUtils.prepareSetOfReports(ConfigurationReport.class, 5, "first config", 1, 5);
        mainTestClassReport.getConfiguration().getSubReports().addAll(firstConfigs);

        // prepare report to merge
        TestClassReport testClassToMerge = ReportGeneratorUtils.prepareReport(TestClassReport.class, "to merge", 5, 10);
        // add test methods
        List<TestMethodReport> methodsToMerge =
            ReportGeneratorUtils.prepareSetOfReports(TestMethodReport.class, 5, "second method", 5, 10);
        testClassToMerge.getTestMethodReports().addAll(methodsToMerge);
        // and config
        List<ConfigurationReport> configsToMerge =
            ReportGeneratorUtils.prepareSetOfReports(ConfigurationReport.class, 5, "second config", 5, 10);
        testClassToMerge.getConfiguration().getSubReports().addAll(configsToMerge);

        //merge
        mainTestClassReport.merge(testClassToMerge);

        // the report that has been merged is still same
        assertThatTestClassReport(testClassToMerge)
            .hasName("to merge")
            .hasTestMethodReportListEqualTo(methodsToMerge)
            .hasConfigSubReportListEqualTo(configsToMerge)
            .hasGeneratedSubReportsAndEntries(5, 10)
            .hasNumberOfSubReportsAndEntries(5);

        // the main report should contain all information
        firstMethods.addAll(methodsToMerge);
        firstConfigs.addAll(configsToMerge);

        //verify
        assertThatTestClassReport(mainTestClassReport)
            .hasName(TEST_CLASS_NAME)
            .hasTestMethodReportListEqualTo(firstMethods)
            .hasConfigSubReportListEqualTo(firstConfigs)
            .hasGeneratedSubReportsAndEntries(1, 10)
            .hasNumberOfSubReportsAndEntries(9);
    }
}
