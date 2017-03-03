package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.BasicReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;

/**
 * A {@link Report} that represents a basic report that should be used for most of the cases.
 * It provides the basic implementation of using a name, list of entries and list of sub-reports
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class BasicReport extends AbstractReport<BasicReport, BasicReportBuilder> {

    /**
     * Creates an instance of {@link AbstractReport}
     */
    public BasicReport() {
    }

    /**
     * Creates an instance of {@link AbstractReport} with the given {@link StringKey} set as a name
     *
     * @param name A {@link StringKey} to be set as a name
     */
    public BasicReport(StringKey name) {
        super(name);
    }

    /**
     * Creates an instance of {@link AbstractReport} with the given String set as a name as {@link UnknownStringKey}
     *
     * @param name A String to be set as a name as {@link UnknownStringKey}
     */
    public BasicReport(String name) {
        super(new UnknownStringKey(name));
    }

    /**
     * It uses only the default functionality of merging (see {@link AbstractReport#defaultMerge(AbstractReport)}).
     * Takes the entries and sub-reports contained in the given {@link BasicReport}
     * and adds them into the list of entries and sub-reports respectively withing this instance of report.
     *
     * @param newBasicReport A {@link BasicReport} to be merged
     * @return This same instance of {@link BasicReport}
     */
    public BasicReport merge(BasicReport newBasicReport) {
        defaultMerge(newBasicReport);
        return this;
    }

    /**
     * Takes the given {@link Report} and adds it into the list of sub-reports.
     *
     * @param newReport A {@link Report} to be added
     * @param expectedReportTypeClass A {@link Report} class of a type that is expected as the default one of the given report
     * @return The same instance of {@link Report} that has been added
     */
    public Report addNewReport(Report newReport, Class<? extends Report> expectedReportTypeClass) {
        getSubReports().add(newReport);
        return newReport;
    }

    @Override
    public Class<BasicReportBuilder> getReportBuilderClass() {
        return BasicReportBuilder.class;
    }
}
