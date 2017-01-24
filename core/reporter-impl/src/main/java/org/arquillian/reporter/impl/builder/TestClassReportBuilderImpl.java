package org.arquillian.reporter.impl.builder;

import org.arquillian.reporter.api.builder.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.TestClassReportBuilder;
import org.arquillian.reporter.api.builder.Utils;
import org.arquillian.reporter.api.model.report.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassReportBuilderImpl extends AbstractReportBuilder<TestClassReport, TestClassReportBuilder> implements
    TestClassReportBuilder {

    public TestClassReportBuilderImpl(TestClassReport sectionReport) {
        super(sectionReport);
    }

    public TestClassReportBuilderImpl stop() {
        getReport().setStop(Utils.getCurrentDate());
        return this;
    }
}
