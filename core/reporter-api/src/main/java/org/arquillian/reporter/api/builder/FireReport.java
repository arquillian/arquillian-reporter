package org.arquillian.reporter.api.builder;

import org.arquillian.reporter.api.event.SectionEvent;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface FireReport {

    ReportInSectionBuilder usingEvent(SectionEvent event);
}
