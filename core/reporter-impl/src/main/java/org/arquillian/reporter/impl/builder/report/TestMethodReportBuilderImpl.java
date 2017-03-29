package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.builder.report.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.report.TestMethodReportBuilder;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.jboss.arquillian.test.spi.TestResult;

import static org.arquillian.reporter.api.model.ReporterCoreKey.METHOD_FAILURE_REPORT;
import static org.arquillian.reporter.api.model.ReporterCoreKey.METHOD_FAILURE_REPORT_STACKTRACE;
import static org.arquillian.reporter.api.utils.ReporterUtils.getHumanReadableStackTrace;

/**
 * An implementation of {@link TestMethodReportBuilder} used for building {@link TestMethodReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestMethodReportBuilderImpl extends AbstractReportBuilder<TestMethodReportBuilder, TestMethodReport>
    implements TestMethodReportBuilder {

    public TestMethodReportBuilderImpl(TestMethodReport sectionReport) {
        super(sectionReport);
    }

    public TestMethodReportBuilderImpl stop() {
        getReport().setStop(ReporterUtils.getCurrentDate());
        return this;
    }

    // todo implement support for throwable in case of skipped
    public TestMethodReportBuilderImpl setResult(TestResult result) {
        if (result.getStatus() == TestResult.Status.FAILED && result.getThrowable() != null) {
            String stackTrace = getHumanReadableStackTrace(result.getThrowable());
            FailureReport failureReport = new FailureReport(METHOD_FAILURE_REPORT);
            Reporter.createReport(failureReport).addKeyValueEntry(METHOD_FAILURE_REPORT_STACKTRACE, stackTrace);
            getReport().setFailureReport(failureReport);
        }
        getReport().setStatus(result.getStatus());
        return this;
    }
}
