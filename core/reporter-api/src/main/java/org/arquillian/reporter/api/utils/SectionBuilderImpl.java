package org.arquillian.reporter.api.utils;

import org.arquillian.reporter.api.model.AbstractSection;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionBuilderImpl<T extends AbstractSection<T,SectionBuilder>> extends AbstractSectionBuilderImpl<T, SectionBuilderImpl> {

    public SectionBuilderImpl(T sectionReport) {
        super(sectionReport);
    }

}
