package org.arquillian.reporter.api.builder;

import java.util.Map;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.report.AbstractReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface ReportBuilder<REPORTTYPE extends AbstractReport<REPORTTYPE, ? extends ReportBuilder>, BUILDERTYPE extends ReportBuilder> {

    REPORTTYPE build();

    BUILDERTYPE addEntry(Entry entry);

    BUILDERTYPE feedKeyValueListFromMap(Map<String, String> keyValueMap);

    BUILDERTYPE addReport(BUILDERTYPE reportBuilder);

    BUILDERTYPE addReport(REPORTTYPE report);

    BUILDERTYPE addKeyValueEntry(String key, Entry value);

    BUILDERTYPE addKeyValueEntry(String key, String value);

    BUILDERTYPE addKeyValueEntry(String key, int value);

    BUILDERTYPE addKeyValueEntry(String key, boolean runAsClient);

    <SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORTTYPE, ? extends SectionEvent>> ReportInSection<REPORTTYPE, SECTIONTYPE> inSection(
        SECTIONTYPE event);
}
