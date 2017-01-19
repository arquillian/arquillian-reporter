package org.arquillian.reporter.api.builder.impl;

import org.arquillian.reporter.api.builder.ReportInSection;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.jboss.arquillian.core.api.Event;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportInSectionImpl<REPORTTYPE extends AbstractReport, SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORTTYPE, ? extends SectionEvent>>
    implements ReportInSection<REPORTTYPE, SECTIONTYPE> {

    private REPORTTYPE sectionReport;
    private SECTIONTYPE reportNodeEvent;

    public ReportInSectionImpl(REPORTTYPE sectionReport, SECTIONTYPE reportNodeEvent) {
        this.sectionReport = sectionReport;
        this.reportNodeEvent = reportNodeEvent;
    }

    @Override
    public SECTIONTYPE fire(Event<SectionEvent> fireEventExecutor) {
        reportNodeEvent.setReport(sectionReport);
        fireEventExecutor.fire(reportNodeEvent);
        return reportNodeEvent;
    }
}
