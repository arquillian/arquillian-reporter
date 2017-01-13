package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportEvent<T extends SectionReport>  {

    private T sectionReport;
    private String identifier;
    private ReportEvent parentEvent;

    public ReportEvent(T sectionReport) {
        this.sectionReport = sectionReport;
    }

    public ReportEvent(String identifier) {
        this.identifier = identifier;
    }

    public ReportEvent(T sectionReport, String identifier) {
        this.sectionReport = sectionReport;
        this.identifier = identifier;
    }

    public T getSectionReport() {
        return sectionReport;
    }

    public void setSectionReport(T sectionReport) {
        this.sectionReport = sectionReport;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public ReportEvent getParentEvent() {
        return parentEvent;
    }

    public void setParentEvent(ReportEvent parentEvent) {
        this.parentEvent = parentEvent;
    }
}
