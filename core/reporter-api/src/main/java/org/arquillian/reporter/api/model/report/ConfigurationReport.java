package org.arquillian.reporter.api.model.report;

import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.model.StringKey;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ConfigurationReport extends AbstractReport<ConfigurationReport, ReportBuilder> {

    public ConfigurationReport() {
    }

    public ConfigurationReport(StringKey name) {
        super(name);
    }

    @Override
    public Class<ReportBuilder> getReportBuilderClass() {
        return ReportBuilder.class;
    }

    @Override
    public ConfigurationReport merge(ConfigurationReport newReport) {
        defaultMerge(newReport);
        return this;
    }

    @Override
    public AbstractReport addNewReport(AbstractReport newReport) {
        getSubreports().add(newReport);
        return newReport;
    }
}
