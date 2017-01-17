package org.arquillian.reporter.api.builder.impl;

import org.arquillian.reporter.api.builder.FireSection;
import org.arquillian.reporter.api.builder.SectionInNode;
import org.arquillian.reporter.api.event.ReportNodeEvent;
import org.arquillian.reporter.api.model.report.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FireSectionImpl implements FireSection {

    private SectionReport sectionReport;

    public FireSectionImpl(SectionReport sectionReport){
        this.sectionReport = sectionReport;
    }

    @Override
    public SectionInNode usingEvent(ReportNodeEvent event) {
        return new SectionInNodeImpl(sectionReport, event);
    }
}
