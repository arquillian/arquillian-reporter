package org.arquillian.reporter.api.builder.report;

import org.arquillian.reporter.api.builder.Builder;
import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.entry.StringEntry;
import org.arquillian.reporter.api.model.report.Report;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


/**
 * Abstract class providing a basic implementation of {@link ReportBuilder}
 *
 * @param <BUILDERTYPE> The {@link Builder} type of this implementation itself
 * @param <REPORTTYPE>  The {@link Report} type the builder should build
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class AbstractReportBuilder<BUILDERTYPE extends ReportBuilder<BUILDERTYPE, REPORTTYPE>, REPORTTYPE extends Report<REPORTTYPE, ? extends ReportBuilder>>
        implements ReportBuilder<BUILDERTYPE, REPORTTYPE> {

    private final REPORTTYPE report;
    private List<ReportBuilder> reportBuilders = new ArrayList<>();

    public AbstractReportBuilder(REPORTTYPE report) {
        this.report = report;
    }

    @Override
    public BUILDERTYPE feedKeyValueListFromMap(Map<String, String> keyValueMap) {
        keyValueMap.forEach((k, v) -> getReport().getEntries().add(new KeyValueEntry(new UnknownStringKey(k), v)));
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addReport(ReportBuilder reportBuilder) {
        reportBuilders.add(reportBuilder);
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addEntry(Entry entry) {
        report.getEntries().add(entry);
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addEntry(String entry) {
        report.getEntries().add(new StringEntry(entry));
        return (BUILDERTYPE) this;
    }


    @Override
    public BUILDERTYPE addEntries(Collection<? extends Entry> entries) {
        report.getEntries().addAll(entries);
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addEntries(String... entries) {
        Arrays.stream(entries).forEach(s -> report.getEntries().add(new StringEntry(s)));
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addEntries(Entry... entries) {
        Arrays.stream(entries).forEach(s -> report.getEntries().add(s));
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addKeyValueEntry(StringKey key, Entry value) {
        report.getEntries().add(new KeyValueEntry(key, value));
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addKeyValueEntry(StringKey key, String value) {
        report.getEntries().add(new KeyValueEntry(key, value));
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addKeyValueEntry(String key, String value) {
        report.getEntries().add(new KeyValueEntry(new UnknownStringKey(key), value));
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addKeyValueEntry(StringKey key, int value) {
        addKeyValueEntry(key, String.valueOf(value));
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addKeyValueEntry(StringKey key, boolean value) {
        addKeyValueEntry(key, String.valueOf(value));
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addReport(Report report) {
        this.report.getSubReports().add(report);
        return (BUILDERTYPE) this;
    }

    @Override
    public REPORTTYPE build() {
        reportBuilders.forEach(builder -> addReport(builder.build()));
        return report;
    }

    @Override
    public <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORTTYPE, ? extends SectionEvent>> ReportInSectionBuilder<REPORTTYPE, SECTIONTYPE> inSection(
            SECTIONTYPE event) {
        return Reporter.usingBuilder(ReportInSectionBuilder.class, build(), event);
    }

    /**
     * Returns list of added sub-report builders
     *
     * @return List of added sub-report builders
     */
    protected List<ReportBuilder> getReportBuilders() {
        return reportBuilders;
    }

    /**
     * Returns an instance of {@link Report} that is being built
     *
     * @return An instance of {@link Report} that is being built
     */
    protected REPORTTYPE getReport() {
        return report;
    }
}

