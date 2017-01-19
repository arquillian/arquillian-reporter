package org.arquillian.reporter.impl;

import org.arquillian.reporter.ExecutionReport;
import org.arquillian.reporter.api.event.SectionEvent;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ExecutionSection extends SectionEvent<ExecutionSection, ExecutionReport, SectionEvent> {

    public ExecutionSection() {
        super("executionId");
    }

    public ExecutionSection(ExecutionReport report) {
        super(report, "executionId");
    }

    @Override
    public SectionEvent getParentSectionThisSectionBelongsTo() {
        return null;
    }
}
