package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportTestSuiteConfigurationEvent extends ReportEvent {

    public ReportTestSuiteConfigurationEvent(SectionReport sectionReport) {
        super(sectionReport);
    }

    public ReportTestSuiteConfigurationEvent(String identifierSuffix) {
        super(identifierSuffix);
    }

    public ReportTestSuiteConfigurationEvent(SectionReport sectionReport, String identifierSuffix) {
        super(sectionReport, identifierSuffix);
    }
}
