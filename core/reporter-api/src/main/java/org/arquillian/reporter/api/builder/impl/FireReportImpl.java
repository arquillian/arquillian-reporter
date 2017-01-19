package org.arquillian.reporter.api.builder.impl;

import org.arquillian.reporter.api.builder.FireReport;
import org.arquillian.reporter.api.builder.ReportInSection;
import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.model.report.Report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FireReportImpl implements FireReport {

    private Report sectionReport;

    public FireReportImpl(Report sectionReport){
        this.sectionReport = sectionReport;
    }

    @Override
    public ReportInSection usingEvent(SectionEvent event) {
        return new ReportInSectionImpl(sectionReport, event);
    }
}
