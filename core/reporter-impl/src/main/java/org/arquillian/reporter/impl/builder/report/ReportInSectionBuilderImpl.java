package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.ReportInSectionBuilder;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.jboss.arquillian.core.api.Event;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportInSectionBuilderImpl<REPORTTYPE extends AbstractReport, SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORTTYPE, ? extends SectionEvent>>
    implements ReportInSectionBuilder<REPORTTYPE, SECTIONTYPE> {

    private REPORTTYPE report;
    private SECTIONTYPE sectionEvent;

    public ReportInSectionBuilderImpl(REPORTTYPE report, SECTIONTYPE sectionEvent) {
        this.report = report;
        this.sectionEvent = sectionEvent;
    }

    @Override
    public SECTIONTYPE fire(Event<SectionEvent> fireEventExecutor) {
        sectionEvent.setReport(report);
        fireEventExecutor.fire(sectionEvent);
        return sectionEvent;
    }
}
