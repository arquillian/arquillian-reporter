package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.ReportInSectionBuilder;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.jboss.arquillian.core.api.Event;

/**
 * An implementation of {@link ReportInSectionBuilder} used for setting report into a {@link SectionEvent}
 *
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
    public ReportInSectionBuilder<REPORTTYPE, SECTIONTYPE> asSubReport() {
        sectionEvent.setContainsSubReport(true);
        return this;
    }

    @Override
    public SECTIONTYPE fire(Event<SectionEvent> injectedSectionEvent) {
        sectionEvent.setReport(report);
        injectedSectionEvent.fire(sectionEvent);
        return sectionEvent;
    }
}
