package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.FailureReportBuilder;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FailureReport extends AbstractReport<FailureReport, FailureReportBuilder> {

    public FailureReport() {
    }

    public FailureReport(StringKey name) {
        super(name);
    }

    @Override
    public Class<FailureReportBuilder> getReportBuilderClass() {
        return FailureReportBuilder.class;
    }

    @Override
    public FailureReport merge(FailureReport newReport) {
        defaultMerge(newReport);
        return this;
    }

    @Override
    public Report addNewReport(Report newReport) {
        getSubReports().add(newReport);
        return newReport;
    }
}
