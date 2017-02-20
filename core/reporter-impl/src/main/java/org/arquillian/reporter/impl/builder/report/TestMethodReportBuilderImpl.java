package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.builder.report.TestMethodReportBuilder;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.jboss.arquillian.test.spi.TestResult;

import static org.arquillian.reporter.api.model.ReporterCoreKeys.METHOD_FAILURE_REPORT;
import static org.arquillian.reporter.api.model.ReporterCoreKeys.METHOD_FAILURE_REPORT_STACKTRACE;

/**
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

    public TestMethodReportBuilderImpl setResult(TestResult result) {
        if (result.getStatus() == TestResult.Status.FAILED && result.getThrowable() != null) {
            String stackTrace = getStackTrace(result.getThrowable());
            FailureReport failureReport = new FailureReport(METHOD_FAILURE_REPORT);
            Reporter.createReport(failureReport).addKeyValueEntry(METHOD_FAILURE_REPORT_STACKTRACE, stackTrace);
            getReport().setFailureReport(failureReport);
        }
        getReport().setStatus(result.getStatus());
        return this;
    }

    private String getStackTrace(Throwable aThrowable) {
        StringBuilder sb = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        sb.append(aThrowable.toString());
        sb.append(newLine);

        for (StackTraceElement element : aThrowable.getStackTrace()) {
            sb.append(element);
            sb.append(newLine);
        }
        return sb.toString();
    }
}
