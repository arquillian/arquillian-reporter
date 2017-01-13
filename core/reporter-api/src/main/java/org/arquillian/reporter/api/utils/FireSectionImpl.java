package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FireSectionImpl implements FireSection{

    private SectionReport sectionReport;

    public FireSectionImpl(SectionReport sectionReport){
        this.sectionReport = sectionReport;
    }

    @Override
    public SectionInEvent usingEvent(ReportEvent event) {
        return new SectionInEventImpl(sectionReport, event);
    }
}
