package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.report.BasicReportBuilder;
import org.arquillian.reporter.api.model.report.BasicReport;

/**
 * An implementation of {@link BasicReportBuilder} used for building {@link BasicReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class BasicReportBuilderImpl extends AbstractReportBuilder<BasicReportBuilder, BasicReport>
    implements BasicReportBuilder {

    public BasicReportBuilderImpl(BasicReport report) {
        super(report);
    }
}
