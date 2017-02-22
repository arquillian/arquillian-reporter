package org.arquillian.reporter.api.builder.report;

import org.arquillian.reporter.api.model.report.FailureReport;
import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.jboss.arquillian.test.spi.TestResult;

/**
 * Builder for {@link TestMethodReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface TestMethodReportBuilder extends ReportBuilder<TestMethodReportBuilder, TestMethodReport> {

    /**
     * Sets the stop time of the {@link TestMethodReport} to the current time
     *
     * @return The same instance of {@link TestMethodReportBuilder} with modified {@link TestMethodReport} instance
     */
    TestMethodReportBuilder stop();

    /**
     * Sets the result status contained in the given {@link TestResult}. If there was a failure during the test,
     * then it is retrieved from the given {@link TestResult} and stored as a {@link FailureReport} in the associated {@link TestMethodReport}
     *
     * @param result A {@link TestResult} containing status and failures
     * @return The same instance of {@link TestMethodReportBuilder} with modified {@link TestMethodReport} instance
     */
    TestMethodReportBuilder setResult(TestResult result);
}
