package org.arquillian.reporter.impl.section;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestMethodConfigurationSection;
import org.arquillian.reporter.api.model.report.Report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionUnderTestMethodConfigSection extends SectionEvent<SectionUnderTestMethodConfigSection, Report, TestMethodConfigurationSection> {

    public SectionUnderTestMethodConfigSection(){
    }


    @Override
    public TestMethodConfigurationSection getParentSectionThisSectionBelongsTo() {
        return new TestMethodConfigurationSection();
    }

    @Override
    public Class<Report> getReportTypeClass() {
        return null;
    }
}
