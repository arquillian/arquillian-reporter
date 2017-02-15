package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.ConfigurationReportBuilder;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ConfigurationReport extends AbstractReport<ConfigurationReport, ConfigurationReportBuilder> {

    public ConfigurationReport() {
    }

    public ConfigurationReport(StringKey name) {
        super(name);
    }

    public ConfigurationReport(String name) {
        super(name);
    }

    @Override
    public Class<ConfigurationReportBuilder> getReportBuilderClass() {
        return ConfigurationReportBuilder.class;
    }

    @Override
    public ConfigurationReport merge(ConfigurationReport newReport) {
        defaultMerge(newReport);
        return this;
    }

    @Override
    public Report addNewReport(Report newReport) {
        getSubReports().add(newReport);
        return newReport;
    }
}
