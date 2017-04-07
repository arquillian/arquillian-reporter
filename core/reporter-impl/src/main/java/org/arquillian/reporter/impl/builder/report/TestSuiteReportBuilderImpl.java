package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.report.TestSuiteReportBuilder;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.api.utils.ReporterUtils;

/**
 * An implementation of {@link TestSuiteReportBuilder} used for building {@link TestSuiteReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReportBuilderImpl extends AbstractReportBuilder<TestSuiteReportBuilder, TestSuiteReport>
    implements TestSuiteReportBuilder {

    public TestSuiteReportBuilderImpl(TestSuiteReport sectionReport) {
        super(sectionReport);
    }

    public TestSuiteReportBuilderImpl stop() {
        getReport().setExecutionStopTime(ReporterUtils.getCurrentDate());
        return this;
    }
}
