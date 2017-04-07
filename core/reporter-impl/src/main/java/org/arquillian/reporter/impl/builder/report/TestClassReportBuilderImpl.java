package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.report.TestClassReportBuilder;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.utils.ReporterUtils;

/**
 * An implementation of {@link TestClassReportBuilder} used for building {@link TestClassReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReportBuilderImpl extends AbstractReportBuilder<TestClassReportBuilder, TestClassReport>
    implements TestClassReportBuilder {

    public TestClassReportBuilderImpl(TestClassReport sectionReport) {
        super(sectionReport);
    }

    public TestClassReportBuilderImpl stop() {
        getReport().setExecutionStopTime(ReporterUtils.getCurrentDate());
        return this;
    }
}
