package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.utils.ReporterUtils;
import org.arquillian.reporter.api.model.report.AbstractReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class SectionEvent<SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORT_TYPE, PARENT_TYPE>, REPORT_TYPE extends AbstractReport, PARENT_TYPE extends SectionEvent> {

    private REPORT_TYPE report;
    private String sectionId;
    private boolean processed = false;

    public SectionEvent() {
    }

    public SectionEvent(REPORT_TYPE report) {
        this.report = report;
    }

    public SectionEvent(String... sectionIdParams) {
        sectionId = ReporterUtils.buildId(sectionIdParams);
    }

    public SectionEvent(REPORT_TYPE report, String... sectionIdParams) {
        this.report = report;
        sectionId = ReporterUtils.buildId(sectionIdParams);
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

    public abstract Class<REPORT_TYPE> getReportTypeClass();

    public Identifier<SECTIONTYPE> identifyYourself() {
        return new Identifier<SECTIONTYPE>((Class<SECTIONTYPE>) this.getClass(), getSectionId());
    }

}
