package org.arquillian.reporter.api.model.report;

import java.util.List;

import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface Report<TYPE extends Report, UTILS extends ReportBuilder> {

    StringKey getName();

    void setName(StringKey name);

    List<Entry> getEntries();

    void setEntries(List<Entry> entries);

    List<Report> getSubReports();

    void setSubReports(List<Report> subReports);

    Class<UTILS> getReportBuilderClass();

    TYPE merge(TYPE newReport);

    Report addNewReport(Report newReport);

}
