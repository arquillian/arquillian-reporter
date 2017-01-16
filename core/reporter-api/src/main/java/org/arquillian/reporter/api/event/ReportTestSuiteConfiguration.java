package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.Configuration;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportTestSuiteConfiguration extends ReportEvent<Configuration, ReportTestSuite> {

    public ReportTestSuiteConfiguration(Configuration sectionReport) {
        super(sectionReport);
    }

    public ReportTestSuiteConfiguration(String identifier) {
        super(identifier);
    }

    public ReportTestSuiteConfiguration(Configuration sectionReport, String identifier) {
        super(sectionReport, identifier);
    }
}
