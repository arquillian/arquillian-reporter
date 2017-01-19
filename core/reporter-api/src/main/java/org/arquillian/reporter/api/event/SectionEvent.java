package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.AbstractReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class SectionEvent<SECTIONTYPE extends SectionEvent<SECTIONTYPE , REPORT_TYPE, PARENT_TYPE>, REPORT_TYPE extends AbstractReport, PARENT_TYPE extends SectionEvent> {

    private String sectionName;
    private REPORT_TYPE report;
    private String sectionId;
//    private PARENT_TYPE parentSection;
    private boolean processed = false;

    public SectionEvent(REPORT_TYPE report) {
        this.report = report;
    }

    public SectionEvent(String sectionId) {
        this.sectionId = sectionId;
    }

    public SectionEvent(REPORT_TYPE report, String sectionId) {
        this.report = report;
        this.sectionId = sectionId;
    }

    public REPORT_TYPE getReport() {
        return report;
    }

    public void setReport(REPORT_TYPE report) {
        this.report = report;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public abstract PARENT_TYPE getParentSectionThisSectionBelongsTo();

//    public abstract String getSectionName();

    public Identifier<SECTIONTYPE> identifyYourself(){
        return new Identifier<SECTIONTYPE>((Class<SECTIONTYPE>) this.getClass(), getSectionId());
    }

}
