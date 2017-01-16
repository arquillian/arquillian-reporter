package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.model.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionBuilderImpl extends AbstractSectionBuilderImpl<SectionReport, SectionBuilderImpl> {

    public SectionBuilderImpl(String name) {
        super(name);
    }

    @Override
    protected SectionReport createSectionInstanceWithName(String name) {
        return new SectionReport(name);
    }

    public SectionBuilderImpl(SectionReport sectionReport) {
        super(sectionReport);
    }
}
