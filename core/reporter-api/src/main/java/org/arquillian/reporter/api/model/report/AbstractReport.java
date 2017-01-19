package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.builder.ReportBuilder;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class AbstractReport<TYPE extends AbstractReport, UTILS extends ReportBuilder> {

    private StringKey name;

    private List<Entry> entries = new ArrayList<>();

    private List<AbstractReport> subreports = new ArrayList<>();

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

    public List<AbstractReport> getSubreports() {
        return subreports;
    }

    public void setSubreports(List<AbstractReport> subreports) {
        this.subreports = subreports;
    }

    public void defaultMerge(TYPE newReport){
        if (newReport == null){
            return;
        }
        getEntries().addAll(newReport.getEntries());
        getSubreports().addAll(newReport.getSubreports());
    }

    public abstract UTILS getReportBuilderClass();

    public abstract TYPE merge(TYPE newReport);

    public abstract AbstractReport addNewReport(AbstractReport newReport);
}
