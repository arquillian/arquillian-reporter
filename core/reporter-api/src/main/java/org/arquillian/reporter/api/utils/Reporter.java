package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.model.AbstractSection;
import org.arquillian.reporter.api.model.Section;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Reporter {

    public static SectionBuilder section(String name){
        return new SectionBuilderImpl(new Section(name));
    }

    public static <T extends SectionBuilder, S extends AbstractSection<? extends AbstractSection, T>> T section(S sectionReport) {
        return sectionReport.getSectionBuilderClass();
    }

    public static FireSection fireSection(Section section){
        return new FireSectionImpl(section);
    }
}
