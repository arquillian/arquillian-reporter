package org.arquillian.reporter.api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.model.Section;
import org.arquillian.reporter.api.model.SectionImpl;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionBuilder {


    private final Section section;
    private List<SectionBuilder> sectionBuilders = new ArrayList<SectionBuilder>();


    private SectionBuilder(String name){
        this.section = new SectionImpl(name);
    }

    public static SectionBuilder create(String name){
        return new SectionBuilder(name);
    }

    public static SectionBuilder attachTo(String identifier, SectionBuilder sectionBuilder){
        return attachTo(identifier, sectionBuilder.build());
    }

    public static SectionBuilder attachTo(String identifier, Section section){
        SectionBuilder parentSectionBuilder = new SectionBuilder(null);
        parentSectionBuilder.withIdentifier(identifier);
        parentSectionBuilder.addSection(section);
        return parentSectionBuilder;
    }

    public static Section attachTo(Section parentSection, Section section){
        parentSection.addSection(section);
        return parentSection;
    }

    public SectionBuilder withIdentifier(String identifier){
        section.setIdentifier(identifier);
        return this;
    }

    public SectionBuilder feedKeyValueListFromMap(Map<String, String> keyValueMap){
        SectionModifier.feedKeyValueListFromMap(section, keyValueMap);
        return this;
    }

    public SectionBuilder addSection(Section section){
        section.getSections().add(section);
        return this;
    }

    public SectionBuilder addSection(SectionBuilder sectionBuilder){
        sectionBuilders.add(sectionBuilder);
        return this;
    }

    public SectionBuilder addEntry(Entry entry){
        section.getEntries().add(entry);
        return this;
    }

    public SectionBuilder addKeyValueEntry(String key, Entry value){
        section.getEntries().add(new KeyValueEntry(key, value));
        return this;
    }

    public SectionBuilder addKeyValueEntry(String key, String value){
        section.getEntries().add(new KeyValueEntry(key, value));
        return this;
    }

    public SectionBuilder addKeyValueEntry(String key, int value){
        section.getEntries().add(new KeyValueEntry(key, String.valueOf(value)));
        return this;
    }

    public Section build(){
        sectionBuilders.forEach(builder -> section.getSections().add(builder.build()));
        return section;
    }
}
