package org.arquillian.reporter.impl.utils.dummy;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestMethodConfigurationSection;
import org.arquillian.reporter.api.model.report.BasicReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionUnderTestMethodConfigSection extends SectionEvent<SectionUnderTestMethodConfigSection, BasicReport, TestMethodConfigurationSection> {

    public SectionUnderTestMethodConfigSection(){
    }


    @Override
    public TestMethodConfigurationSection getParentSectionThisSectionBelongsTo() {
        return new TestMethodConfigurationSection();
    }

    @Override
    public Class<BasicReport> getReportTypeClass() {
        return BasicReport.class;
    }
}
