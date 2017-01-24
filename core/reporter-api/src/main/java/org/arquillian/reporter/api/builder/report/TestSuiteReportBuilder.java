package org.arquillian.reporter.api.builder.report;

import org.arquillian.reporter.api.model.report.TestSuiteReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface TestSuiteReportBuilder extends ReportBuilder<TestSuiteReport, TestSuiteReportBuilder> {

    TestSuiteReportBuilder stop();
}
