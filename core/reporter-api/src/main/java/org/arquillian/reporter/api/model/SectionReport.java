package org.arquillian.reporter.api.model;

import org.arquillian.reporter.api.utils.SectionBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionReport extends AbstractSectionReport<SectionReport, SectionBuilderImpl> {

    public SectionReport(String name) {
        super(name);
    }

    public SectionReport merge(SectionReport newSection) {
        defaultMerge(newSection);
        return this;
    }

    @Override
    public SectionBuilderImpl getSectionBuilderClass() {
        return new SectionBuilderImpl(this);
    }

}
