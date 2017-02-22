package org.arquillian.reporter.api.builder.report;

import org.arquillian.reporter.api.model.report.TestClassReport;

/**
 * Builder for {@link TestClassReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface TestClassReportBuilder extends ReportBuilder<TestClassReportBuilder, TestClassReport> {

    /**
     * Sets the stop time of the {@link TestClassReport} to the current time
     *
     * @return The same instance of {@link TestClassReportBuilder} with modified {@link TestClassReport} instance
     */
    TestClassReportBuilder stop();
}
