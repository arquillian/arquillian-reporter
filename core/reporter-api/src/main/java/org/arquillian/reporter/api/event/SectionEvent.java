package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.Report;

/**
 * An abstract class representing Section in general. Extending this class you create an event that represent some specific section
 *
 * @param <SECTIONTYPE>       Type of the {@link SectionEvent} implementation
 * @param <REPORTTYPE>        Type of {@link Report} that is the default payload of the event
 * @param <PARENTSECTIONTYPE> Type of the {@link SectionEvent} implementation that represents a parental section this section belongs to
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class SectionEvent<SECTIONTYPE extends SectionEvent<SECTIONTYPE, REPORTTYPE, PARENTSECTIONTYPE>, REPORTTYPE extends Report, PARENTSECTIONTYPE extends SectionEvent> {

    private REPORTTYPE report;
    private String sectionId;
    private boolean containsSubReport = false;

    /**
     * Creates an instance of {@link SectionEvent}
     */
    public SectionEvent() {
    }

    /**
     * Creates an instance of {@link SectionEvent} with the given {@link Report}
     *
     * @param report A {@link Report} to be set as its payload
     */
    public SectionEvent(REPORTTYPE report) {
        this.report = report;
    }

    /**
     * Creates an instance of {@link SectionEvent} and sets the sectionId as an id of the section
     *
     * @param sectionId An id to be used as an identifier
     */
    public SectionEvent(String sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * Creates an instance of {@link SectionEvent} with the given {@link Report} and sets the sectionId as an id of the section.
     *
     * @param report    A {@link Report} to be set as its payload
     * @param sectionId An id to be used as an identifier
     */
    public SectionEvent(REPORTTYPE report, String sectionId) {
        this.report = report;
        this.sectionId = sectionId;
    }

    /**
     * Returns the {@link Report} set in this {@link SectionEvent} as its payload
     *
     * @return The {@link Report} set in this {@link SectionEvent}
     */
    public REPORTTYPE getReport() {
        return report;
    }

    /**
     * Sets the given {@link Report} in this {@link SectionEvent} as its payload
     *
     * @param report A {@link Report} to be set
     */
    public void setReport(REPORTTYPE report) {
        this.report = report;
    }

    /**
     * Returns the id of this {@link SectionEvent}
     *
     * @return The id of this {@link SectionEvent}
     */
    public String getSectionId() {
        return sectionId;
    }

    /**
     * Sets the given unique id to this {@link SectionEvent}
     *
     * @param sectionId The id to be set
     */
    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * Returns whether the attached {@link Report} will be added into the list of sub-reports of a report that is associated
     * with this section + id
     *
     * @return whether the attached {@link Report} will be added into the list of sub-reports of a report that is associated
     * with this section + id
     */
    public boolean isContainsSubReport() {
        return containsSubReport;
    }

    /**
     * Sets whether the attached {@link Report} will be added into the list of sub-reports of a report that is associated
     * with this section + id
     *
     * @param containsSubReport Whether the attached {@link Report} will be added into the list of sub-reports of a report
     *                          that is associated with this section + id
     */
    public void setContainsSubReport(boolean containsSubReport) {
        this.containsSubReport = containsSubReport;
    }

    /**
     * Based on information that are set, creates and returns an instance of {@link SectionEvent} implementation
     * that represents a parent section this section belongs to
     *
     * @return An instance of {@link SectionEvent} implementation that represents a parent section this section belongs to
     */
    public abstract PARENTSECTIONTYPE getParentSectionThisSectionBelongsTo();

    /**
     * Returns a {@link Report} class that is default payload of this {@link SectionEvent}
     *
     * @return A {@link Report} class that is default payload of this {@link SectionEvent}
     */
    public abstract Class<REPORTTYPE> getReportTypeClass();

    /**
     * Based on information that are set, creates and returns an {@link Identifier} that identifies this {@link SectionEvent}
     *
     * @return An {@link Identifier} that identifies this {@link SectionEvent}
     */
    public Identifier<SECTIONTYPE> identifyYourself() {
        return new Identifier<SECTIONTYPE>((Class<SECTIONTYPE>) this.getClass(), getSectionId());
    }
}
