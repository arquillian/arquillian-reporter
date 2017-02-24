package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.report.FailureReportBuilder;
import org.arquillian.reporter.api.model.report.FailureReport;

/**
 * An implementation of {@link FailureReportBuilder} used for building {@link FailureReport}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class FailureReportBuilderImpl extends AbstractReportBuilder<FailureReportBuilder, FailureReport>
    implements FailureReportBuilder {

    public FailureReportBuilderImpl(FailureReport report) {
        super(report);
    }
}
