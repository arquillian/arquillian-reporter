package org.arquillian.reporter.api.builder;

import org.arquillian.reporter.api.builder.impl.FireReportImpl;
import org.arquillian.reporter.api.builder.impl.ReportBuilderImpl;
import org.arquillian.reporter.api.builder.impl.TableBuilderImpl;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.report.AbstractReport;
import org.arquillian.reporter.api.model.report.Report;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Reporter {

    public static ReportBuilder createReport(StringKey name){
        return new ReportBuilderImpl(new Report(name));
    }

    public static <T extends ReportBuilder<? extends AbstractReport,T>, S extends AbstractReport<? extends AbstractReport, T>> T createReport(S report) {
        return report.getReportBuilderClass();
    }

    public static FireReport reportSection(Report sectionReport){
        return new FireReportImpl(sectionReport);
    }

    public static TableBuilder createTable(String name) {
        return new TableBuilderImpl(name);
    }

    public static TableBuilder createTable(StringKey name) {
        return new TableBuilderImpl(name);
    }

//    public static CreateNode createNode(ReportNodeEvent reportNodeEvent){
//        return new CreateNode()
//    }
}
