package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.FailureReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;

/**
 * A {@link Report} implementation that represents any report information related to a failure.
 * It provides the basic implementation of using a name, list of entries and list of sub-reports
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FailureReport extends AbstractReport<FailureReport, FailureReportBuilder> {
    /**
     * Creates an instance of {@link TestClassReport}
     */
    public FailureReport() {
    }

    /**
     * Creates an instance of {@link TestClassReport} with the given {@link StringKey} set as a name
     *
     * @param name A {@link StringKey} to be set as a name
     */
    public FailureReport(StringKey name) {
        super(name);
    }

    /**
     * Creates an instance of {@link TestClassReport} with the given String set as a name as {@link UnknownStringKey}
     *
     * @param name A String to be set as a name as {@link UnknownStringKey}
     */
    public FailureReport(String name) {
        super(name);
    }

    @Override
    public Class<FailureReportBuilder> getReportBuilderClass() {
        return FailureReportBuilder.class;
    }

    /**
     * It uses only the default functionality of merging (see {@link AbstractReport#defaultMerge(AbstractReport)}).
     * Takes the entries and sub-reports contained in the given {@link FailureReport}
     * and adds them into the list of entries and sub-reports respectively withing this instance of report.
     *
     * @param newReport A {@link FailureReport} to be merged
     * @return This same instance of {@link FailureReport}
     */
    public FailureReport merge(FailureReport newReport) {
        defaultMerge(newReport);
        return this;
    }

    /**
     * Takes the given {@link Report} and adds it into the list of sub-reports.
     *
     * @param newReport A {@link Report} to be added
     * @return The same instance of {@link Report} that has been added
     */
    public Report addNewReport(Report newReport) {
        getSubReports().add(newReport);
        return newReport;
    }
}
