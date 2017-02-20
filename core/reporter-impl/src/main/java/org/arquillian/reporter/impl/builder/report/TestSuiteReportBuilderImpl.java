package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.report.TestSuiteReportBuilder;
import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.report.TestSuiteReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReportBuilderImpl extends AbstractReportBuilder<TestSuiteReportBuilder, TestSuiteReport>
    implements TestSuiteReportBuilder {

    public TestSuiteReportBuilderImpl(TestSuiteReport sectionReport) {
        super(sectionReport);
    }

    public TestSuiteReportBuilderImpl stop() {
        getReport().setStop(ReporterUtils.getCurrentDate());
        return this;
    }
}
