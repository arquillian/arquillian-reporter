package org.arquillian.reporter.impl.builder.report;

import org.arquillian.reporter.api.builder.report.AbstractReportBuilder;
import org.arquillian.reporter.api.builder.report.ReportBuilder;
import org.arquillian.reporter.api.model.report.AbstractReport;

/**
 * An implementation of {@link ReportBuilder} used for building any {@link AbstractReport} implementation
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ReportBuilderImpl<REPORTTYPE extends AbstractReport<REPORTTYPE,ReportBuilder>> extends
    AbstractReportBuilder<ReportBuilderImpl<REPORTTYPE>, REPORTTYPE> {

    public ReportBuilderImpl(REPORTTYPE sectionReport) {
        super(sectionReport);
    }

}
