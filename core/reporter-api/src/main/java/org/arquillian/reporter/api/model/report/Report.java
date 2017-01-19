package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.impl.ReportBuilderImpl;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Report extends AbstractReport<Report,ReportBuilderImpl> {

    public Report() {
    }

    public Report(StringKey name) {
        super(name);
    }

    public Report merge(Report newReport) {
        defaultMerge(newReport);
        return this;
    }

    @Override
    public AbstractReport addNewReport(AbstractReport newReport) {
        getSubreports().add(newReport);
        return newReport;
    }

    public ReportBuilderImpl getReportBuilderClass() {
        return new ReportBuilderImpl(this);
    }

}
