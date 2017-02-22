package org.arquillian.reporter.api.builder.report;

import org.arquillian.reporter.api.model.report.TestSuiteReport;

/**
 * Builder for {@link TestSuiteReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface TestSuiteReportBuilder extends ReportBuilder<TestSuiteReportBuilder, TestSuiteReport> {

    /**
     * Sets the stop time of the {@link TestSuiteReport} to the current time
     *
     * @return The same instance of {@link TestSuiteReportBuilder} with modified {@link TestSuiteReport} instance
     */
    TestSuiteReportBuilder stop();
}
