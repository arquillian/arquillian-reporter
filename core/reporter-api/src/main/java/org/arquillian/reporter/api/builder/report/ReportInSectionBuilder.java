package org.arquillian.reporter.api.builder.report;

import org.arquillian.reporter.api.builder.Builder;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.Report;
import org.jboss.arquillian.core.api.Event;

/**
 * Builder for firing a {@link SectionEvent}
 *
 * @param <REPORTTYPE>  Type of {@link Report} the set {@link SectionEvent} should carry
 * @param <SECTIONTYPE> Type of {@link SectionEvent} to be fired
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface ReportInSectionBuilder<REPORTTYPE extends Report, SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORTTYPE, ? extends SectionEvent>>
    extends
    Builder {

    /**
     * Fire an arquillian {@link SectionEvent} that was set in previous step
     *
     * @param injectedSectionEvent A {@link Event<SectionEvent>} to be used for firing
     * @return The fired {@link SectionEvent} that was set in previous step
     */
    SECTIONTYPE fire(Event<SectionEvent> injectedSectionEvent);

}
