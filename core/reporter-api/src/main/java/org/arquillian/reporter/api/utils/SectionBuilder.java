package org.arquillian.reporter.api.utils;

import java.util.Map;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.SectionReport;
import org.arquillian.reporter.api.model.entry.Entry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface SectionBuilder {

    SectionReport build();

    SectionBuilder addEntry(Entry entry);

    SectionBuilder setIdentifier(String identifier);

    SectionBuilder feedKeyValueListFromMap(Map<String, String> keyValueMap);

    SectionBuilder addSection(SectionBuilder sectionBuilder);

    SectionBuilder addSection(SectionReport sectionBuilder);

    SectionBuilder addKeyValueEntry(String key, Entry value);

    SectionBuilder addKeyValueEntry(String key, String value);

    SectionBuilder addKeyValueEntry(String key, int value);

    SectionBuilder addKeyValueEntry(String s, boolean runAsClient);

    SectionInEvent fireUsingEvent(ReportEvent reportEvent);
}
