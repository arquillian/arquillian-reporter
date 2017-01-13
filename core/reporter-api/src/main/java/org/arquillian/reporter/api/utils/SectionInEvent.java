package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.event.ReportEvent;
import org.jboss.arquillian.core.api.Event;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface SectionInEvent {

    void fire(Event<ReportEvent> reportEvent);
}
