package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.ConfigurationReportBuilder;
import org.arquillian.reporter.api.model.StringKey;
import org.arquillian.reporter.api.model.UnknownStringKey;

/**
 * A {@link Report} implementation that represents any report information related to a configuration.
 * It provides the basic implementation of using a name, list of entries and list of sub-reports
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ConfigurationReport extends AbstractReport<ConfigurationReport, ConfigurationReportBuilder> {

    /**
     * Creates an instance of {@link ConfigurationReport}
     */
    public ConfigurationReport() {
    }

    /**
     * Creates an instance of {@link ConfigurationReport} with the given {@link StringKey} set as a name
     *
     * @param name A {@link StringKey} to be set as a name
     */
    public ConfigurationReport(StringKey name) {
        super(name);
    }

    /**
     * Creates an instance of {@link ConfigurationReport} with the given String set as a name as {@link UnknownStringKey}
     *
     * @param name A String to be set as a name as {@link UnknownStringKey}
     */
    public ConfigurationReport(String name) {
        super(name);
    }

    @Override
    public Class<ConfigurationReportBuilder> getReportBuilderClass() {
        return ConfigurationReportBuilder.class;
    }

    /**
     * It uses only the default functionality of merging (see {@link AbstractReport#defaultMerge(AbstractReport)}).
     * Takes the entries and sub-reports contained in the given {@link ConfigurationReport}
     * and adds them into the list of entries and sub-reports respectively withing this instance of report.
     *
     * @param newReport A {@link ConfigurationReport} to be merged
     * @return This same instance of {@link ConfigurationReport}
     */
    public ConfigurationReport merge(ConfigurationReport newReport) {
        defaultMerge(newReport);
        return this;
    }

    /**
     * Takes the given {@link Report} and adds it into the list of sub-reports.
     *
     * @param newReport A {@link Report} to be added
     * @param expectedReportTypeClass A {@link Report} class of a type that is expected as the default one of the given report
     * @return If the given report's type is {@link ConfigurationReport} then it returns the same instance of {@link Report} that has been added; otherwise null.
     */
    public Report addNewReport(Report newReport, Class<? extends Report> expectedReportTypeClass) {
        getSubReports().add(newReport);

        Class<? extends Report> newReportClass = newReport.getClass();
        if (ConfigurationReport.class.isAssignableFrom(newReportClass)){
            return newReport;
        }
        return null;
    }
}
