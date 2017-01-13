package org.arquillian.reporter.api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.model.SectionReport;
import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.model.entry.KeyValueEntry;

import static org.arquillian.reporter.api.utils.SectionModifier.merge;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionImpl implements Section {


    private final SectionReport sectionReport;
    private List<Section> subsections = new ArrayList<>();

    public SectionImpl(String name){
        this.sectionReport = new SectionReport(name);
    }

    public SectionImpl(SectionReport sectionReport) {
        this.sectionReport = sectionReport;
    }

    public Section setIdentifier(String identifier){
        sectionReport.setIdentifier(identifier);
        return this;
    }

    public Section feedKeyValueListFromMap(Map<String, String> keyValueMap){
        SectionModifier.feedKeyValueListFromMap(sectionReport, keyValueMap);
        return this;
    }

    public Section addSection(Section section){
        subsections.add(section);
        return this;
    }

//    public Section addSection(SectionReport section){
//        sectionReport.getSectionReports().add(section);
//        return this;
//    }

    public Section addEntry(Entry entry){
        sectionReport.getEntries().add(entry);
        return this;
    }

    public SectionImpl addKeyValueEntry(String key, Entry value){
        sectionReport.getEntries().add(new KeyValueEntry(key, value));
        return this;
    }

    public SectionImpl addKeyValueEntry(String key, String value){
        sectionReport.getEntries().add(new KeyValueEntry(key, value));
        return this;
    }

    public SectionImpl addKeyValueEntry(String key, int value){
        addKeyValueEntry(key, String.valueOf(value));
        return this;
    }

    @Override public Section addKeyValueEntry(String key, boolean value) {
        addKeyValueEntry(key, String.valueOf(value));
        return this;
    }

    public Section addSection(SectionReport newSection) {
        if (Validate.isNotEmpty(newSection.getIdentifier())) {
            SectionReport originalSection = sectionReport.getSectionReports()
                .stream()
                .filter(sr ->
                            Validate.isNotEmpty(sr.getIdentifier())
                                && sr.getIdentifier().equals(newSection.getIdentifier()))
                .findFirst()
                .get();
            if (originalSection != null) {
                merge(originalSection, newSection);
                return this;
            }
        }
        sectionReport.getSectionReports().add(newSection);
        return this;
    }

    public SectionReport build(){
        subsections.forEach(builder -> addSection(builder.build()));
        return sectionReport;
    }

}
