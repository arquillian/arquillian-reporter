package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.event.ReportEvent;
import org.arquillian.reporter.api.model.SectionReport;
import org.jboss.arquillian.core.api.Event;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionInEventImpl implements SectionInEvent {

    private SectionReport sectionReport;
    private ReportEvent reportEvent;

    public SectionInEventImpl(SectionReport sectionReport, ReportEvent reportEvent){
        this.sectionReport = sectionReport;
        this.reportEvent = reportEvent;
    }

    @Override
    public void fire(Event<ReportEvent> fireEventExecutor) {
        reportEvent.setSectionReport(sectionReport);
        fireEventExecutor.fire(reportEvent);
    }
}
