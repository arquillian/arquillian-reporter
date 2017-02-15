package org.arquillian.reporter.api.builder.report;

import org.arquillian.reporter.api.builder.Builder;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.Report;
import org.jboss.arquillian.core.api.Event;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface ReportInSectionBuilder<REPORTTYPE extends Report, SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORTTYPE, ? extends SectionEvent>> extends
    Builder {

     SECTIONTYPE fire(Event<SectionEvent> reportEvent);

}
