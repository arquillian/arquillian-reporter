package org.arquillian.reporter.api.model;

import org.arquillian.reporter.api.utils.SectionBuilder;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionReport extends AbstractSectionReport<SectionReport, SectionBuilder> {

    public SectionReport(String name) {
        super(name);
    }

    public SectionReport merge(SectionReport newSection) {
        defaultMerge(newSection);
        return this;
    }

    @Override
    public Class<SectionBuilder> getSectionBuilderClass() {
        return SectionBuilder.class;
    }

}
