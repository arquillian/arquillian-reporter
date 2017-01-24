package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FailureReport extends AbstractReport<FailureReport, ReportBuilder> {

    public FailureReport() {
    }

    public FailureReport(StringKey name) {
        super(name);
    }

    @Override
    public Class<ReportBuilder> getReportBuilderClass() {
        return ReportBuilder.class;
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
