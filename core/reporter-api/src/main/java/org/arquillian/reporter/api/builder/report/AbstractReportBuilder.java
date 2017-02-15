package org.arquillian.reporter.api.builder.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.builder.Reporter;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;
import org.arquillian.reporter.api.model.report.Report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class AbstractReportBuilder<REPORTTYPE extends Report<REPORTTYPE, ? extends ReportBuilder>, BUILDERTYPE extends ReportBuilder<REPORTTYPE, BUILDERTYPE>>
    implements ReportBuilder<REPORTTYPE, BUILDERTYPE> {

    private final REPORTTYPE report;
    private List<BUILDERTYPE> reportBuilders = new ArrayList<>();

    public AbstractReportBuilder(REPORTTYPE report) {
        this.report = report;
    }

    public BUILDERTYPE feedKeyValueListFromMap(Map<String, String> keyValueMap) {
        keyValueMap.forEach((k, v) -> getReport().getEntries().add(new KeyValueEntry(new UnknownStringKey(k), v)));
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addReport(BUILDERTYPE reportBuilder) {
        reportBuilders.add(reportBuilder);
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addEntry(Entry entry) {
        report.getEntries().add(entry);
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addKeyValueEntry(StringKey key, Entry value) {
        report.getEntries().add(new KeyValueEntry(key, value));
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addKeyValueEntry(StringKey key, String value) {
        report.getEntries().add(new KeyValueEntry(key, value));
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addKeyValueEntry(String key, String value) {
        report.getEntries().add(new KeyValueEntry(new UnknownStringKey(key), value));
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addKeyValueEntry(StringKey key, int value) {
        addKeyValueEntry(key, String.valueOf(value));
        return (BUILDERTYPE) this;
    }

    @Override
    public BUILDERTYPE addKeyValueEntry(StringKey key, boolean value) {
        addKeyValueEntry(key, String.valueOf(value));
        return (BUILDERTYPE) this;
    }

    public BUILDERTYPE addReport(Report report) {
        this.report.getSubReports().add(report);
        return (BUILDERTYPE) this;
    }

    public REPORTTYPE build() {
        reportBuilders.forEach(builder -> addReport(builder.build()));
        return report;
    }

    public <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORTTYPE, ? extends SectionEvent>> ReportInSectionBuilder<REPORTTYPE, SECTIONTYPE> inSection(
        SECTIONTYPE event) {
        return Reporter.usingBuilder(ReportInSectionBuilder.class, build(), event);
    }

    protected List<BUILDERTYPE> getReportBuilders() {
        return reportBuilders;
    }

    protected REPORTTYPE getReport() {
        return report;
    }
}

