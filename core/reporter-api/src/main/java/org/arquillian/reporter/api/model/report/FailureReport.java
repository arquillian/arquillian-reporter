package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.ReportBuilder;
import org.arquillian.reporter.api.builder.impl.ReportBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FailureReport extends AbstractReport<FailureReport, ReportBuilder> {

    public FailureReport() {
    }

    public FailureReport(String name) {
        super(name);
    }

    @Override
    public ReportBuilder getReportBuilderClass() {
        return new ReportBuilderImpl(this);
    }

    @Override
    public FailureReport merge(FailureReport newReport) {
        defaultMerge(newReport);
        return this;
    }

    @Override
    public AbstractReport addNewReport(AbstractReport newReport) {
        getSubreports().add(newReport);
        return newReport;
    }
}
