package org.arquillian.reporter.api.builder.impl;

import org.arquillian.reporter.api.builder.SectionInNode;
import org.arquillian.reporter.api.event.ReportNodeEvent;
import org.arquillian.reporter.api.model.report.AbstractSectionReport;
import org.jboss.arquillian.core.api.Event;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionInNodeImpl<SECTIONTYPE extends AbstractSectionReport, NODETYPE extends ReportNodeEvent<SECTIONTYPE, ? extends ReportNodeEvent>>
    implements SectionInNode<SECTIONTYPE, NODETYPE> {

    private SECTIONTYPE sectionReport;
    private NODETYPE reportNodeEvent;

    public SectionInNodeImpl(SECTIONTYPE sectionReport, NODETYPE reportNodeEvent) {
        this.sectionReport = sectionReport;
        this.reportNodeEvent = reportNodeEvent;
    }

    @Override
    public NODETYPE report(Event<ReportNodeEvent> fireEventExecutor) {
        reportNodeEvent.setSection(sectionReport);
        fireEventExecutor.fire(reportNodeEvent);
        return reportNodeEvent;
    }
}
