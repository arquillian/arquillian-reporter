package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.builder.report.ReportBuilder;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class AbstractReport<TYPE extends AbstractReport, UTILS extends ReportBuilder> {

    private StringKey name;

    private List<Entry> entries = new ArrayList<>();

    private List<AbstractReport> subReports = new ArrayList<>();

    public AbstractReport(){
    }

    public AbstractReport(StringKey name) {
        this.name = name;
    }

    public StringKey getName() {
        return name;
    }

    public void setName(StringKey name) {
        this.name = name;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public List<AbstractReport> getSubReports() {
        return subReports;
    }

    public void setSubReports(List<AbstractReport> subReports) {
        this.subReports = subReports;
    }

    public void defaultMerge(TYPE newReport){
        if (newReport == null){
            return;
        }
        getEntries().addAll(newReport.getEntries());
        getSubReports().addAll(newReport.getSubReports());
    }

    public abstract Class<UTILS> getReportBuilderClass();

    public abstract TYPE merge(TYPE newReport);

    public abstract AbstractReport addNewReport(AbstractReport newReport);

}
