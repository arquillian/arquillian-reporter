package org.arquillian.reporter.api.model;

import java.util.List;

import org.arquillian.reporter.api.model.entry.Entry;
import org.arquillian.reporter.api.utils.SectionBuilder;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface Section<TYPE extends Section, UTILS extends SectionBuilder> {

    TYPE merge(TYPE newSection);

    String getName();

    void setName(String name);

    List<Entry> getEntries();

    void setEntries(List<Entry> entries);

    List<Section> getSectionReports();

    void setSectionReports(List<Section> sectionReports);

    String getIdentifier();

    void setIdentifier(String identifier);

    Class<? extends UTILS> getSectionBuilderClass();
}
