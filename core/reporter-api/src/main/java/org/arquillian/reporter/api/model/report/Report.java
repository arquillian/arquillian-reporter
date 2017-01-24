package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.ReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Report extends AbstractReport<Report,ReportBuilder> {

    public Report() {
    }

    public Report(StringKey name) {
        super(name);
    }

    public Report(String name) {
        super(new UnknownStringKey(name));
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

    public Class<ReportBuilder> getReportBuilderClass() {
        return ReportBuilder.class;
    }
}
