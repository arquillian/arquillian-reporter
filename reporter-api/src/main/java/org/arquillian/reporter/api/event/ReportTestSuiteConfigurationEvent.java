package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.Section;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportTestSuiteConfigurationEvent extends ReportEvent {

    public ReportTestSuiteConfigurationEvent(Section section) {
        super(section);
    }
}
