package org.arquillian.reporter.api.builder.impl;

import org.arquillian.reporter.api.builder.AbstractSectionBuilder;
import org.arquillian.reporter.api.builder.SectionBuilder;
import org.arquillian.reporter.api.model.report.AbstractSectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionBuilderImpl<T extends AbstractSectionReport<T,SectionBuilder>> extends
    AbstractSectionBuilder<T, SectionBuilderImpl> {

    public SectionBuilderImpl(T sectionReport) {
        super(sectionReport);
    }

}
