package org.arquillian.reporter.api.model.report;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.builder.SectionBuilder;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class AbstractSectionReport<TYPE extends AbstractSectionReport, UTILS extends SectionBuilder> {

    private String name;

    private List<Entry> entries = new ArrayList<>();

    private List<AbstractSectionReport> sectionReports = new ArrayList<>();

    private String identifier;

    public AbstractSectionReport(String name) {
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

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public List<AbstractSectionReport> getSectionReports() {
        return sectionReports;
    }

    public void setSectionReports(List<AbstractSectionReport> sectionReports) {
        this.sectionReports = sectionReports;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void defaultMerge(TYPE newSection){
        getEntries().addAll(newSection.getEntries());
        getSectionReports().addAll(newSection.getSectionReports());
    }

    public abstract UTILS getSectionBuilderClass();

    public abstract TYPE merge(TYPE newSection);
}
