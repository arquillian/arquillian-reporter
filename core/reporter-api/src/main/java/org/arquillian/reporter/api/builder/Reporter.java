package org.arquillian.reporter.api.builder;

import org.arquillian.reporter.api.builder.impl.FireSectionImpl;
import org.arquillian.reporter.api.builder.impl.SectionBuilderImpl;
import org.arquillian.reporter.api.model.report.AbstractSectionReport;
import org.arquillian.reporter.api.model.report.SectionReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Reporter {

    public static SectionBuilder section(String name){
        return new SectionBuilderImpl(new SectionReport(name));
    }

    public static <T extends SectionBuilder<? extends AbstractSectionReport,T>, S extends AbstractSectionReport<? extends AbstractSectionReport, T>> T section(S sectionReport) {
        return sectionReport.getSectionBuilderClass();
    }

    public static FireSection reportSection(SectionReport sectionReport){
        return new FireSectionImpl(sectionReport);
    }

//    public static CreateNode createNode(ReportNodeEvent reportNodeEvent){
//        return new CreateNode()
//    }
}
