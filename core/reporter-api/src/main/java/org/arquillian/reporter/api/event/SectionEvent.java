package org.arquillian.reporter.api.event;

import org.arquillian.reporter.api.model.report.Report;
import org.arquillian.reporter.api.utils.ReporterUtils;

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
    private boolean processed = false;

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
     * Creates an instance of {@link SectionEvent}
     * The given sectionIdParams are processed and created a String identifier of the {@link SectionEvent}.
     * The identifier is created using all sectionIdParams joining them together using a char: '#'
     * <p>
     * The resulting identifier of two sectionIdParams 'bar' and 'foo' is: 'bar#foo'
     * </p>
     *
     * @param sectionIdParams Strings to be used for creating identifier
     */
    public SectionEvent(String... sectionIdParams) {
        sectionId = ReporterUtils.buildId(sectionIdParams);
    }

    /**
     * Creates an instance of {@link SectionEvent} with the given {@link Report}.
     * The given sectionIdParams are processed and created a String identifier of the {@link SectionEvent}.
     * The identifier is created using all sectionIdParams joining them together using a char: '#'
     * <p>
     * The resulting identifier of two sectionIdParams 'bar' and 'foo' is: 'bar#foo'
     * </p>
     *
     * @param report          A {@link Report} to be set as its payload
     * @param sectionIdParams Strings to be used for creating identifier
     */
    public SectionEvent(REPORTTYPE report, String... sectionIdParams) {
        this.report = report;
        sectionId = ReporterUtils.buildId(sectionIdParams);
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
     * Returns whether the event has been already processed
     *
     * @return Whether the event has been already processed
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * Sets whether the event has been already processed
     *
     * @param processed Whether the event has been already processed
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
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
