package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.utils.Validate;

/**
 * An abstract class providing a basic implementation of {@link Report} interface.
 * It is advised to extend this abstract class, then creating a brand new one.
 *
 * @param <TYPE>        A type of the {@link Report} implementation itself
 * @param <BUILDERTYPE> A {@link ReportBuilder} type of a builder that should be used for building the {@link Report} implementation
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class AbstractReport<TYPE extends AbstractReport, BUILDERTYPE extends ReportBuilder>
    implements Report<TYPE, BUILDERTYPE> {

    private StringKey name;

    private List<Entry> entries = new ArrayList<>();

    private List<Report> subReports = new ArrayList<>();

    /**
     * Creates an instance of {@link AbstractReport}
     */
    public AbstractReport() {
    }

    /**
     * Creates an instance of {@link AbstractReport} with the given {@link StringKey} set as a name
     *
     * @param name A {@link StringKey} to be set as a name
     */
    public AbstractReport(StringKey name) {
        this.name = name;
    }

    /**
     * Creates an instance of {@link AbstractReport} with the given String set as a name as {@link UnknownStringKey}
     *
     * @param name A String to be set as a name as {@link UnknownStringKey}
     */
    public AbstractReport(String name) {
        this.name = new UnknownStringKey(name);
    }

    @Override
    public StringKey getName() {
        return name;
    }

    @Override
    public void setName(StringKey name) {
        this.name = name;
    }

    @Override
    public List<Entry> getEntries() {
        return entries;
    }

    @Override
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public List<Report> getSubReports() {
        return subReports;
    }

    @Override
    public void setSubReports(List<Report> subReports) {
        this.subReports = subReports;
    }

    /**
     * Provides a default implementation of merging. Takes the entries and sub-reports contained in the given {@link Report}
     * and adds them into the list of entries and sub-reports respectively withing this instance of report.
     *
     * @param newReport A {@link Report} to be merged
     */
    public void defaultMerge(TYPE newReport) {
        if (newReport == null) {
            return;
        }
        if (getName() == null || Validate.isEmpty(getName().getValue())){
            setName(newReport.getName());
        }
        getEntries().addAll(newReport.getEntries());
        getSubReports().addAll(newReport.getSubReports());
    }

    public void processEntries(){
        entries.replaceAll(entry -> entry.outputEntry());
    }
}
