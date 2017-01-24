package org.arquillian.reporter.impl.builder;

import org.arquillian.reporter.api.builder.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.TestSuiteReportBuilder;
import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.model.report.TestSuiteReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestSuiteReportBuilderImpl extends AbstractReportBuilder<TestSuiteReport, TestSuiteReportBuilder>
    implements TestSuiteReportBuilder {

    public TestSuiteReportBuilderImpl(TestSuiteReport sectionReport) {
        super(sectionReport);
    }

    public TestSuiteReportBuilderImpl stop() {
        getReport().setStop(Utils.getCurrentDate());
        return this;
    }
}
