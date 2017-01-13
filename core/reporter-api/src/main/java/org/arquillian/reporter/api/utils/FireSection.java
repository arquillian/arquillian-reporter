package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.event.ReportEvent;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface FireSection {

    SectionInEvent usingEvent(ReportEvent event);
}
