package org.arquillian.reporter.impl.utils.dummy;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestMethodConfigurationSection;
import org.arquillian.reporter.api.model.report.BasicReport;
import org.arquillian.reporter.api.model.report.Report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class SectionUnderTestMethodConfigSection extends SectionEvent<SectionUnderTestMethodConfigSection, BasicReport, TestMethodConfigurationSection> {

    private TestMethodConfigurationSection testMethodConfigSection;

    public SectionUnderTestMethodConfigSection(){
    }

    public SectionUnderTestMethodConfigSection(BasicReport sectionReport) {
        super(sectionReport);
    }

    public <T extends Report> SectionUnderTestMethodConfigSection(BasicReport sectionReport, String sectionId, TestMethodConfigurationSection testMethodConfigSection) {
        super(sectionReport, sectionId);
        this.testMethodConfigSection = testMethodConfigSection;
    }

    @Override
    public TestMethodConfigurationSection getParentSectionThisSectionBelongsTo() {
        return testMethodConfigSection == null ? new TestMethodConfigurationSection() : testMethodConfigSection;
    }

    @Override
    public Class<BasicReport> getReportTypeClass() {
        return BasicReport.class;
    }
}
