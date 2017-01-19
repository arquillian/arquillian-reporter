package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.impl.ReportBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Report extends AbstractReport<Report,ReportBuilderImpl> {

    public Report(String name) {
        super(name);
    }

    public Report merge(Report newReport) {
        defaultMerge(newReport);
        return this;
    }

    @Override
    public Report addNewReport(AbstractReport newReport) {
        getSubreports().add(newReport);
        return this;
    }

    public ReportBuilderImpl getReportBuilderClass() {
        return new ReportBuilderImpl(this);
    }

}
