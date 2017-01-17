package org.arquillian.reporter.api.builder;

import org.arquillian.reporter.api.event.ReportNodeEvent;
import org.arquillian.reporter.api.model.report.AbstractSectionReport;
import org.jboss.arquillian.core.api.Event;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface SectionInNode<SECTIONTYPE extends AbstractSectionReport, NODETYPE extends ReportNodeEvent<SECTIONTYPE, ? extends ReportNodeEvent>> {

     NODETYPE report(Event<ReportNodeEvent> reportEvent);
}
