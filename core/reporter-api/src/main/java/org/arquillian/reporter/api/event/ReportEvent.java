package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.Section;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class ReportEvent<PAYLOAD_TYPE extends Section, PARENT_TYPE extends ReportEvent> {

    private PAYLOAD_TYPE sectionReport;
    private String identifier;
    private PARENT_TYPE parentEvent;
    private boolean processed = false;

    public ReportEvent(PAYLOAD_TYPE sectionReport) {
        this.sectionReport = sectionReport;
    }

    public ReportEvent(String identifier) {
        this.identifier = identifier;
    }

    public ReportEvent(PAYLOAD_TYPE sectionReport, String identifier) {
        this.sectionReport = sectionReport;
        this.identifier = identifier;
    }

    public PAYLOAD_TYPE getSectionReport() {
        return sectionReport;
    }

    public void setSectionReport(PAYLOAD_TYPE sectionReport) {
        this.sectionReport = sectionReport;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public PARENT_TYPE getParentEvent() {
        return parentEvent;
    }

    public ReportEvent setParentEvent(PARENT_TYPE parentEvent) {
        this.parentEvent = parentEvent;
        return this;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
