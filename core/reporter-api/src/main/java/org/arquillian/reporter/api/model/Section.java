package org.arquillian.reporter.api.model;

import org.arquillian.reporter.api.utils.SectionBuilderImpl;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Section extends AbstractSection<Section,SectionBuilderImpl> {

    public Section(String name) {
        super(name);
    }

    public Section merge(Section newSection) {
        defaultMerge(newSection);
        return this;
    }

    public SectionBuilderImpl getSectionBuilderClass() {
        return new SectionBuilderImpl(this);
    }

}
