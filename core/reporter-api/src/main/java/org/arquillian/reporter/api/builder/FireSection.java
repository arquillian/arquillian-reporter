package org.arquillian.reporter.api.builder;

import org.arquillian.reporter.api.event.ReportNodeEvent;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface FireSection {

    SectionInNode usingEvent(ReportNodeEvent event);
}
