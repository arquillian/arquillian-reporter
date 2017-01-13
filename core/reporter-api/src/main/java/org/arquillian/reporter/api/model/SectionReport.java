package org.arquillian.reporter.api.model;

import java.util.ArrayList;
import java.util.List;

import org.arquillian.reporter.api.model.entry.Entry;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionReport {

    private String name;

    private List<Entry> entries = new ArrayList<>();

    private List<SectionReport> sectionReports = new ArrayList<>();

    private String identifier;

    public SectionReport(String name) {
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

    public List<SectionReport> getSectionReports() {
        return sectionReports;
    }

    public void setSectionReports(List<SectionReport> sectionReports) {
        this.sectionReports = sectionReports;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
