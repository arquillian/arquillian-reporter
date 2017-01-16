package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.Configuration;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportEventTestSuiteConfiguration extends ReportEvent<Configuration, ReportEventTestSuite> {

    public ReportEventTestSuiteConfiguration(Configuration sectionReport) {
        super(sectionReport);
    }

    public ReportEventTestSuiteConfiguration(String identifier) {
        super(identifier);
    }

    public ReportEventTestSuiteConfiguration(Configuration sectionReport, String identifier) {
        super(sectionReport, identifier);
    }
}
