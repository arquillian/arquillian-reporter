package org.arquillian.reporter.api.builder.report;

import org.arquillian.reporter.api.model.report.TestMethodReport;
import org.jboss.arquillian.test.spi.TestResult;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface TestMethodReportBuilder extends ReportBuilder<TestMethodReportBuilder, TestMethodReport> {

    TestMethodReportBuilder stop();

    TestMethodReportBuilder setResult(TestResult result);
}
