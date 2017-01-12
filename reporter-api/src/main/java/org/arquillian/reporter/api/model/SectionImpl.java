package org.arquillian.reporter.api.model;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.utils.Validate;

import static org.arquillian.reporter.api.utils.SectionModifier.merge;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionImpl implements Section {

    private String name;

    private List<Entry> entries = new ArrayList<>();

    private List<Section> sections = new ArrayList<>();

    private String identifier;

    public SectionImpl(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        if (Validate.isNotEmpty(section.getIdentifier())) {
            Section originalSection = sections
                .stream()
                .filter(ss ->
                            Validate.isNotEmpty(ss.getIdentifier())
                                && ss.getIdentifier().equals(section.getIdentifier()))
                .findFirst()
                .get();
            if (originalSection != null) {
                merge(originalSection, section);
                return;
            }
        }
        sections.add(section);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
