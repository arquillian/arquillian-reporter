package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.impl.SectionBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionReport extends AbstractSectionReport<SectionReport,SectionBuilderImpl> {

    public SectionReport(String name) {
        super(name);
    }

    public SectionReport merge(SectionReport newSectionReport) {
        defaultMerge(newSectionReport);
        return this;
    }

    public SectionBuilderImpl getSectionBuilderClass() {
        return new SectionBuilderImpl(this);
    }

}
