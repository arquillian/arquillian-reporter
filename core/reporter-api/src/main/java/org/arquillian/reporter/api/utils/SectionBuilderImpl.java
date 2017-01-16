package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.model.AbstractSectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionBuilderImpl<T extends AbstractSectionReport<T, SectionBuilder>> extends AbstractSectionBuilderImpl<T, SectionBuilderImpl> {

    public SectionBuilderImpl(T sectionReport) {
        super(sectionReport);
    }

}
