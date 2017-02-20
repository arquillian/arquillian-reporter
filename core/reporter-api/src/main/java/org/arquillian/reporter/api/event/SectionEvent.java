package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.utils.ReporterUtils;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class SectionEvent<SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORTTYPE, PARENTSECTIONTYPE>, REPORTTYPE extends Report, PARENTSECTIONTYPE extends SectionEvent> {

    private REPORTTYPE report;
    private String sectionId;
    private boolean processed = false;

    public SectionEvent() {
    }

    public SectionEvent(REPORTTYPE report) {
        this.report = report;
    }

    public SectionEvent(String... sectionIdParams) {
        sectionId = ReporterUtils.buildId(sectionIdParams);
    }

    public SectionEvent(REPORTTYPE report, String... sectionIdParams) {
        this.report = report;
        sectionId = ReporterUtils.buildId(sectionIdParams);
    }

    public REPORTTYPE getReport() {
        return report;
    }

    public void setReport(REPORTTYPE report) {
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

    public abstract PARENTSECTIONTYPE getParentSectionThisSectionBelongsTo();

    public abstract Class<REPORTTYPE> getReportTypeClass();

    public Identifier<SECTIONTYPE> identifyYourself() {
        return new Identifier<SECTIONTYPE>((Class<SECTIONTYPE>) this.getClass(), getSectionId());
    }

}
