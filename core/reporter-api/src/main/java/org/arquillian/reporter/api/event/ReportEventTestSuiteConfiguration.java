package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportEventTestSuiteConfiguration extends ReportEvent {

    public ReportEventTestSuiteConfiguration(SectionReport sectionReport) {
        super(sectionReport);
    }

    public ReportEventTestSuiteConfiguration(String identifierSuffix) {
        super(identifierSuffix);
    }

    public ReportEventTestSuiteConfiguration(SectionReport sectionReport, String identifierSuffix) {
        super(sectionReport, identifierSuffix);
    }
}
