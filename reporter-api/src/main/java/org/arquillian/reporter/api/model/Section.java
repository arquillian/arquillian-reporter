package org.arquillian.reporter.api.model;

import java.util.List;

import org.arquillian.reporter.api.model.entry.Entry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface Section {

    void addSection(Section section);

    List<Section> getSections();

    void addEntry(Entry entry);

    String getName();

    String getIdentifier();

    void setIdentifier(String identifier);

    List<Entry> getEntries();

    void setName(String name);
}
