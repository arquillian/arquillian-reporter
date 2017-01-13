package org.arquillian.reporter.api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.SectionReport;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionBuilderImpl implements SectionBuilder {


    private final SectionReport sectionReport;
    private List<SectionBuilder> subsections = new ArrayList<>();

    public SectionBuilderImpl(String name){
        sectionReport = new SectionReport(name);
    }

    public SectionBuilderImpl(SectionReport sectionReport) {
        this.sectionReport = sectionReport;
    }

    public SectionBuilder setIdentifier(String identifier){
        sectionReport.setIdentifier(identifier);
        return this;
    }

    public SectionBuilder feedKeyValueListFromMap(Map<String, String> keyValueMap){
        SectionModifier.feedKeyValueListFromMap(sectionReport, keyValueMap);
        return this;
    }

    public SectionBuilder addSection(SectionBuilder section){
        subsections.add(section);
        return this;
    }

    public SectionBuilder addEntry(Entry entry){
        sectionReport.getEntries().add(entry);
        return this;
    }

    public SectionBuilderImpl addKeyValueEntry(String key, Entry value){
        sectionReport.getEntries().add(new KeyValueEntry(key, value));
        return this;
    }

    public SectionBuilderImpl addKeyValueEntry(String key, String value){
        sectionReport.getEntries().add(new KeyValueEntry(key, value));
        return this;
    }

    public SectionBuilderImpl addKeyValueEntry(String key, int value){
        addKeyValueEntry(key, String.valueOf(value));
        return this;
    }

    @Override public SectionBuilder addKeyValueEntry(String key, boolean value) {
        addKeyValueEntry(key, String.valueOf(value));
        return this;
    }

    public SectionBuilder addSection(SectionReport newSection) {
        // todo should we take care of merging?
        sectionReport.getSectionReports().add(newSection);
        return this;
    }

    public SectionReport build(){
        subsections.forEach(builder -> addSection(builder.build()));
        return sectionReport;
    }

    public SectionInEvent fireUsingEvent(ReportEvent event){
        return new SectionInEventImpl(build(), event);
    }

}
