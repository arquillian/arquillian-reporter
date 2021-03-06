package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.report.ConfigurationReportBuilder;
import org.arquillian.reporter.api.model.report.ConfigurationReport;

/**
 * An implementation of {@link ConfigurationReportBuilder} used for building {@link ConfigurationReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ConfigurationReportBuilderImpl
    extends AbstractReportBuilder<ConfigurationReportBuilder, ConfigurationReport>
    implements ConfigurationReportBuilder {

    public ConfigurationReportBuilderImpl(ConfigurationReport report) {
        super(report);
    }
}
