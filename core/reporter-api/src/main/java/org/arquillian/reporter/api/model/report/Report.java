package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.ReportBuilder;
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
        getSubReports().add(newReport);
        return newReport;
    }

    public Class<ReportBuilder> getReportBuilderClass() {
        return ReportBuilder.class;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Report that = (Report) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (getEntries() != null ? !getEntries().equals(that.getEntries()) : that.getEntries() != null)
            return false;
        if (getSubReports() != null ? !getSubReports().equals(that.getSubReports()) : that.getSubReports() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getEntries() != null ? getEntries().hashCode() : 0);
        result = 31 * result + (getSubReports() != null ? getSubReports().hashCode() : 0);
        return result;
    }
}
