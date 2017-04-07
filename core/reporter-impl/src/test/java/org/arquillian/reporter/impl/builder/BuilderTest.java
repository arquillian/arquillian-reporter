package org.arquillian.reporter.impl.builder;

import org.arquillian.reporter.api.builder.BuilderRegistryDelegate;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.builder.report.TestClassReportBuilder;
import org.arquillian.reporter.api.builder.report.TestMethodReportBuilder;
import org.arquillian.reporter.api.builder.report.TestSuiteReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.StringEntry;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.asserts.TestMethodReportAssert;
import org.arquillian.reporter.impl.base.AbstractReporterTestBase;
import org.arquillian.reporter.impl.model.report.DateChecker;
import org.jboss.arquillian.core.impl.ExceptionHandlingTestCase;
import org.jboss.arquillian.test.spi.TestResult;
import org.junit.Test;

import java.text.ParseException;
import java.util.List;

import static org.arquillian.reporter.api.model.ReporterCoreKey.METHOD_FAILURE_REPORT;
import static org.arquillian.reporter.api.model.ReporterCoreKey.METHOD_FAILURE_REPORT_STACKTRACE;
import static org.arquillian.reporter.api.utils.ReporterUtils.getHumanReadableStackTrace;
import static org.arquillian.reporter.impl.asserts.ReportAssert.assertThatReport;

public class BuilderTest extends AbstractReporterTestBase {

    @Test
    public void should_verify_stop_for_test_class_report() throws ParseException {
        TestClassReportBuilder builder = Reporter
                .createReport(new TestClassReport("Report name"))
                .addEntries("entry");

        new DateChecker() {
            @Override
            public String getDateFromReport() {
                TestClassReport report = builder.stop().build();
                verifyBasicContent(report);
                return report.getExecutionStopTime();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void should_verify_stop_for_test_suite_report() throws ParseException {
        TestSuiteReportBuilder builder = Reporter
                .createReport(new TestSuiteReport("Report name"))
                .addEntries("entry");

        new DateChecker() {
            @Override
            public String getDateFromReport() {
                TestSuiteReport report = builder.stop().build();
                verifyBasicContent(report);
                return report.getExecutionStopTime();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void should_verify_stop_for_test_method_report() throws ParseException {
        TestMethodReportBuilder builder = Reporter
                .createReport(new TestMethodReport("Report name"))
                .addEntries("entry");

        new DateChecker() {
            @Override
            public String getDateFromReport() {
                TestMethodReport report = builder.stop().build();
                verifyBasicContent(report);
                return report.getExecutionStopTime();
            }
        }.assertThatDateWasCorrectlyCreated();
    }

    @Test
    public void should_verify_failed_result_for_test_method_report() throws ParseException {
        ExceptionHandlingTestCase.TestException exception = new ExceptionHandlingTestCase.TestException("cause");
        TestResult result = TestResult.failed(exception);

        TestMethodReport report = Reporter
                .createReport(new TestMethodReport("Report name"))
                .addEntries("entry")
                .setResult(result)
                .build();

        verifyBasicContent(report);

         FailureReport failureReport = Reporter.createReport(new FailureReport(METHOD_FAILURE_REPORT))
            .addKeyValueEntry(METHOD_FAILURE_REPORT_STACKTRACE, getHumanReadableStackTrace(result.getThrowable()))
            .build();

        // TODO: verify failure Report Generation - failure report not a sub report
        TestMethodReportAssert
            .assertThatTestMethodReport(report)
            .hasFailureReportsContainingExactly(failureReport)
            .hasStatus(TestResult.Status.FAILED);
    }

    // todo add verification for throwable when it is supported by core
    @Test
    public void should_verify_skipped_result_for_test_method_report() throws ParseException {
        ExceptionHandlingTestCase.TestException exception = new ExceptionHandlingTestCase.TestException("cause");
        TestResult result = TestResult.skipped(exception);

        TestMethodReport report = Reporter
                .createReport(new TestMethodReport("Report name"))
                .addEntries("entry")
                .setResult(result)
                .build();

        verifyBasicContent(report);

        // todo change this when throwable is supported
//        FailureReport failureReport = Reporter.createReport(new FailureReport(METHOD_FAILURE_REPORT))
//                .addKeyValueEntry(METHOD_FAILURE_REPORT_STACKTRACE, getHumanReadableStackTrace(exception))
//                .build();

        TestMethodReportAssert
                .assertThatTestMethodReport(report)
                .hasFailureSubReportsContainingExactly()
                .hasStatus(TestResult.Status.SKIPPED);
    }

    // todo clarify this
    @Test
    public void should_verify_passed_result_for_test_method_report() throws ParseException {
        ExceptionHandlingTestCase.TestException exception = new ExceptionHandlingTestCase.TestException("cause");
        TestResult result = TestResult.passed();
        result.setThrowable(exception);

        TestMethodReport report = Reporter
                .createReport(new TestMethodReport("Report name"))
                .addEntries("entry")
                .setResult(result)
                .build();

        verifyBasicContent(report);

        TestMethodReportAssert
                .assertThatTestMethodReport(report)
                .hasFailureSubReportsContainingExactly()
                .hasStatus(TestResult.Status.PASSED);
    }


    private void verifyBasicContent(Report report) {
        assertThatReport(report)
                .hasName("Report name")
                .hasNumberOfEntries(1)
                .hasEntriesContaining(new StringEntry("entry"));
    }

    @Override
    protected void addAdditionalExtensions(List<Class<?>> extensions) {

    }

    @Override
    protected void addReporterStringKeys(List<StringKey> stringKeys) {

    }

    @Override
    protected void registerBuilders(BuilderRegistryDelegate builderRegistry) {

    }
}
