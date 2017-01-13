package org.arquillian.reporter.api.utils;

import java.util.Map;

import org.arquillian.reporter.api.model.SectionReport;
import org.arquillian.reporter.api.model.entry.Entry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface Section {

    Section addEntry(Entry entry);

    Section setIdentifier(String identifier);

    SectionReport build();

    Section feedKeyValueListFromMap(Map<String, String> keyValueMap);

    Section addSection(Section section);

    Section addSection(SectionReport section);

    Section addKeyValueEntry(String key, Entry value);

    Section addKeyValueEntry(String key, String value);

    Section addKeyValueEntry(String key, int value);

    Section addKeyValueEntry(String s, boolean runAsClient);
}
