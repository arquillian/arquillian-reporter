package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.Section;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FireSectionImpl implements FireSection{

    private Section section;

    public FireSectionImpl(Section section){
        this.section = section;
    }

    @Override
    public SectionInEvent usingEvent(ReportEvent event) {
        return new SectionInEventImpl(section, event);
    }
}
