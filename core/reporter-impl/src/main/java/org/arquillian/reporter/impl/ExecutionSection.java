package org.arquillian.reporter.impl;

import org.arquillian.reporter.api.event.SectionEvent;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionSection extends SectionEvent<ExecutionSection, ExecutionReport, SectionEvent> {

    public static final String EXECUTION_SECTION_ID = "executionId";

    public ExecutionSection() {
        super(EXECUTION_SECTION_ID);
    }

    public ExecutionSection(ExecutionReport report) {
        super(report, EXECUTION_SECTION_ID);
    }

    @Override
    public SectionEvent getParentSectionThisSectionBelongsTo() {
        return null;
    }

    @Override
    public Class<ExecutionReport> getReportTypeClass() {
        return ExecutionReport.class;
    }
}
