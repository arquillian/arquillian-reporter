package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.AbstractSection;
import org.jboss.arquillian.core.api.Event;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionInEventImpl<SECTIONTYPE extends AbstractSection, EVENTTYPE extends ReportEvent<SECTIONTYPE, ? extends ReportEvent>>
    implements SectionInEvent {

    private SECTIONTYPE sectionReport;
    private EVENTTYPE reportEvent;

    public SectionInEventImpl(SECTIONTYPE sectionReport, EVENTTYPE reportEvent) {
        this.sectionReport = sectionReport;
        this.reportEvent = reportEvent;
    }

    @Override
    public void fire(Event<ReportEvent> fireEventExecutor) {
        reportEvent.setSectionReport(sectionReport);
        fireEventExecutor.fire(reportEvent);
    }
}
