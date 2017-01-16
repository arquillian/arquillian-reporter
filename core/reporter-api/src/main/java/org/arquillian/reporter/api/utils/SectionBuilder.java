package org.arquillian.reporter.api.utils;

import java.util.Map;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.AbstractSectionReport;
import org.arquillian.reporter.api.model.entry.Entry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface SectionBuilder<SECTIONTYPE extends AbstractSectionReport<SECTIONTYPE, ? extends SectionBuilder>, BUILDERTYPE extends SectionBuilder> {

    SECTIONTYPE build();

    BUILDERTYPE addEntry(Entry entry);

    BUILDERTYPE setIdentifier(String identifier);

    BUILDERTYPE feedKeyValueListFromMap(Map<String, String> keyValueMap);

    BUILDERTYPE addSection(BUILDERTYPE sectionBuilder);

    BUILDERTYPE addSection(SECTIONTYPE section);

    BUILDERTYPE addKeyValueEntry(String key, Entry value);

    BUILDERTYPE addKeyValueEntry(String key, String value);

    BUILDERTYPE addKeyValueEntry(String key, int value);

    BUILDERTYPE addKeyValueEntry(String s, boolean runAsClient);

    SectionInEvent fireUsingEvent(ReportEvent reportEvent);
}
