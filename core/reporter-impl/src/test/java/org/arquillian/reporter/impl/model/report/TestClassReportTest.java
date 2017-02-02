package org.arquillian.reporter.impl.model.report;

import java.text.ParseException;
import java.util.List;

import org.arquillian.reporter.api.builder.BuilderLoader;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.impl.utils.dummy.DummyStringKeys;
import org.arquillian.reporter.impl.utils.Utils;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;
import static org.arquillian.reporter.impl.utils.dummy.DummyStringKeys.TEST_CLASS_NAME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReportTest {

    @Test
    public void testAddNewReportToTestClassReport() throws InstantiationException, IllegalAccessException {
        TestClassReport testClassReport = Utils.prepareReport(TestClassReport.class, "main test class", 1, 5);

        // add configuration report - should be added into list of configs
        ConfigurationReport configurationReportToAdd =
            Utils.prepareReport(ConfigurationReport.class, DummyStringKeys.TEST_CLASS_CONFIG_NAME, 5, 10);
        testClassReport.addNewReport(configurationReportToAdd);

        // add test method report - should be added into list of set method reports
        TestMethodReport testMethodReportToAdd = Utils.prepareReport(TestMethodReport.class, "test method name", 3, 8);
        testClassReport.addNewReport(testMethodReportToAdd);

        // add a normal report - should be added into List of subReports
        BasicReport basicReport = Utils.prepareReport(BasicReport.class, "report", 5, 10);
        testClassReport.addNewReport(basicReport);

        // add test method report - should be added into list of set method reports
        TestMethodReport secondTestMethodReportToAdd =
            Utils.prepareReport(TestMethodReport.class, "test method name", 3, 8);
        testClassReport.addNewReport(secondTestMethodReportToAdd);

        // add test method report - should be added into list of set method reports
        testClassReport.addNewReport(testMethodReportToAdd);

        // verify
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(testClassReport)
                .hasName("main test class")
                .hasSubReportsWithout(configurationReportToAdd, testMethodReportToAdd, secondTestMethodReportToAdd)
                .hasSubReportsEndingWith(basicReport)
                .hasGeneratedSubreportsAndEntries(1, 5)
                .hasNumberOfSubreports(5)
                .hasNumberOfEntries(4);

            assertThat(testClassReport.getConfiguration().getSubReports()).containsExactly(configurationReportToAdd);
            assertThat(testClassReport.getTestMethodReports())
                .containsExactly(testMethodReportToAdd, secondTestMethodReportToAdd, testMethodReportToAdd);
        });
    }

    @Test
    public void testNewTestClassReportShouldContainStartDate() throws ParseException {
        new DateChecker() {
            @Override
            public String getDateFromReport() {
                return new TestClassReport().getStart();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void testStopDateUsingTestClassBuilder() throws ParseException {
        BuilderLoader.load();
        TestClassReport testClassReport = new TestClassReport();
        assertThat(testClassReport.getStop()).isNull();

        new DateChecker() {
            @Override
            public String getDateFromReport() {
                return Reporter.createReport(testClassReport).stop().build().getStop();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void testMergeReports() throws InstantiationException, IllegalAccessException {
        // prepare main
        TestClassReport mainTestClassReport = Utils.prepareReport(TestClassReport.class, TEST_CLASS_NAME, 1, 5);
        // add test methods
        List<TestMethodReport> firstMethods =
            Utils.prepareSetOfReports(TestMethodReport.class, 5, "first method", 1, 5);
        mainTestClassReport.getTestMethodReports().addAll(firstMethods);
        // and config
        List<ConfigurationReport> firstConfigs =
            Utils.prepareSetOfReports(ConfigurationReport.class, 5, "first config", 1, 5);
        mainTestClassReport.getConfiguration().getSubReports().addAll(firstConfigs);

        // prepare report to merge
        TestClassReport testClassToMerge = Utils.prepareReport(TestClassReport.class, "to merge", 5, 10);
        // add test methods
        List<TestMethodReport> methodsToMerge =
            Utils.prepareSetOfReports(TestMethodReport.class, 5, "second method", 5, 10);
        testClassToMerge.getTestMethodReports().addAll(methodsToMerge);
        // and config
        List<ConfigurationReport> configsToMerge =
            Utils.prepareSetOfReports(ConfigurationReport.class, 5, "second config", 5, 10);
        testClassToMerge.getConfiguration().getSubReports().addAll(configsToMerge);

        //merge
        mainTestClassReport.merge(testClassToMerge);

        // the report that has been merged is still same
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(testClassToMerge)
                .hasName("to merge")
                .hasGeneratedSubreportsAndEntries(5, 10)
                .hasNumberOfSubreportsAndEntries(5);

            assertThat(testClassToMerge.getTestMethodReports()).isEqualTo(methodsToMerge);
            assertThat(testClassToMerge.getConfiguration().getSubReports()).isEqualTo(configsToMerge);
        });

        // the main report should contain all information
        firstMethods.addAll(methodsToMerge);
        firstConfigs.addAll(configsToMerge);

        //verify
        SoftAssertions.assertSoftly(softly -> {
            assertThatReport(mainTestClassReport)
                .hasName(TEST_CLASS_NAME)
                .hasGeneratedSubreportsAndEntries(1, 10)
                .hasNumberOfSubreportsAndEntries(9);

            assertThat(mainTestClassReport.getTestMethodReports()).isEqualTo(firstMethods);
            assertThat(mainTestClassReport.getConfiguration().getSubReports()).isEqualTo(firstConfigs);
        });
    }
}
