package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.AbstractSectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class ReportNodeEvent<PAYLOAD_TYPE extends AbstractSectionReport, PARENT_TYPE extends ReportNodeEvent> {

    private PAYLOAD_TYPE section;
    private String identifier;
    private PARENT_TYPE parentEvent;
    private boolean processed = false;

    public ReportNodeEvent(){
    }

    public ReportNodeEvent(PAYLOAD_TYPE section) {
        this.section = section;
    }

    public ReportNodeEvent(String identifier) {
        this.identifier = identifier;
    }

    public ReportNodeEvent(PAYLOAD_TYPE section, String identifier) {
        this.section = section;
        this.identifier = identifier;
    }

    public PAYLOAD_TYPE getSection() {
        return section;
    }

    public void setSection(PAYLOAD_TYPE section) {
        this.section = section;
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

    public ReportNodeEvent setParentEvent(PARENT_TYPE parentEvent) {
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
